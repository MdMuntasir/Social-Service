package com.socialservice.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.socialservice.model.Profile;

public class ProfileRepository {
    private static final String URL = "jdbc:mysql://localhost:3306/social_service_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Dev2700#";

    public Profile findByUserId(Long userId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT user_id, name, work_field, experience, is_available FROM profile WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Profile profile = new Profile();
                profile.setUserId(rs.getLong("user_id"));
                profile.setName(rs.getString("name"));
                profile.setWorkField(rs.getString("work_field"));
                profile.setExperience(rs.getString("experience"));
                profile.setAvailable(rs.getBoolean("is_available"));
                return profile;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Profile save(Profile profile) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO profile (user_id, name, work_field, experience, is_available) VALUES (?, ?, ?, ?, ?) "
                        + "ON DUPLICATE KEY UPDATE name = ?, work_field = ?, experience = ?, is_available = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, profile.getUserId());
            stmt.setString(2, profile.getName());
            stmt.setString(3, profile.getWorkField());
            stmt.setString(4, profile.getExperience());
            stmt.setBoolean(5, profile.isAvailable());
            stmt.setString(6, profile.getName());
            stmt.setString(7, profile.getWorkField());
            stmt.setString(8, profile.getExperience());
            stmt.setBoolean(9, profile.isAvailable());
            stmt.executeUpdate();
            return profile;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
