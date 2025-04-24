package com.socialservice.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialservice.model.JobRequest;

public class JobRequestRepository {
    private static final String URL = "jdbc:mysql://localhost:3306/social_service_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Dev2700#";

    public JobRequest save(JobRequest request) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO job_request (job_id, freelancer_id, status) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, request.getJobId());
            stmt.setLong(2, request.getFreelancerId());
            stmt.setString(3, request.getStatus());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                request.setId(rs.getLong(1));
            }
            return request;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<JobRequest> findByJobId(Long jobId) {
        List<JobRequest> requests = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id, job_id, freelancer_id, status FROM job_request WHERE job_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, jobId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JobRequest request = new JobRequest();
                request.setId(rs.getLong("id"));
                request.setJobId(rs.getLong("job_id"));
                request.setFreelancerId(rs.getLong("freelancer_id"));
                request.setStatus(rs.getString("status"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public JobRequest update(JobRequest request) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE job_request SET status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, request.getStatus());
            stmt.setLong(2, request.getId());
            stmt.executeUpdate();
            return request;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
