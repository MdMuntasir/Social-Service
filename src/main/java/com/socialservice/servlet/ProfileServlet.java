package com.socialservice.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import com.socialservice.model.Profile;
import com.socialservice.model.User;
import com.socialservice.repository.ProfileRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/api/profiles/*")
public class ProfileServlet extends HttpServlet {
    private final ProfileRepository profileRepository = new ProfileRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\":\"User ID required\"}");
            return;
        }
        Long userId = Long.parseLong(pathInfo.substring(1));
        Profile profile = profileRepository.findByUserId(userId);
        if (profile == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("{\"error\":\"Profile not found\"}");
            return;
        }
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(new JSONObject(profile).toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        User user = (User) req.getAttribute("user");
        if (!user.getRole().equals("ROLE_FREELANCER")) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.getWriter().write("{\"error\":\"Only freelancers can update profiles\"}");
            return;
        }
        JSONObject json = readJsonBody(req);
        Profile profile = new Profile();
        profile.setUserId(user.getId());
        profile.setName(json.getString("name"));
        profile.setWorkField(json.getString("workField"));
        profile.setExperience(json.getString("experience"));
        profile.setAvailable(json.getBoolean("isAvailable"));
        profile = profileRepository.save(profile);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(new JSONObject(profile).toString());
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
