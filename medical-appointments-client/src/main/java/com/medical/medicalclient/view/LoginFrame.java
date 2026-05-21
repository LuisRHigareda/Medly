package com.medical.medicalclient.view;
import com.medical.medicalclient.client.AuthClient;
import com.medical.medicalclient.client.UserClient;
import com.medical.medicalclient.dto.LoginResponse;
import com.medical.medicalclient.dto.ReceptionistResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Graphical user interface frame responsible for rendering the login form
 * and handling the initial authentication handshake with the network layer
 * @author Yuri German Garcia López - 252583
 */
public class LoginFrame extends JFrame{
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private final AuthClient authClient;
    private final UserClient userClient;

    public LoginFrame() {
        // Initialize backend service clients
        this.authClient = new AuthClient();
        this.userClient = new UserClient();

        // Configure basic frame properties
        setTitle("Medical System - Staff Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window on desktop screen
        setResizable(false);

        // Build and append the layout components
        initComponents();
    }

    private void initComponents() {
        // Central container panel with customized padding margins
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8); // Component padding spacing

        // Title Header Label
        JLabel titleLabel = new JLabel("System Authentication", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Email Form Component
        JLabel emailLabel = new JLabel("Email Address:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(emailField, gbc);

        // Password Form Component
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        // Submission Action Button
        loginButton = new JButton("Sign In");
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        // Attach event listener trigger for the submission button
        loginButton.addActionListener(new LoginActionHandler());

        // Append the configured panel setup into the main window view frame
        add(panel);
    }

    /**
     * Inner class action handler responsible for capture and routing of 
     * login events to the distributed network layer
     */
    private class LoginActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            // Simple client-side validation constraint check
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Please fill in all security credential fields.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Disable button during the network payload handshake to prevent duplicate requests
            loginButton.setEnabled(false);

            // Execute network request routing to the gateway context
            LoginResponse loginRes = authClient.login(email, password);
            
            if (loginRes != null && loginRes.isSuccess()) {
                String role = loginRes.getUserRole();
                if ("RECEPTIONIST".equalsIgnoreCase(role)) {
                    
                    // Fetch full profile details to extract the assigned clinic execution context
                    ReceptionistResponse receptionist = userClient.getReceptionistById(loginRes.getUserId());

                    if (receptionist != null) {
                        JOptionPane.showMessageDialog(LoginFrame.this,
                                "Welcome back, " + receptionist.getName() + "!\nClinic: " + receptionist.getClinicName(),
                                "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                        MainReceptionistFrame mainFrame = new MainReceptionistFrame(receptionist);
                        mainFrame.setVisible(true);
                        
                        LoginFrame.this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this,
                                "Could not recover specific receptionist workspace metadata.",
                                "Server Error", JOptionPane.ERROR_MESSAGE);
                        loginButton.setEnabled(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Access Denied: This subsystem is reserved for Receptionist roles only.",
                            "Authorization Failure", JOptionPane.ERROR_MESSAGE);
                    loginButton.setEnabled(true);
                }
            } else {
                String errorMsg = (loginRes != null) ? loginRes.getMessage() : "Connection timeout.";
                JOptionPane.showMessageDialog(LoginFrame.this,
                        errorMsg, "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                loginButton.setEnabled(true);
            }
        }
    }
}
