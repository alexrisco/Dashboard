package com.mycompany.proyectodashboard;

import javax.swing.*;
import java.awt.*;

public class RegistroPanel extends JPanel {
    private JTextField nombreField;
    private JTextField apellidosField;
    private JTextField emailField;
    private JTextField telefonoField;
    private JComboBox<String> generoCombo;
    private JPasswordField passwordField;
    private JPasswordField confirmarPasswordField;
    private JCheckBox mayorEdadCheck;
    private JCheckBox terminosCheck;
    private JButton crearCuentaButton;
    private DatabaseManager dbManager;

    public RegistroPanel(DatabaseManager dbManager, CardLayout cardLayout, JPanel mainPanel) {
        this.dbManager = dbManager;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 20, 3, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ... [CÓDIGO DE DISEÑO OMITIDO POR BREVEDAD, ES IDÉNTICO AL QUE PASASTE] ...

        // Título
        JLabel titleLabel = new JLabel("Crear Cuenta", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);
        
        // Campos Nombre, Apellidos, Email, Telefono, Género, Contraseñas
        // ... (Se asume el layout complejo de Nombre/Apellido y el resto de campos) ...
        
        // Nombre y Apellidos en la misma fila (Reconstrucción del layout complejo)
        JPanel nombrePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        nombrePanel.setBackground(Color.WHITE);
        
        JPanel nombreContainer = new JPanel(new BorderLayout());
        nombreContainer.setBackground(Color.WHITE);
        JLabel nombreLabel = new JLabel("Nombre");
        nombreField = new JTextField();
        nombreField.setPreferredSize(new Dimension(120, 30));
        nombreContainer.add(nombreLabel, BorderLayout.NORTH);
        nombreContainer.add(nombreField, BorderLayout.CENTER);

        JPanel apellidosContainer = new JPanel(new BorderLayout());
        apellidosContainer.setBackground(Color.WHITE);
        JLabel apellidosLabel = new JLabel("Apellidos");
        apellidosField = new JTextField();
        apellidosField.setPreferredSize(new Dimension(120, 30));
        apellidosContainer.add(apellidosLabel, BorderLayout.NORTH);
        apellidosContainer.add(apellidosField, BorderLayout.CENTER);

        nombrePanel.add(nombreContainer);
        nombrePanel.add(apellidosContainer);
        gbc.gridy = 3;
        add(nombrePanel, gbc);

        // Email
        JLabel emailLabel = new JLabel("Correo electrónico");
        gbc.gridy = 4;
        add(emailLabel, gbc);
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(280, 30));
        gbc.gridy = 5;
        add(emailField, gbc);

        // Teléfono
        JLabel telefonoLabel = new JLabel("Teléfono");
        gbc.gridy = 6;
        add(telefonoLabel, gbc);
        telefonoField = new JTextField();
        telefonoField.setPreferredSize(new Dimension(280, 30));
        gbc.gridy = 7;
        add(telefonoField, gbc);

        // Género
        JLabel generoLabel = new JLabel("Género");
        gbc.gridy = 8;
        add(generoLabel, gbc);
        String[] generos = {"Selecciona tu género", "Masculino", "Femenino", "Otro", "Prefiero no decir"};
        generoCombo = new JComboBox<>(generos);
        generoCombo.setPreferredSize(new Dimension(280, 30));
        gbc.gridy = 9;
        add(generoCombo, gbc);

        // Contraseña
        JLabel passwordLabel = new JLabel("Contraseña");
        gbc.gridy = 10;
        add(passwordLabel, gbc);
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(280, 30));
        gbc.gridy = 11;
        add(passwordField, gbc);

        // Confirmar Contraseña
        JLabel confirmarLabel = new JLabel("Confirmar contraseña");
        gbc.gridy = 12;
        add(confirmarLabel, gbc);
        confirmarPasswordField = new JPasswordField();
        confirmarPasswordField.setPreferredSize(new Dimension(280, 30));
        gbc.gridy = 13;
        add(confirmarPasswordField, gbc);
        
        // Checkboxes
        mayorEdadCheck = new JCheckBox("Confirmo que soy mayor de 18 años");
        mayorEdadCheck.setBackground(Color.WHITE);
        gbc.gridy = 14;
        add(mayorEdadCheck, gbc);

        terminosCheck = new JCheckBox("Acepto los términos y condiciones y la política de privacidad");
        terminosCheck.setBackground(Color.WHITE);
        gbc.gridy = 15;
        add(terminosCheck, gbc);
        
        // Botón Crear Cuenta
        crearCuentaButton = new JButton("Crear Cuenta");
        crearCuentaButton.setPreferredSize(new Dimension(280, 40));
        crearCuentaButton.setBackground(new Color(139, 92, 246));
        crearCuentaButton.setForeground(Color.WHITE);
        crearCuentaButton.setBorderPainted(false);
        crearCuentaButton.setFocusPainted(false);
        crearCuentaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        crearCuentaButton.addActionListener(e -> registrarUsuario(cardLayout, mainPanel));
        gbc.gridy = 16;
        add(crearCuentaButton, gbc);

        // Link para ir al login (Redirección a LoginPanel)
        JPanel loginLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginLinkPanel.setBackground(Color.WHITE);
        JLabel yaTienesLabel = new JLabel("¿Ya tienes una cuenta? ");
        
        JLabel loginLabel = new JLabel("Inicia sesión aquí");
        loginLabel.setForeground(new Color(220, 53, 69));
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "LoginPanel");
            }
        });

        loginLinkPanel.add(yaTienesLabel);
        loginLinkPanel.add(loginLabel);
        gbc.gridy = 17;
        add(loginLinkPanel, gbc);
        
        // Simulación de diseño inicial completa
    }

    private void registrarUsuario(CardLayout cardLayout, JPanel mainPanel) {
        String nombre = nombreField.getText().trim();
        String apellidos = apellidosField.getText().trim();
        String email = emailField.getText().trim();
        String telefono = telefonoField.getText().trim();
        String genero = (String) generoCombo.getSelectedItem();
        String password = new String(passwordField.getPassword());
        String confirmarPassword = new String(confirmarPasswordField.getPassword());

        // Validaciones de campos obligatorios
        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, completa todos los campos obligatorios", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validaciones de Contraseña
        if (!password.equals(confirmarPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Las contraseñas no coinciden", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "La contraseña debe tener al menos 6 caracteres", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validaciones de Checkboxes
        if (!mayorEdadCheck.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "Debes ser mayor de 18 años para registrarte", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!terminosCheck.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "Debes aceptar los términos y condiciones", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Manejo de campo de género vacío
        if (genero.equals("Selecciona tu género")) {
            genero = "";
        }

        // Registrar en la base de datos
        if (dbManager.registrarUsuario(nombre, apellidos, email, telefono, genero, password)) {
            JOptionPane.showMessageDialog(this, 
                "¡Registro exitoso! Ahora puedes iniciar sesión", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Limpiar campos y volver al login
            limpiarCampos();
            cardLayout.show(mainPanel, "LoginPanel");
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error al registrar usuario. El email ya puede estar en uso.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        nombreField.setText("");
        apellidosField.setText("");
        emailField.setText("");
        telefonoField.setText("");
        generoCombo.setSelectedIndex(0);
        passwordField.setText("");
        confirmarPasswordField.setText("");
        mayorEdadCheck.setSelected(false);
        terminosCheck.setSelected(false);
    }
}