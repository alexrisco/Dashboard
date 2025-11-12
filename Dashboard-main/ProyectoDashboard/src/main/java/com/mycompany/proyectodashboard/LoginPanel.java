package com.mycompany.proyectodashboard;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox recordarCheck;
    private JButton loginButton;
    private JLabel registrarLabel;
    private JLabel olvidasteLabel;
    private DatabaseManager dbManager;
    
    // Referencia al Dashboard Panel (¡Añadido!)
    private DashboardPanel dashboardPanel; 

    // CONSTRUCTOR CORREGIDO
    public LoginPanel(DatabaseManager dbManager, CardLayout cardLayout, JPanel mainPanel, DashboardPanel dashboardPanel) {
        this.dbManager = dbManager;
        this.dashboardPanel = dashboardPanel; // ¡Asignación clave!
        
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título "Iniciar Sesión"
        JLabel titleLabel = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);
        
        // Email
        JLabel emailLabel = new JLabel("Correo electrónico");
        gbc.gridy = 3;
        add(emailLabel, gbc);
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(280, 35));
        gbc.gridy = 4;
        add(emailField, gbc);

        // Contraseña
        JLabel passwordLabel = new JLabel("Contraseña");
        gbc.gridy = 5;
        add(passwordLabel, gbc);
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(280, 35));
        gbc.gridy = 6;
        add(passwordField, gbc);
        
        // Panel de recordar y olvidaste contraseña
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setBackground(Color.WHITE);
        recordarCheck = new JCheckBox("Recordarme");
        recordarCheck.setBackground(Color.WHITE);
        olvidasteLabel = new JLabel("¿Olvidaste tu contraseña?");
        olvidasteLabel.setForeground(new Color(220, 53, 69));
        olvidasteLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        olvidasteLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "RecuperarPanel");
            }
        });
        optionsPanel.add(recordarCheck, BorderLayout.WEST);
        optionsPanel.add(olvidasteLabel, BorderLayout.EAST);
        gbc.gridy = 7;
        add(optionsPanel, gbc);

        // Botón Iniciar Sesión
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setPreferredSize(new Dimension(280, 40));
        loginButton.setBackground(new Color(139, 92, 246));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> iniciarSesion(cardLayout, mainPanel));
        gbc.gridy = 8;
        add(loginButton, gbc);

        // Label de registro
        JPanel registroPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registroPanel.setBackground(Color.WHITE);
        JLabel noTienesCuentaLabel = new JLabel("¿No tienes una cuenta? ");
        registrarLabel = new JLabel("Regístrate aquí");
        registrarLabel.setForeground(new Color(220, 53, 69));
        registrarLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registrarLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "RegistroPanel");
            }
        });
        registroPanel.add(noTienesCuentaLabel);
        registroPanel.add(registrarLabel);
        gbc.gridy = 9;
        add(registroPanel, gbc);
    }

    private void iniciarSesion(CardLayout cardLayout, JPanel mainPanel) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, completa todos los campos", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = dbManager.iniciarSesion(email, password);

        if (usuario != null) {
            JOptionPane.showMessageDialog(this, 
                "¡Bienvenido " + usuario.getNombre() + "!", 
                "Inicio de sesión exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // 1. Cargar los datos del usuario en el Dashboard
            dashboardPanel.cargarDatosUsuario(usuario);
            
            // 2. Navegar al Dashboard
            cardLayout.show(mainPanel, "DashboardPanel");
            
            // Limpiar campos
            emailField.setText("");
            passwordField.setText("");
            
        } else {
            JOptionPane.showMessageDialog(this, 
                "Email o contraseña incorrectos", 
                "Error de autenticación", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
}