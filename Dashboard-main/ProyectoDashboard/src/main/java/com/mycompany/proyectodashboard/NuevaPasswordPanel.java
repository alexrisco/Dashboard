package com.mycompany.proyectodashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel para establecer nueva contraseña con diseño moderno
 */
public class NuevaPasswordPanel extends JPanel {
    private JTextField codigoField;
    private JPasswordField nuevaPasswordField;
    private JPasswordField confirmarPasswordField;
    private JLabel emailDisplayLabel;
    private DatabaseManager dbManager;
    private String emailRecuperacion;

    private static final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private static final Color PRIMARY_HOVER = new Color(79, 70, 229);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color INFO_COLOR = new Color(59, 130, 246);
    private static final Color BACKGROUND = new Color(249, 250, 251);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);

    public NuevaPasswordPanel(DatabaseManager dbManager, CardLayout cardLayout, JPanel mainPanel) {
        this.dbManager = dbManager;
        
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(BACKGROUND);
        
        JPanel cardPanel = createNuevaPasswordCard(cardLayout, mainPanel);
        centerWrapper.add(cardPanel);
        
        add(centerWrapper, BorderLayout.CENTER);
    }

    private JPanel createNuevaPasswordCard(CardLayout cardLayout, JPanel mainPanel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(40, 45, 40, 45)
        ));
        card.setPreferredSize(new Dimension(440, 700));

        // Icono
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(34, 197, 94, 30),
                    getWidth(), getHeight(), new Color(99, 102, 241, 30)
                );
                g2d.setPaint(gradient);
                g2d.fillOval(15, 5, 70, 70);
            }
        };
        iconPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setBackground(CARD_BG);
        iconPanel.setMaximumSize(new Dimension(440, 90));
        
        JLabel iconLabel = new JLabel("✉️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        iconPanel.add(iconLabel);
        
        card.add(iconPanel);
        card.add(Box.createVerticalStrut(15));

        // Título
        JLabel titleLabel = new JLabel("Nueva Contraseña");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(25));

        // Panel de mensaje de código enviado
        JPanel mensajePanel = new JPanel();
        mensajePanel.setLayout(new BoxLayout(mensajePanel, BoxLayout.Y_AXIS));
        mensajePanel.setBackground(new Color(220, 252, 231));
        mensajePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(134, 239, 172), 2, true),
            new EmptyBorder(14, 16, 14, 16)
        ));
        mensajePanel.setMaximumSize(new Dimension(350, 75));

        JLabel mensajeLabel = new JLabel("✓ Código enviado a:");
        mensajeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mensajeLabel.setForeground(new Color(21, 128, 61));
        mensajeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        emailDisplayLabel = new JLabel("tu@email.com");
        emailDisplayLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailDisplayLabel.setForeground(new Color(21, 128, 61));
        emailDisplayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mensajePanel.add(mensajeLabel);
        mensajePanel.add(Box.createVerticalStrut(5));
        mensajePanel.add(emailDisplayLabel);
        
        card.add(mensajePanel);
        card.add(Box.createVerticalStrut(28));

        // Código de verificación
        card.add(createFieldLabel("Código de verificación"));
        card.add(Box.createVerticalStrut(8));
        codigoField = createStyledTextField("000000");
        card.add(codigoField);
        card.add(Box.createVerticalStrut(18));

        // Nueva contraseña
        card.add(createFieldLabel("Nueva contraseña"));
        card.add(Box.createVerticalStrut(8));
        nuevaPasswordField = createStyledPasswordField();
        card.add(nuevaPasswordField);
        card.add(Box.createVerticalStrut(18));

        // Confirmar contraseña
        card.add(createFieldLabel("Confirmar nueva contraseña"));
        card.add(Box.createVerticalStrut(8));
        confirmarPasswordField = createStyledPasswordField();
        card.add(confirmarPasswordField);
        card.add(Box.createVerticalStrut(28));

        // Botón Cambiar Contraseña
        JButton cambiarButton = createPrimaryButton("Cambiar Contraseña");
        cambiarButton.addActionListener(e -> cambiarPassword(cardLayout, mainPanel));
        card.add(cambiarButton);
        card.add(Box.createVerticalStrut(25));

        // Separador
        card.add(createSeparator());
        card.add(Box.createVerticalStrut(25));

        // Link cambiar email
        JLabel cambiarEmailLabel = new JLabel("← Usar otro email");
        cambiarEmailLabel.setForeground(INFO_COLOR);
        cambiarEmailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cambiarEmailLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cambiarEmailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cambiarEmailLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                limpiarCampos();
                cardLayout.show(mainPanel, "RecuperarPanel");
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cambiarEmailLabel.setText("<html><u>← Usar otro email</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cambiarEmailLabel.setText("← Usar otro email");
            }
        });
        card.add(cambiarEmailLabel);

        return card;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(350, 22));
        return label;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(350, 44));
        field.setMaximumSize(new Dimension(350, 44));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true),
                    BorderFactory.createEmptyBorder(7, 13, 7, 13)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                    BorderFactory.createEmptyBorder(8, 14, 8, 14)
                ));
            }
        });
        
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(350, 44));
        field.setMaximumSize(new Dimension(350, 44));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true),
                    BorderFactory.createEmptyBorder(7, 13, 7, 13)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                    BorderFactory.createEmptyBorder(8, 14, 8, 14)
                ));
            }
        });
        
        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(350, 48));
        button.setMaximumSize(new Dimension(350, 48));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }

    private JPanel createSeparator() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setMaximumSize(new Dimension(350, 1));
        panel.setPreferredSize(new Dimension(350, 1));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        return panel;
    }

    public void setEmail(String email) {
        this.emailRecuperacion = email;
        emailDisplayLabel.setText(email);
    }

    private void cambiarPassword(CardLayout cardLayout, JPanel mainPanel) {
        String codigo = codigoField.getText().trim();
        String nuevaPassword = new String(nuevaPasswordField.getPassword());
        String confirmarPassword = new String(confirmarPasswordField.getPassword());

        if (emailRecuperacion == null || emailRecuperacion.isEmpty()) {
            mostrarError("Error: No se ha iniciado el proceso de recuperación.\nPor favor, vuelve al paso anterior.");
            cardLayout.show(mainPanel, "RecuperarPanel");
            return;
        }

        if (codigo.isEmpty() || nuevaPassword.isEmpty() || confirmarPassword.isEmpty()) {
            mostrarError("Por favor, completa todos los campos");
            return;
        }

        if (!nuevaPassword.equals(confirmarPassword)) {
            mostrarError("Las contraseñas no coinciden");
            return;
        }

        if (nuevaPassword.length() < 6) {
            mostrarError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!dbManager.verificarCodigo(emailRecuperacion, codigo)) {
            mostrarError("Código de verificación incorrecto o ya usado");
            return;
        }

        if (dbManager.cambiarPassword(emailRecuperacion, nuevaPassword)) {
            JOptionPane.showMessageDialog(this, 
                "¡Contraseña cambiada exitosamente!\n\n" +
                "Ya puedes iniciar sesión con tu nueva contraseña", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            limpiarCampos();
            cardLayout.show(mainPanel, "LoginPanel");
        } else {
            mostrarError("Error al cambiar la contraseña.\nPor favor, inténtalo de nuevo.");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void limpiarCampos() {
        codigoField.setText("");
        nuevaPasswordField.setText("");
        confirmarPasswordField.setText("");
        emailRecuperacion = null;
        emailDisplayLabel.setText("tu@email.com");
    }
}