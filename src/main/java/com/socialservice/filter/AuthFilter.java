package com.socialservice.filter;

import com.socialservice.model.User;
import com.socialservice.repository.UserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Base64;

public class AuthFilter implements Filter {
    private final UserRepository userRepository = new UserRepository();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Skip authentication for public endpoints
        String uri = req.getRequestURI();
        if (req.getMethod().equals("POST") && uri.endsWith("/api/jobs")) {
            chain.doFilter(request, response);
            return;
        }
        if (req.getMethod().equals("POST") && uri.endsWith("/api/users")) {
            chain.doFilter(request, response);
            return;
        }
        if (req.getMethod().equals("GET") && uri.endsWith("/api/profiles")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            sendUnauthorized(res, "Missing or invalid Authorization header");
            return;
        }

        String base64Credentials = authHeader.substring(6);
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] parts = credentials.split(":", 2);
        if (parts.length != 2) {
            sendUnauthorized(res, "Invalid credentials format");
            return;
        }

        String username = parts[0];
        String password = parts[1];
        User user = userRepository.findByUsername(username);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            sendUnauthorized(res, "Invalid username or password");
            return;
        }

        // Role-based access control
        String role = user.getRole();
        if (uri.startsWith("/api/jobs/open") && !role.equals("ROLE_FREELANCER")) {
            sendUnauthorized(res, "Requires ROLE_FREELANCER");
            return;
        }
        if (uri.startsWith("/api/jobs/assign") && !role.equals("ROLE_HEAD")) {
            sendUnauthorized(res, "Requires ROLE_HEAD");
            return;
        }
        if (uri.startsWith("/api/job-requests") && !role.equals("ROLE_FREELANCER")) {
            sendUnauthorized(res, "Requires ROLE_FREELANCER");
            return;
        }

        req.setAttribute("user", user);
        chain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse res, String message) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        JSONObject error = new JSONObject();
        error.put("error", "Unauthorized");
        error.put("message", message);
        res.getWriter().write(error.toString());
    }
}
