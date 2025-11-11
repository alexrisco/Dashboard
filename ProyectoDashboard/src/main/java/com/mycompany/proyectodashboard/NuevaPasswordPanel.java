package com.mycompany.proyectodashboard;

import javax.swing.*;
import java.awt.*;

public class NuevaPasswordPanel extends JPanel {
    private JTextField codigoField;
    private JPasswordField nuevaPasswordField;
    private JPasswordField confirmarPasswordField;
    private JLabel emailLabel;
    private DatabaseManager dbManager;
    private String emailRecuperacion; // Variable de estado

    public NuevaPasswordPanel(DatabaseManager dbManager, CardLayout cardLayout, JPanel mainPanel) {
        this.dbManager = dbManager;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ... [CÓDIGO DE DISEÑO OMITIDO POR BREVEDAD, ES IDÉNTICO AL QUE PASASTE] ...

        // Título
        JLabel titleLabel = new JLabel("Nueva Contraseña", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Panel de mensaje de código enviado
        JPanel mensajePanel = new JPanel();
        mensajePanel.setLayout(new BoxLayout(mensajePanel, BoxLayout.Y_AXIS));
        mensajePanel.setBackground(new Color(220, 252, 231));
        mensajePanel.setBorder(BorderFactory.createLineBorder(new Color(134, 239, 172), 2));
        mensajePanel.setPreferredSize(new Dimension(280, 50));

        JLabel mensajeLabel = new JLabel("✓ Se ha enviado un código de verificación a");
        emailLabel = new JLabel("tu@email.com"); // Este label se actualizará con setEmail()

        mensajePanel.add(Box.createVerticalGlue());
        mensajePanel.add(mensajeLabel);
        mensajePanel.add(emailLabel);
        mensajePanel.add(Box.createVerticalGlue());
        gbc.gridy = 3;
        add(mensajePanel, gbc);

        // Código de verificación
        JLabel codigoLabel = new JLabel("Código de verificación");
        gbc.gridy = 5;
        add(codigoLabel, gbc);
        codigoField = new JTextField();
        codigoField.setPreferredSize(new Dimension(280, 35));
        gbc.gridy = 6;
        add(codigoField, gbc);

        // Nueva contraseña
        JLabel nuevaPasswordLabel = new JLabel("Nueva contraseña");
        gbc.gridy = 7;
        add(nuevaPasswordLabel, gbc);
        nuevaPasswordField = new JPasswordField();
        nuevaPasswordField.setPreferredSize(new Dimension(280, 35));
        gbc.gridy = 8;
        add(nuevaPasswordField, gbc);

        // Confirmar contraseña
        JLabel confirmarLabel = new JLabel("Confirmar nueva contraseña");
        gbc.gridy = 9;
        add(confirmarLabel, gbc);
        confirmarPasswordField = new JPasswordField();
        confirmarPasswordField.setPreferredSize(new Dimension(280, 35));
        gbc.gridy = 10;
        add(confirmarPasswordField, gbc);

        // Botón Cambiar Contraseña
        JButton cambiarButton = new JButton("Cambiar Contraseña");
        cambiarButton.setPreferredSize(new Dimension(280, 40));
        cambiarButton.setBackground(new Color(139, 92, 246));
        cambiarButton.setForeground(Color.WHITE);
        cambiarButton.setBorderPainted(false);
        cambiarButton.setFocusPainted(false);
        cambiarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cambiarButton.addActionListener(e -> cambiarPassword(cardLayout, mainPanel));
        gbc.gridy = 11;
        add(cambiarButton, gbc);

        // Link cambiar email (Redirección a RecuperarPanel)
        JPanel cambiarEmailPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cambiarEmailPanel.setBackground(Color.WHITE);
        JLabel cambiarEmailLabel = new JLabel("← Cambiar email");
        cambiarEmailLabel.setForeground(new Color(59, 130, 246));
        cambiarEmailLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cambiarEmailLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "RecuperarPanel");
            }
        });
        cambiarEmailPanel.add(cambiarEmailLabel);
        gbc.gridy = 12;
        add(cambiarEmailPanel, gbc);

        // Simulación de diseño inicial completa
    }

    /**
     * Settea el email desde RecuperarPanel para saber a qué usuario cambiar la contraseña.
     */
    public void setEmail(String email) {
        this.emailRecuperacion = email;
        emailLabel.setText(email);
    }

    private void cambiarPassword(CardLayout cardLayout, JPanel mainPanel) {
        String codigo = codigoField.getText().trim();
        String nuevaPassword = new String(nuevaPasswordField.getPassword());
        String confirmarPassword = new String(confirmarPasswordField.getPassword());

        // Validaciones
        if (emailRecuperacion == null || emailRecuperacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Error: No se ha iniciado el proceso de recuperación.", 
                "Error de Flujo", 
                JOptionPane.ERROR_MESSAGE);
            cardLayout.show(mainPanel, "RecuperarPanel");
            return;
        }

        if (codigo.isEmpty() || nuevaPassword.isEmpty() || confirmarPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, completa todos los campos", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!nuevaPassword.equals(confirmarPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Las contraseñas no coinciden", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nuevaPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "La contraseña debe tener al menos 6 caracteres", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Verificar el código en la base de datos
        if (!dbManager.verificarCodigo(emailRecuperacion, codigo)) {
            JOptionPane.showMessageDialog(this, 
                "Código de verificación incorrecto o expirado", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Cambiar la contraseña
        if (dbManager.cambiarPassword(emailRecuperacion, nuevaPassword)) {
            JOptionPane.showMessageDialog(this, 
                "¡Contraseña cambiada exitosamente!\nYa puedes iniciar sesión con tu nueva contraseña", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Limpiar campos y volver al login
            codigoField.setText("");
            nuevaPasswordField.setText("");
            confirmarPasswordField.setText("");
            
            cardLayout.show(mainPanel, "LoginPanel");
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error al cambiar la contraseña", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}