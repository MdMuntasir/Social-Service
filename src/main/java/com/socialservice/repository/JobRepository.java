package com.socialservice.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialservice.model.Job;

public class JobRepository {
    private static final String URL = "jdbc:mysql://localhost:3306/social_service_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Dev2700#";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load MySQL driver", e);
        }
    }

    public Job save(Job job) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO job (title, description, reward, status, assigned_freelancer_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, job.getTitle());
            stmt.setString(2, job.getDescription());
            stmt.setDouble(3, job.getReward());
            stmt.setString(4, job.getStatus());
            stmt.setObject(5, job.getAssignedFreelancerId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                job.setId(rs.getLong(1));
            }
            return job;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Job> findAll() {
        List<Job> jobs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id, title, description, reward, status, assigned_freelancer_id FROM job";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Job job = new Job();
                job.setId(rs.getLong("id"));
                job.setTitle(rs.getString("title"));
                job.setDescription(rs.getString("description"));
                job.setReward(rs.getDouble("reward"));
                job.setStatus(rs.getString("status"));
                job.setAssignedFreelancerId(rs.getObject("assigned_freelancer_id") != null ? rs.getLong("assigned_freelancer_id") : null);
                jobs.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public Job findById(Long id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id, title, description, reward, status, assigned_freelancer_id FROM job WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Job job = new Job();
                job.setId(rs.getLong("id"));
                job.setTitle(rs.getString("title"));
                job.setDescription(rs.getString("description"));
                job.setReward(rs.getDouble("reward"));
                job.setStatus(rs.getString("status"));
                job.setAssignedFreelancerId(rs.getObject("assigned_freelancer_id") != null ? rs.getLong("assigned_freelancer_id") : null);
                return job;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Job update(Job job) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE job SET title = ?, description = ?, reward = ?, status = ?, assigned_freelancer_id = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, job.getTitle());
            stmt.setString(2, job.getDescription());
            stmt.setDouble(3, job.getReward());
            stmt.setString(4, job.getStatus());
            stmt.setObject(5, job.getAssignedFreelancerId());
            stmt.setLong(6, job.getId());
            stmt.executeUpdate();
            return job;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
