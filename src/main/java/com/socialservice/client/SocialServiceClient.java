package com.socialservice.client;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
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
    private static final Font MAIN_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font SECTION_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Segoe UI Semibold", Font.PLAIN, 18);
    private static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI Semibold", Font.PLAIN, 14);
    private static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font TABLE_CONTENT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

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
            titleLabel.setFont(MAIN_TITLE_FONT);
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
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create rounded rectangle shape
                int arc = 25; // Increased arc size for more rounded corners
                RoundRectangle2D.Float r = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, arc, arc);
                
                // Create gradient paint
                GradientPaint gp;
                if (getModel().isPressed()) {
                    gp = new GradientPaint(0, 0, PRIMARY_COLOR.darker(), 0, getHeight(), PRIMARY_COLOR.darker().darker());
                } else if (getModel().isRollover()) {
                    gp = new GradientPaint(0, 0, PRIMARY_COLOR.brighter(), 0, getHeight(), PRIMARY_COLOR);
                } else {
                    gp = new GradientPaint(0, 0, PRIMARY_COLOR, 0, getHeight(), PRIMARY_COLOR.darker());
                }
                
                // Fill button with gradient
                g2.setPaint(gp);
                g2.fill(r);
                
                // Add subtle border
                g2.setColor(new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(2f));
                g2.draw(r);
                
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
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
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
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create rounded rectangle shape
                int arc = 20; // Rounded corners
                RoundRectangle2D.Float r = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, arc, arc);
                
                // Create gradient paint
                Color baseColor = getBackground();
                GradientPaint gp;
                if (getModel().isPressed()) {
                    gp = new GradientPaint(0, 0, baseColor.darker(), 0, getHeight(), baseColor.darker().darker());
                } else if (getModel().isRollover()) {
                    gp = new GradientPaint(0, 0, baseColor.brighter(), 0, getHeight(), baseColor);
                } else {
                    gp = new GradientPaint(0, 0, baseColor, 0, getHeight(), baseColor.darker());
                }
                
                // Fill button with gradient
                g2.setPaint(gp);
                g2.fill(r);
                
                // Add subtle border
                g2.setColor(new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(2f));
                g2.draw(r);
                
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // Don't paint the border
            }
        };
        
        button.setFont(HEADER_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setMaximumSize(new Dimension(300, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return button;
    }

    private static JPanel createCenteredTitlePanel(String text, Font font, Color color) {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setMaximumSize(new Dimension(800, 100));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(font);
        titleLabel.setForeground(color);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(Box.createVerticalGlue());
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Add decorative line
        JPanel linePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw gradient line
                GradientPaint gp1 = new GradientPaint(
                    0f, 0f, new Color(200, 200, 200, 0),
                    getWidth()/2f, 0f, color
                );
                GradientPaint gp2 = new GradientPaint(
                    getWidth()/2f, 0f, color,
                    getWidth(), 0f, new Color(200, 200, 200, 0)
                );
                
                g2d.setPaint(gp1);
                g2d.drawLine(0, 0, getWidth()/2, 0);
                g2d.setPaint(gp2);
                g2d.drawLine(getWidth()/2, 0, getWidth(), 0);
            }
        };
        linePanel.setPreferredSize(new Dimension(300, 2));
        linePanel.setMaximumSize(new Dimension(300, 2));
        linePanel.setOpaque(false);
        linePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(linePanel);
        titlePanel.add(Box.createVerticalGlue());
        
        return titlePanel;
    }

    private static JPanel createSectionHeader(String text) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(800, 50));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel headerLabel = new JLabel(text);
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(TEXT_COLOR);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(headerLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        return headerPanel;
    }

    private static JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(400, 600));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return formPanel;
    }

    private static void addFormField(JPanel panel, String labelText, JComponent field) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);
        fieldPanel.setMaximumSize(new Dimension(350, 70));
        fieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        fieldPanel.add(label);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldPanel.add(field);
        
        panel.add(fieldPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private static JPanel createHirerPanel(JFrame frame) {
        JPanel mainPanel = createStyledFormPanel();
        
        // Title Section
        mainPanel.add(createCenteredTitlePanel("Post a New Job", SECTION_TITLE_FONT, TEXT_COLOR));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form Section
        JPanel formPanel = createFormPanel();
        
        JTextField titleField = createStyledTextField();
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setFont(REGULAR_FONT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setMaximumSize(new Dimension(350, 100));
        
        JTextField rewardField = createStyledTextField();
        
        addFormField(formPanel, "Job Title", titleField);
        addFormField(formPanel, "Description", scrollPane);
        addFormField(formPanel, "Reward Amount ($)", rewardField);
        
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        buttonPanel.add(submitButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel);

        return mainPanel;
    }

    private static JPanel createFreelancerLoginPanel(JFrame frame) {
        JPanel mainPanel = createStyledFormPanel();
        
        // Title Section
        mainPanel.add(createCenteredTitlePanel("Freelancer Access", SECTION_TITLE_FONT, TEXT_COLOR));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form Section
        JPanel formPanel = createFormPanel();
        
        JTextField usernameField = createStyledTextField();
        JPasswordField passwordField = createStyledPasswordField();
        
        addFormField(formPanel, "Username", usernameField);
        addFormField(formPanel, "Password", passwordField);
        
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = createActionButton("Login");
        JButton registerButton = createActionButton("Register New Account");
        registerButton.setBackground(new Color(46, 204, 113));
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

        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel);

        return mainPanel;
    }

    private static JPanel createFreelancerRegisterPanel(JFrame frame) {
        JPanel mainPanel = createStyledFormPanel();
        
        // Title Section
        mainPanel.add(createCenteredTitlePanel("Freelancer Registration", SECTION_TITLE_FONT, TEXT_COLOR));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form Section
        JPanel formPanel = createFormPanel();
        
        JTextField usernameField = createStyledTextField();
        JPasswordField passwordField = createStyledPasswordField();
        JTextField nameField = createStyledTextField();
        JTextField workFieldField = createStyledTextField();
        JTextArea experienceArea = new JTextArea();
        experienceArea.setFont(REGULAR_FONT);
        experienceArea.setLineWrap(true);
        experienceArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(experienceArea);
        scrollPane.setMaximumSize(new Dimension(350, 100));

        addFormField(formPanel, "Username", usernameField);
        addFormField(formPanel, "Password", passwordField);
        addFormField(formPanel, "Full Name", nameField);
        addFormField(formPanel, "Work Field", workFieldField);
        addFormField(formPanel, "Experience", scrollPane);
        
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton submitButton = createActionButton("Complete Registration");
        submitButton.setBackground(new Color(46, 204, 113));
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

        buttonPanel.add(submitButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel);

        return mainPanel;
    }

    private static JPanel createFreelancerPanel(JFrame frame, String username) {
        JPanel mainPanel = createStyledFormPanel();
        
        // Welcome Section
        mainPanel.add(createCenteredTitlePanel("Welcome, " + username, SECTION_TITLE_FONT, TEXT_COLOR));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Jobs Section
        mainPanel.add(createSectionHeader("Available Jobs"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextArea jobListArea = createStyledJobList();
        JScrollPane scrollPane = new JScrollPane(jobListArea);
        scrollPane.setMaximumSize(new Dimension(800, 300));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Job Request Section
        JPanel requestPanel = new JPanel();
        requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.X_AXIS));
        requestPanel.setOpaque(false);
        requestPanel.setMaximumSize(new Dimension(800, 35));
        requestPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel jobIdLabel = new JLabel("Job ID: ");
        jobIdLabel.setFont(REGULAR_FONT);
        JTextField jobIdField = createStyledTextField();
        jobIdField.setMaximumSize(new Dimension(100, 30));
        
        JButton requestButton = createActionButton("Request Job");
        requestButton.setMaximumSize(new Dimension(150, 35));
        
        requestPanel.add(Box.createHorizontalGlue());
        requestPanel.add(jobIdLabel);
        requestPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        requestPanel.add(jobIdField);
        requestPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        requestPanel.add(requestButton);
        requestPanel.add(Box.createHorizontalGlue());
        
        mainPanel.add(requestPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Action Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton profileButton = createActionButton("Update Profile");
        profileButton.setBackground(new Color(142, 68, 173));
        JButton logoutButton = createActionButton("Logout");
        logoutButton.setBackground(ACCENT_COLOR);

        try {
            String jobs = sendGetRequest("/jobs/open", authHeader);
            jobListArea.setText(formatJobList(jobs));
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

        buttonPanel.add(profileButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(logoutButton);
        
        mainPanel.add(buttonPanel);

        return mainPanel;
    }

    private static JPanel createProfilePanel(JFrame frame) {
        JPanel panel = createStyledFormPanel();
        
        JLabel titleLabel = new JLabel("Update Profile");
        titleLabel.setFont(MAIN_TITLE_FONT);
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
        JPanel mainPanel = createStyledFormPanel();
        
        // Title Section
        mainPanel.add(createCenteredTitlePanel("Service Head Access", SECTION_TITLE_FONT, TEXT_COLOR));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Form Section
        JPanel formPanel = createFormPanel();
        
        JTextField usernameField = createStyledTextField();
        JPasswordField passwordField = createStyledPasswordField();
        
        addFormField(formPanel, "Username", usernameField);
        addFormField(formPanel, "Password", passwordField);
        
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel);

        return mainPanel;
    }

    private static JPanel createHeadPanel(JFrame frame) {
        JPanel mainPanel = createStyledFormPanel();
        
        // Title Section
        mainPanel.add(createCenteredTitlePanel("Service Head Dashboard", SECTION_TITLE_FONT, TEXT_COLOR));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Jobs Section
        mainPanel.add(createSectionHeader("All Jobs"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextArea jobListArea = createStyledJobList();
        JScrollPane scrollPane = new JScrollPane(jobListArea);
        scrollPane.setMaximumSize(new Dimension(800, 300));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Assignment Section
        JPanel formPanel = createFormPanel();
        
        JTextField jobIdField = createStyledTextField();
        JTextField freelancerIdField = createStyledTextField();
        
        addFormField(formPanel, "Job ID", jobIdField);
        addFormField(formPanel, "Freelancer ID", freelancerIdField);
        
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Action Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton assignButton = createActionButton("Assign Job");
        assignButton.setBackground(new Color(46, 204, 113));
        JButton viewProfileButton = createActionButton("View Freelancer Profile");
        viewProfileButton.setBackground(new Color(142, 68, 173));
        JButton logoutButton = createActionButton("Logout");
        logoutButton.setBackground(ACCENT_COLOR);

        try {
            String jobs = sendGetRequest("/jobs", authHeader);
            jobListArea.setText(formatJobList(jobs));
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

        buttonPanel.add(assignButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(viewProfileButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(logoutButton);
        
        mainPanel.add(buttonPanel);

        return mainPanel;
    }

    private static String formatJobList(String jsonResponse) {
        try {
            StringBuilder formattedText = new StringBuilder();
            formattedText.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            formattedText.append(String.format("%-6s │ %-35s │ %-15s │ %-20s │ %s%n",
                "ID", "TITLE", "REWARD", "STATUS", "DESCRIPTION"));
            formattedText.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

            org.json.JSONArray jobs = new org.json.JSONArray(jsonResponse);
            for (int i = 0; i < jobs.length(); i++) {
                org.json.JSONObject job = jobs.getJSONObject(i);
                String title = truncateString(job.getString("title"), 35);
                String description = truncateString(job.getString("description"), 40);
                String reward = String.format("$%.2f", job.getDouble("reward"));
                String status = job.optString("status", "Open");
                String id = String.valueOf(job.getLong("id"));

                formattedText.append(String.format("%-6s │ %-35s │ %-15s │ %-20s │ %s%n",
                    id, title, reward, status, description));
                
                if (i < jobs.length() - 1) {
                    formattedText.append("─".repeat(100)).append("\n");
                }
            }
            formattedText.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            return formattedText.toString();
        } catch (Exception e) {
            return "Error formatting jobs: " + e.getMessage();
        }
    }

    private static String truncateString(String str, int length) {
        if (str == null) return "";
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }

    private static JTextArea createStyledJobList() {
        JTextArea area = new JTextArea();
        area.setFont(TABLE_CONTENT_FONT);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(252, 252, 252));
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return area;
    }

    private static JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
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
