package com.socialservice.client;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.json.JSONObject;

public class SocialServiceClient {
    private static final String BASE_URL = "http://localhost:8080/SocialService/api";
    private static String authHeader = null;
    
    // Custom colors
    private static final Color PRIMARY_COLOR = new Color(51, 153, 255);    // Blue
    private static final Color SECONDARY_COLOR = new Color(240, 240, 240); // Light Gray
    private static final Color TEXT_COLOR = new Color(51, 51, 51);        // Dark Gray
    private static final Color ACCENT_COLOR = new Color(255, 102, 102);   // Coral Red
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Social Service System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new CardLayout());

            // Main Panel with gradient background
            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    int w = getWidth();
                    int h = getHeight();
                    GradientPaint gp = new GradientPaint(0, 0, new Color(240, 248, 255), 0, h, new Color(202, 225, 255));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, w, h);
                }
            };
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

            // Title Label
            JLabel titleLabel = new JLabel("Welcome to Social Service System");
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(titleLabel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

            // Button Panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setOpaque(false);
            
            JButton hirerButton = createStyledButton("Hirer", "Post and manage jobs");
            JButton freelancerButton = createStyledButton("Freelancer", "Find and apply for jobs");
            JButton headButton = createStyledButton("Service Head", "Manage service operations");

            buttonPanel.add(hirerButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            buttonPanel.add(freelancerButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            buttonPanel.add(headButton);

            mainPanel.add(buttonPanel);

            // Create other panels
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

    private static JButton createStyledButton(String text, String description) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(PRIMARY_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(PRIMARY_COLOR.brighter());
                } else {
                    g2.setColor(PRIMARY_COLOR);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setLayout(new BoxLayout(button, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(REGULAR_FONT);
        descLabel.setForeground(Color.WHITE);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        button.add(Box.createRigidArea(new Dimension(0, 10)));
        button.add(titleLabel);
        button.add(Box.createRigidArea(new Dimension(0, 5)));
        button.add(descLabel);
        button.add(Box.createRigidArea(new Dimension(0, 10)));
        
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(300, 80));
        button.setMaximumSize(new Dimension(300, 80));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        return button;
    }

    private static JPanel createStyledFormPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255), 0, h, SECONDARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        return panel;
    }

    private static JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(REGULAR_FONT);
        field.setMaximumSize(new Dimension(300, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private static JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(REGULAR_FONT);
        field.setMaximumSize(new Dimension(300, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private static JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(HEADER_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(300, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private static JPanel createHirerPanel(JFrame frame) {
        JPanel panel = createStyledFormPanel();
        
        JLabel titleLabel = new JLabel("Post a New Job");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JTextField titleField = createStyledTextField();
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setFont(REGULAR_FONT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setMaximumSize(new Dimension(300, 100));
        
        JTextField rewardField = createStyledTextField();
        
        panel.add(new JLabel("Job Title"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(titleField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(new JLabel("Description"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(new JLabel("Reward Amount"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(rewardField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton submitButton = createActionButton("Post Job");
        JButton backButton = createActionButton("Back to Main Menu");
        backButton.setBackground(ACCENT_COLOR);

        submitButton.addActionListener(e -> {
            try {
                JSONObject json = new JSONObject();
                json.put("title", titleField.getText());
                json.put("description", descriptionArea.getText());
                json.put("reward", Double.parseDouble(rewardField.getText()));
                String response = sendPostRequest("/jobs", json.toString(), null);
                JOptionPane.showMessageDialog(frame, "Job posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main"));

        panel.add(submitButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(backButton);

        return panel;
    }

    private static JPanel createFreelancerLoginPanel(JFrame frame) {
        JPanel panel = createStyledFormPanel();
        
        JLabel titleLabel = new JLabel("Freelancer Access");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JTextField usernameField = createStyledTextField();
        JPasswordField passwordField = createStyledPasswordField();
        
        panel.add(new JLabel("Username"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(new JLabel("Password"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton loginButton = createActionButton("Login");
        JButton registerButton = createActionButton("Register New Account");
        registerButton.setBackground(new Color(46, 204, 113)); // Green color for register
        JButton backButton = createActionButton("Back to Main Menu");
        backButton.setBackground(ACCENT_COLOR);

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
                JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            JPanel registerPanel = createFreelancerRegisterPanel(frame);
            frame.add(registerPanel, "Register");
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Register");
        });

        backButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main"));

        panel.add(loginButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(registerButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(backButton);

        return panel;
    }

    private static JPanel createFreelancerRegisterPanel(JFrame frame) {
        JPanel panel = createStyledFormPanel();
        
        JLabel titleLabel = new JLabel("Freelancer Registration");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JTextField usernameField = createStyledTextField();
        JPasswordField passwordField = createStyledPasswordField();
        JTextField nameField = createStyledTextField();
        JTextField workFieldField = createStyledTextField();
        JTextArea experienceArea = new JTextArea();
        experienceArea.setFont(REGULAR_FONT);
        experienceArea.setLineWrap(true);
        experienceArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(experienceArea);
        scrollPane.setMaximumSize(new Dimension(300, 100));

        // Add form fields with proper spacing
        String[] labels = {"Username", "Password", "Full Name", "Work Field", "Experience"};
        JComponent[] fields = {usernameField, passwordField, nameField, workFieldField, scrollPane};
        
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(fields[i]);
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JButton submitButton = createActionButton("Complete Registration");
        submitButton.setBackground(new Color(46, 204, 113)); // Green color
        JButton backButton = createActionButton("Back to Login");
        backButton.setBackground(ACCENT_COLOR);

        submitButton.addActionListener(e -> {
            try {
                JSONObject json = new JSONObject();
                json.put("username", usernameField.getText());
                json.put("password", new String(passwordField.getPassword()));
                json.put("name", nameField.getText());
                json.put("workField", workFieldField.getText());
                json.put("experience", experienceArea.getText());
                String response = sendPostRequest("/users", json.toString(), null);
                JOptionPane.showMessageDialog(frame, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "FreelancerLogin");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "FreelancerLogin"));

        panel.add(submitButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(backButton);

        return panel;
    }

    private static JPanel createFreelancerPanel(JFrame frame, String username) {
        JPanel panel = createStyledFormPanel();
        
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Available Jobs Section
        JLabel jobsLabel = new JLabel("Available Jobs");
        jobsLabel.setFont(HEADER_FONT);
        jobsLabel.setForeground(TEXT_COLOR);
        jobsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(jobsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextArea jobListArea = new JTextArea();
        jobListArea.setFont(REGULAR_FONT);
        jobListArea.setEditable(false);
        jobListArea.setLineWrap(true);
        jobListArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(jobListArea);
        scrollPane.setMaximumSize(new Dimension(500, 200));
        scrollPane.setPreferredSize(new Dimension(500, 200));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Job Request Section
        JPanel requestPanel = new JPanel();
        requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.X_AXIS));
        requestPanel.setOpaque(false);
        requestPanel.setMaximumSize(new Dimension(500, 35));
        
        JLabel jobIdLabel = new JLabel("Job ID: ");
        jobIdLabel.setFont(REGULAR_FONT);
        JTextField jobIdField = createStyledTextField();
        jobIdField.setMaximumSize(new Dimension(100, 30));
        
        requestPanel.add(jobIdLabel);
        requestPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        requestPanel.add(jobIdField);
        requestPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        JButton requestButton = createActionButton("Request Job");
        requestButton.setMaximumSize(new Dimension(150, 35));
        requestPanel.add(requestButton);
        
        panel.add(requestPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Action Buttons
        JButton profileButton = createActionButton("Update Profile");
        profileButton.setBackground(new Color(142, 68, 173)); // Purple color for profile
        JButton logoutButton = createActionButton("Logout");
        logoutButton.setBackground(ACCENT_COLOR);

        try {
            String jobs = sendGetRequest("/jobs/open", authHeader);
            jobListArea.setText(jobs);
        } catch (Exception ex) {
            jobListArea.setText("Error loading jobs: " + ex.getMessage());
        }

        requestButton.addActionListener(e -> {
            try {
                JSONObject json = new JSONObject();
                json.put("jobId", Long.parseLong(jobIdField.getText()));
                String response = sendPostRequest("/job-requests", json.toString(), authHeader);
                JOptionPane.showMessageDialog(frame, "Job request sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                jobIdField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        panel.add(profileButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(logoutButton);

        return panel;
    }

    private static JPanel createProfilePanel(JFrame frame) {
        JPanel panel = createStyledFormPanel();
        
        JLabel titleLabel = new JLabel("Update Profile");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JTextField nameField = createStyledTextField();
        JTextField workFieldField = createStyledTextField();
        JTextArea experienceArea = new JTextArea();
        experienceArea.setFont(REGULAR_FONT);
        experienceArea.setLineWrap(true);
        experienceArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(experienceArea);
        scrollPane.setMaximumSize(new Dimension(300, 100));
        
        JCheckBox availableCheck = new JCheckBox("Available for Work");
        availableCheck.setFont(REGULAR_FONT);
        availableCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        availableCheck.setOpaque(false);

        panel.add(new JLabel("Full Name"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(nameField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(new JLabel("Work Field"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(workFieldField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(new JLabel("Experience"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(availableCheck);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton submitButton = createActionButton("Save Changes");
        submitButton.setBackground(new Color(46, 204, 113)); // Green color
        JButton backButton = createActionButton("Back to Dashboard");
        backButton.setBackground(ACCENT_COLOR);

        submitButton.addActionListener(e -> {
            try {
                JSONObject json = new JSONObject();
                json.put("name", nameField.getText());
                json.put("workField", workFieldField.getText());
                json.put("experience", experienceArea.getText());
                json.put("isAvailable", availableCheck.isSelected());
                String response = sendPostRequest("/profiles", json.toString(), authHeader);
                JOptionPane.showMessageDialog(frame, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Freelancer"));

        panel.add(submitButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(backButton);

        return panel;
    }

    private static JPanel createHeadLoginPanel(JFrame frame) {
        JPanel panel = createStyledFormPanel();
        
        JLabel titleLabel = new JLabel("Service Head Access");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JTextField usernameField = createStyledTextField();
        JPasswordField passwordField = createStyledPasswordField();
        
        panel.add(new JLabel("Username"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(new JLabel("Password"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton loginButton = createActionButton("Login");
        JButton backButton = createActionButton("Back to Main Menu");
        backButton.setBackground(ACCENT_COLOR);

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
                JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main"));

        panel.add(loginButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(backButton);

        return panel;
    }

    private static JPanel createHeadPanel(JFrame frame) {
        JPanel panel = createStyledFormPanel();
        
        JLabel titleLabel = new JLabel("Service Head Dashboard");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Jobs Section
        JLabel jobsLabel = new JLabel("All Jobs");
        jobsLabel.setFont(HEADER_FONT);
        jobsLabel.setForeground(TEXT_COLOR);
        jobsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(jobsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextArea jobListArea = new JTextArea();
        jobListArea.setFont(REGULAR_FONT);
        jobListArea.setEditable(false);
        jobListArea.setLineWrap(true);
        jobListArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(jobListArea);
        scrollPane.setMaximumSize(new Dimension(500, 200));
        scrollPane.setPreferredSize(new Dimension(500, 200));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Assignment Section
        JPanel assignmentPanel = new JPanel();
        assignmentPanel.setLayout(new BoxLayout(assignmentPanel, BoxLayout.Y_AXIS));
        assignmentPanel.setOpaque(false);
        assignmentPanel.setMaximumSize(new Dimension(500, 100));
        
        JPanel jobIdPanel = new JPanel();
        jobIdPanel.setLayout(new BoxLayout(jobIdPanel, BoxLayout.X_AXIS));
        jobIdPanel.setOpaque(false);
        JLabel jobIdLabel = new JLabel("Job ID: ");
        jobIdLabel.setFont(REGULAR_FONT);
        JTextField jobIdField = createStyledTextField();
        jobIdField.setMaximumSize(new Dimension(100, 30));
        jobIdPanel.add(jobIdLabel);
        jobIdPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        jobIdPanel.add(jobIdField);
        
        JPanel freelancerIdPanel = new JPanel();
        freelancerIdPanel.setLayout(new BoxLayout(freelancerIdPanel, BoxLayout.X_AXIS));
        freelancerIdPanel.setOpaque(false);
        JLabel freelancerIdLabel = new JLabel("Freelancer ID: ");
        freelancerIdLabel.setFont(REGULAR_FONT);
        JTextField freelancerIdField = createStyledTextField();
        freelancerIdField.setMaximumSize(new Dimension(100, 30));
        freelancerIdPanel.add(freelancerIdLabel);
        freelancerIdPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        freelancerIdPanel.add(freelancerIdField);
        
        assignmentPanel.add(jobIdPanel);
        assignmentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        assignmentPanel.add(freelancerIdPanel);
        
        panel.add(assignmentPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Action Buttons
        JButton assignButton = createActionButton("Assign Job");
        assignButton.setBackground(new Color(46, 204, 113)); // Green color
        JButton viewProfileButton = createActionButton("View Freelancer Profile");
        viewProfileButton.setBackground(new Color(142, 68, 173)); // Purple color
        JButton logoutButton = createActionButton("Logout");
        logoutButton.setBackground(ACCENT_COLOR);

        try {
            String jobs = sendGetRequest("/jobs", authHeader);
            jobListArea.setText(jobs);
        } catch (Exception ex) {
            jobListArea.setText("Error loading jobs: " + ex.getMessage());
        }

        assignButton.addActionListener(e -> {
            try {
                String url = "/jobs/assign/" + jobIdField.getText() + "/" + freelancerIdField.getText();
                String response = sendPostRequest(url, "{}", authHeader);
                JOptionPane.showMessageDialog(frame, "Job assigned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                jobListArea.setText(sendGetRequest("/jobs", authHeader));
                jobIdField.setText("");
                freelancerIdField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        viewProfileButton.addActionListener(e -> {
            try {
                String response = sendGetRequest("/profiles/" + freelancerIdField.getText(), authHeader);
                JOptionPane.showMessageDialog(frame, "Freelancer Profile:\n\n" + response, "Profile Information", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        logoutButton.addActionListener(e -> {
            authHeader = null;
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });

        panel.add(assignButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(viewProfileButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(logoutButton);

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
