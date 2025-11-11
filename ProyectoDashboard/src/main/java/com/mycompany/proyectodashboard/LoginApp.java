package com.mycompany.proyectodashboard;

import javax.swing.*;
import java.awt.*;

public class LoginApp extends JFrame {
    
    // VARIABLES DE INSTANCIA
    private DatabaseManager dbManager;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private RegistroPanel registroPanel;
    private RecuperarPanel recuperarPanel;
    private NuevaPasswordPanel nuevaPasswordPanel;
    private DashboardPanel dashboardPanel; 

    public LoginApp() {
        // 1. Inicializar base de datos
        dbManager = new DatabaseManager();

        // 2. Configurar ventana
        setTitle("Sistema de Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Configuramos un tamaño grande para el Dashboard
        setSize(1200, 800); 
        setLocationRelativeTo(null);
        setResizable(true); 

        // 3. Crear CardLayout y panel principal
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 4. Crear todos los paneles
        
        // A. Inicializar DashboardPanel primero (es una dependencia de LoginPanel)
        dashboardPanel = new DashboardPanel(cardLayout, mainPanel); 
        
        // B. Inicializar LoginPanel, pasándole el DashboardPanel (¡Corrección clave!)
        loginPanel = new LoginPanel(dbManager, cardLayout, mainPanel, dashboardPanel); 
        
        // C. Resto de Paneles
        registroPanel = new RegistroPanel(dbManager, cardLayout, mainPanel);
        recuperarPanel = new RecuperarPanel(dbManager, cardLayout, mainPanel);
        nuevaPasswordPanel = new NuevaPasswordPanel(dbManager, cardLayout, mainPanel);

        // 5. Añadir paneles al CardLayout
        mainPanel.add(loginPanel, "LoginPanel");
        mainPanel.add(registroPanel, "RegistroPanel");
        mainPanel.add(recuperarPanel, "RecuperarPanel");
        mainPanel.add(nuevaPasswordPanel, "NuevaPasswordPanel");
        mainPanel.add(dashboardPanel, "DashboardPanel"); 

        // 6. Añadir mainPanel a la ventana
        add(mainPanel);

        // 7. Mostrar el primer panel y ajustar tamaño para el login
        cardLayout.show(mainPanel, "LoginPanel");
        setSize(400, 700); 
        setLocationRelativeTo(null);

        // 8. Hacer visible la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginApp());
    }
}