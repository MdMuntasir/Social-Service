package com.socialservice.client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

public class SocialServiceClient {
    private static final String BASE_URL = "http://localhost:8080/SocialService/api";
    private static String authHeader = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Social Service System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new CardLayout());

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(3, 1));
            JButton hirerButton = new JButton("Hirer");
            JButton freelancerButton = new JButton("Freelancer");
            JButton headButton = new JButton("Service Head");
            mainPanel.add(hirerButton);
            mainPanel.add(freelancerButton);
            mainPanel.add(headButton);

            JPanel hirerPanel = createHirerPanel(frame);
            JPanel freelancerLoginPanel = createFreelancerLoginPanel(frame);
            JPanel headLoginPanel = createHeadLoginPanel(frame);

            frame.add(mainPanel, "Main");
            frame.add(hirerPanel, "Hirer");
            frame.add(freelancerLoginPanel, "FreelancerLogin");
            frame.add(headLoginPanel, "HeadLogin");

            CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
            hirerButton.addActionListener(e -> cl.show(frame.getContentPane(), "Hirer"));
            freelancerButton.addActionListener(e -> cl.show(frame.getContentPane(), "FreelancerLogin"));
            headButton.addActionListener(e -> cl.show(frame.getContentPane(), "HeadLogin"));

            frame.setVisible(true);
        });
    }

    private static JPanel createHirerPanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JTextField titleField = new JTextField();
        JTextArea descriptionArea = new JTextArea();
        JTextField rewardField = new JTextField();
        JButton submitButton = new JButton("Post Job");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));
        panel.add(new JLabel("Reward:"));
        panel.add(rewardField);
        panel.add(submitButton);
        panel.add(backButton);

        submitButton.addActionListener(e -> {
            try {
                JSONObject json = new JSONObject();
                json.put("title", titleField.getText());
                json.put("description", descriptionArea.getText());
                json.put("reward", Double.parseDouble(rewardField.getText()));
                String response = sendPostRequest("/jobs", json.toString(), null);
                JOptionPane.showMessageDialog(frame, "Job posted: " + response);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main"));
        return panel;
    }

    private static JPanel createFreelancerLoginPanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(backButton);

        loginButton.addActionListener(e -> {
            try {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
                String response = sendGetRequest("/jobs/open", authHeader);
                JPanel freelancerPanel = createFreelancerPanel(frame, username);
                frame.add(freelancerPanel, "Freelancer");
                ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Freelancer");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage());
            }
        });

        registerButton.addActionListener(e -> {
            JPanel registerPanel = createFreelancerRegisterPanel(frame);
            frame.add(registerPanel, "Register");
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Register");
        });

        backButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main"));
        return panel;
    }

    private static JPanel createFreelancerRegisterPanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(7, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField workFieldField = new JTextField();
        JTextArea experienceArea = new JTextArea();
        JButton submitButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Work Field:"));
        panel.add(workFieldField);
        panel.add(new JLabel("Experience:"));
        panel.add(new JScrollPane(experienceArea));
        panel.add(submitButton);
        panel.add(backButton);

        submitButton.addActionListener(e -> {
            try {
                JSONObject json = new JSONObject();
                json.put("username", usernameField.getText());
                json.put("password", new String(passwordField.getPassword()));
                json.put("name", nameField.getText());
                json.put("workField", workFieldField.getText());
                json.put("experience", experienceArea.getText());
                String response = sendPostRequest("/users", json.toString(), null);
                JOptionPane.showMessageDialog(frame, "Registered: " + response);
                ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "FreelancerLogin");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Registration failed: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "FreelancerLogin"));
        return panel;
    }

    private static JPanel createFreelancerPanel(JFrame frame, String username) {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea jobListArea = new JTextArea();
        jobListArea.setEditable(false);
        JTextField jobIdField = new JTextField();
        JButton requestButton = new JButton("Request Job");
        JButton profileButton = new JButton("Update Profile");
        JButton logoutButton = new JButton("Logout");

        try {
            String jobs = sendGetRequest("/jobs/open", authHeader);
            jobListArea.setText(jobs);
        } catch (Exception ex) {
            jobListArea.setText("Error: (Hagu) " + ex.getMessage());
        }

        JPanel bottomPanel = new JPanel(new GridLayout(2, 2));
        bottomPanel.add(new JLabel("Job ID:"));
        bottomPanel.add(jobIdField);
        bottomPanel.add(requestButton);
        bottomPanel.add(profileButton);
        bottomPanel.add(logoutButton);

        panel.add(new JScrollPane(jobListArea), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        requestButton.addActionListener(e -> {
            try {
                JSONObject json = new JSONObject();
                json.put("jobId", Long.parseLong(jobIdField.getText()));
                String response = sendPostRequest("/job-requests", json.toString(), authHeader);
                JOptionPane.showMessageDialog(frame, "Request sent: " + response);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        profileButton.addActionListener(e -> {
            JPanel profilePanel = createProfilePanel(frame);
            frame.add(profilePanel, "Profile");
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Profile");
        });

        logoutButton.addActionListener(e -> {
            authHeader = null;
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });

        return panel;
    }

    private static JPanel createProfilePanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(6, 2));
        JTextField nameField = new JTextField();
        JTextField workFieldField = new JTextField();
        JTextArea experienceArea = new JTextArea();
        JCheckBox availableCheck = new JCheckBox("Available");
        JButton submitButton = new JButton("Update Profile");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Work Field:"));
        panel.add(workFieldField);
        panel.add(new JLabel("Experience:"));
        panel.add(new JScrollPane(experienceArea));
        panel.add(new JLabel("Available:"));
        panel.add(availableCheck);
        panel.add(submitButton);
        panel.add(backButton);

        submitButton.addActionListener(e -> {
            try {
                JSONObject json = new JSONObject();
                json.put("name", nameField.getText());
                json.put("workField", workFieldField.getText());
                json.put("experience", experienceArea.getText());
                json.put("isAvailable", availableCheck.isSelected());
                String response = sendPostRequest("/profiles", json.toString(), authHeader);
                JOptionPane.showMessageDialog(frame, "Profile updated: " + response);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Freelancer"));
        return panel;
    }

    private static JPanel createHeadLoginPanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(backButton);

        loginButton.addActionListener(e -> {
            try {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
                String response = sendGetRequest("/jobs", authHeader);
                JPanel headPanel = createHeadPanel(frame);
                frame.add(headPanel, "Head");
                ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Head");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main"));
        return panel;
    }

    private static JPanel createHeadPanel(JFrame frame) {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea jobListArea = new JTextArea();
        jobListArea.setEditable(false);
        JTextField jobIdField = new JTextField();
        JTextField freelancerIdField = new JTextField();
        JButton assignButton = new JButton("Assign Job");
        JButton viewProfileButton = new JButton("View Freelancer Profile");
        JButton logoutButton = new JButton("Logout");

        try {
            String jobs = sendGetRequest("/jobs", authHeader);
            jobListArea.setText(jobs);
        } catch (Exception ex) {
            jobListArea.setText("Error: " + ex.getMessage());
        }

        JPanel bottomPanel = new JPanel(new GridLayout(3, 2));
        bottomPanel.add(new JLabel("Job ID:"));
        bottomPanel.add(jobIdField);
        bottomPanel.add(new JLabel("Freelancer ID:"));
        bottomPanel.add(freelancerIdField);
        bottomPanel.add(assignButton);
        bottomPanel.add(viewProfileButton);
        bottomPanel.add(logoutButton);

        panel.add(new JScrollPane(jobListArea), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        assignButton.addActionListener(e -> {
            try {
                String url = "/jobs/assign/" + jobIdField.getText() + "/" + freelancerIdField.getText();
                String response = sendPostRequest(url, "{}", authHeader);
                JOptionPane.showMessageDialog(frame, "Job assigned: " + response);
                jobListArea.setText(sendGetRequest("/jobs", authHeader));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        viewProfileButton.addActionListener(e -> {
            try {
                String response = sendGetRequest("/profiles/" + freelancerIdField.getText(), authHeader);
                JOptionPane.showMessageDialog(frame, "Profile: " + response);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        logoutButton.addActionListener(e -> {
            authHeader = null;
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });

        return panel;
    }

    private static String sendGetRequest(String endpoint, String authHeader) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if (authHeader != null) {
            conn.setRequestProperty("Authorization", authHeader);
        }
        conn.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        return response.toString();
    }

    private static String sendPostRequest(String endpoint, String body, String authHeader) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        if (authHeader != null) {
            conn.setRequestProperty("Authorization", authHeader);
        }
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        return response.toString();
    }
}
