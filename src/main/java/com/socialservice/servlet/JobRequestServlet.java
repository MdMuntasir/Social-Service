package com.socialservice.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import com.socialservice.model.JobRequest;
import com.socialservice.model.User;
import com.socialservice.repository.JobRequestRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/api/job-requests")
public class JobRequestServlet extends HttpServlet {
    private final JobRequestRepository jobRequestRepository = new JobRequestRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        User user = (User) req.getAttribute("user");
        JSONObject json = readJsonBody(req);
        Long jobId = json.getLong("jobId");

        JobRequest request = new JobRequest();
        request.setJobId(jobId);
        request.setFreelancerId(user.getId());
        request.setStatus("Pending");
        request = jobRequestRepository.save(request);

        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(new JSONObject(request).toString());
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
