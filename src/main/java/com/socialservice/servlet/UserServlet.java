package com.socialservice.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import com.socialservice.model.Profile;
import com.socialservice.model.User;
import com.socialservice.repository.ProfileRepository;
import com.socialservice.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/api/users")
public class UserServlet extends HttpServlet {
    private final UserRepository userRepository = new UserRepository();
    private final ProfileRepository profileRepository = new ProfileRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        JSONObject json = readJsonBody(req);

        String username = json.getString("username");
        String password = json.getString("password");
        String name = json.getString("name");
        String workField = json.getString("workField");
        String experience = json.getString("experience");

        User user = new User();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setRole("ROLE_FREELANCER");
        user = userRepository.save(user);

        Profile profile = new Profile();
        profile.setUserId(user.getId());
        profile.setName(name);
        profile.setWorkField(workField);
        profile.setExperience(experience);
        profile.setAvailable(true);
        profileRepository.save(profile);

        res.setStatus(HttpServletResponse.SC_CREATED);
        res.getWriter().write(new JSONObject(user).toString());
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
