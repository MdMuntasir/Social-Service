package com.socialservice.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.socialservice.model.Job;
import com.socialservice.model.User;
import com.socialservice.repository.JobRepository;
import com.socialservice.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/api/jobs", "/api/jobs/open", "/api/jobs/assign/*"})
public class JobServlet extends HttpServlet {
    private final JobRepository jobRepository = new JobRepository();
    private final UserRepository userRepository = new UserRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getServletPath();
        res.setContentType("application/json");

        try {
            if ("/api/jobs".equals(path)) {
                JSONObject json = readJsonBody(req);
                Job job = new Job();
                job.setTitle(json.getString("title"));
                job.setDescription(json.getString("description"));
                job.setReward(json.getDouble("reward"));
                job.setStatus("Open");
                job = jobRepository.save(job);
                if (job == null) {
                    throw new ServletException("Failed to save job");
                }
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().write(new JSONObject(job).toString());
            } else if (path.startsWith("/api/jobs/assign")) {
                String pathInfo = req.getPathInfo();
                if (pathInfo == null || pathInfo.split("/").length != 3) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.getWriter().write("{\"error\":\"Invalid URL\"}");
                    return;
                }
                String[] parts = pathInfo.split("/");
                Long jobId = Long.parseLong(parts[1]);
                Long freelancerId = Long.parseLong(parts[2]);
                Job job = jobRepository.findById(jobId);
                if (job == null) {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    res.getWriter().write("{\"error\":\"Job not found\"}");
                    return;
                }
                User freelancer = userRepository.findById(freelancerId);
                if (freelancer == null || !freelancer.getRole().equals("ROLE_FREELANCER")) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.getWriter().write("{\"error\":\"Invalid freelancer\"}");
                    return;
                }
                job.setAssignedFreelancerId(freelancerId);
                job.setStatus("Assigned");
                job = jobRepository.update(job);
                if (job == null) {
                    throw new ServletException("Failed to update job");
                }
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().write(new JSONObject(job).toString());
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getServletPath();
        res.setContentType("application/json");

        try {
            if ("/api/jobs".equals(path)) {
                List<Job> jobs = jobRepository.findAll();
                JSONArray jsonArray = new JSONArray();
                for (Job job : jobs) {
                    jsonArray.put(new JSONObject(job));
                }
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().write(jsonArray.toString());
            } else if ("/api/jobs/open".equals(path)) {
                List<Job> jobs = jobRepository.findAll().stream()
                        .filter(job -> job.getStatus().equals("Open"))
                        .toList();
                JSONArray jsonArray = new JSONArray();
                for (Job job : jobs) {
                    jsonArray.put(new JSONObject(job));
                }
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().write(jsonArray.toString());
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private JSONObject readJsonBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return new JSONObject(sb.toString());
    }
}
