package com.mycompany.proyectodashboard;

import javax.swing.*;
import java.awt.*;

public class RecuperarPanel extends JPanel {
    private JTextField emailField;
    private JButton enviarButton;
    private DatabaseManager dbManager;

    public RecuperarPanel(DatabaseManager dbManager, CardLayout cardLayout, JPanel mainPanel) {
        this.dbManager = dbManager;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ... [CÓDIGO DE DISEÑO OMITIDO POR BREVEDAD, ES IDÉNTICO AL QUE PASASTE] ...
        
        // Título
        JLabel titleLabel = new JLabel("Recuperar Contraseña", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("<html><center>Ingresa tu email para recibir instrucciones de recuperación</center></html>", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        gbc.gridy = 2;
        add(subtitleLabel, gbc);

        // Email
        JLabel emailLabel = new JLabel("Correo electrónico");
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 20, 5, 20);
        add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(280, 35));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 20, 10, 20);
        add(emailField, gbc);

        // Botón Enviar Instrucciones
        enviarButton = new JButton("Enviar Instrucciones");
        enviarButton.setPreferredSize(new Dimension(280, 40));
        enviarButton.setBackground(new Color(139, 92, 246));
        enviarButton.setForeground(Color.WHITE);
        enviarButton.setBorderPainted(false);
        enviarButton.setFocusPainted(false);
        enviarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        enviarButton.addActionListener(e -> enviarInstrucciones(cardLayout, mainPanel));
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 20, 10, 20);
        add(enviarButton, gbc);

        // Link volver al login (Redirección a LoginPanel)
        JPanel volverPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        volverPanel.setBackground(Color.WHITE);
        JLabel volverLabel = new JLabel("← Volver al login");
        volverLabel.setForeground(new Color(59, 130, 246));
        volverLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        volverLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "LoginPanel");
            }
        });
        volverPanel.add(volverLabel);
        gbc.gridy = 6;
        add(volverPanel, gbc);
        
        // Simulación de diseño inicial completa

    }

    private void enviarInstrucciones(CardLayout cardLayout, JPanel mainPanel) {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingresa tu correo electrónico", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!dbManager.emailExiste(email)) {
            JOptionPane.showMessageDialog(this, 
                "No existe una cuenta con ese correo electrónico", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generar código de verificación
        String codigo = dbManager.generarCodigoVerificacion(email);

        if (codigo != null) {
            JOptionPane.showMessageDialog(this, 
                "Código de verificación generado: " + codigo + "\n\n" +
                "Usa este código en la siguiente pantalla.", 
                "Código generado (Demo)", 
                JOptionPane.INFORMATION_MESSAGE);

            // Obtener la referencia al NuevaPasswordPanel para inyectar el email
            JPanel mainPanelRef = (JPanel) this.getParent();
            for (Component comp : mainPanelRef.getComponents()) {
                if (comp instanceof NuevaPasswordPanel) {
                    ((NuevaPasswordPanel) comp).setEmail(email);
                    break;
                }
            }

            emailField.setText("");
            cardLayout.show(mainPanel, "NuevaPasswordPanel");
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error al generar el código de verificación", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}