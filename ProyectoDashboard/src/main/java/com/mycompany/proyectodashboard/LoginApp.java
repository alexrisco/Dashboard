package com.mycompany.proyectodashboard;

import javax.swing.*;
import java.awt.*;

public class LoginApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DatabaseManager dbManager;

    public LoginApp() {
        super("Sistema de Gestión y Login");
        
        // 1. Inicializar el Database Manager
        dbManager = new DatabaseManager();

        // Configuración de la ventana principal
        setSize(1200, 800); 
        setLocationRelativeTo(null);
        setResizable(true);

        // 2. Crear CardLayout y panel principal
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 3. Crear todos los paneles
        // NOTA IMPORTANTE: DashboardPanel debe inicializarse primero para pasárselo a LoginPanel.
        DashboardPanel dashboardPanel = new DashboardPanel(cardLayout, mainPanel, dbManager);

        // Se asume la existencia de LoginPanel, RegistroPanel, RecuperarPanel y NuevaPasswordPanel:
        LoginPanel loginPanel = new LoginPanel(dbManager, cardLayout, mainPanel, dashboardPanel);
        RegistroPanel registroPanel = new RegistroPanel(dbManager, cardLayout, mainPanel);
        RecuperarPanel recuperarPanel = new RecuperarPanel(dbManager, cardLayout, mainPanel);
        NuevaPasswordPanel nuevaPasswordPanel = new NuevaPasswordPanel(dbManager, cardLayout, mainPanel);

        // 4. Añadir paneles al panel principal
        mainPanel.add(loginPanel, "LoginPanel");
        mainPanel.add(registroPanel, "RegistroPanel");
        mainPanel.add(recuperarPanel, "RecuperarPanel");
        mainPanel.add(nuevaPasswordPanel, "NuevaPasswordPanel");
        mainPanel.add(dashboardPanel, "DashboardPanel");

        add(mainPanel);
        
        // Mostrar el LoginPanel al inicio
        cardLayout.show(mainPanel, "LoginPanel"); 

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginApp());
    }
}