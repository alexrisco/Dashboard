package com.mycompany.proyectodashboard;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class DashboardPanel extends JPanel {
    
    private JLabel welcomeLabel;
    private JButton logoutButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Usuario usuarioActual; 

    public DashboardPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(240, 240, 240)); 

        // A. HEADER (Barra de navegación)
        JPanel headerPanel = crearHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // B. CONTENIDO PRINCIPAL (Paneles 1, 2, 3, 4)
        JPanel contentPanel = crearContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }
    
    // --- MÉTODOS DE CONSTRUCCIÓN DE LA ESTRUCTURA ---

    private JPanel crearHeaderPanel() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        navBar.setPreferredSize(new Dimension(10, 40));

        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftNav.setBackground(Color.WHITE);
        leftNav.add(new JLabel("Archivo"));
        leftNav.add(new JLabel("Productos"));
        leftNav.add(new JLabel("Ventas"));
        leftNav.add(new JLabel("Recursos Humanos"));

        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        rightNav.setBackground(Color.WHITE);
        
        welcomeLabel = new JLabel("¡Hola, Usuario!"); 
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        logoutButton = new JButton("Salir");
        logoutButton.setBackground(new Color(220, 53, 69)); 
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> cerrarSesion());
        
        rightNav.add(welcomeLabel);
        rightNav.add(logoutButton);

        navBar.add(leftNav, BorderLayout.WEST);
        navBar.add(rightNav, BorderLayout.EAST);
        
        return navBar;
    }

    private JPanel crearContentPanel() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(15, 15, 15, 15));
        content.setBackground(new Color(240, 240, 240));

        JPanel panelInventario = crearPanelInventario(); // Panel 1
        JPanel panelLateral = crearPanelLateral();      // Contiene Paneles 2, 3 y 4

        JSplitPane splitH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelInventario, panelLateral);
        splitH.setResizeWeight(0.65);
        splitH.setDividerSize(10);
        splitH.setBorder(null);

        content.add(splitH, BorderLayout.CENTER);
        return content;
    }
    
    // Panel 1: Inventario
    private JPanel crearPanelInventario() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                                        "Panel 1: Inventario de Productos", TitledBorder.LEFT, TitledBorder.TOP, 
                                        new Font("Arial", Font.BOLD, 14), Color.DARK_GRAY));
        
        // Simulación de la barra de búsqueda
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchBar.add(new JLabel("Buscar:"));
        searchBar.add(new JTextField(30));
        searchBar.add(new JButton("Buscar"));
        panel.add(searchBar, BorderLayout.NORTH);

        // Tabla de Inventario
        String[] columnNames = {"SKU", "Producto", "Talla", "Color", "Stock", "Precio (90)", "Estado", "Acciones"};
        Object[][] data = {
            {"CA0001", "Básica Blanca", "M", "Blanco", 45, "19.99", "Disponible", "Ver / Editar"},
            {"CA0002", "Básica Negra", "L", "Negro", 38, "19.99", "Disponible", "Ver / Editar"},
            {"CA0007", "Polo Verde", "M", "Verde", 0, "32.99", "Agotado", "Ver / Editar"}
        };
        JTable table = new JTable(data, columnNames);
        table.getTableHeader().setBackground(new Color(240, 240, 255));
        
        JScrollPane scrollTable = new JScrollPane(table);

        // JSplitPane Vertical para la tabla y el Registro (Panel 3)
        JPanel panelRegistro = crearPanelRegistro();
        JSplitPane splitV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTable, panelRegistro);
        splitV.setResizeWeight(0.5); // 50% para cada uno
        splitV.setDividerSize(10);
        splitV.setBorder(null);
        
        panel.add(splitV, BorderLayout.CENTER);
        
        return panel;
    }

    // Área derecha (Paneles 2 y 4)
    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(240, 240, 240));

        JPanel panelEstadisticas = crearPanelEstadisticas(); // Panel 2
        JPanel panelConfiguracion = crearPanelConfiguracion(); // Panel 4
        
        JSplitPane splitV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelEstadisticas, panelConfiguracion);
        splitV.setResizeWeight(0.5); 
        splitV.setDividerSize(10);
        splitV.setBorder(null);

        panel.add(splitV, BorderLayout.CENTER);
        return panel;
    }
    
    // Panel 2: Estadísticas y Gráficas
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                                        "Panel 2: Estadísticas y Gráficas", TitledBorder.LEFT, TitledBorder.TOP, 
                                        new Font("Arial", Font.BOLD, 14), Color.DARK_GRAY));
        panel.setBackground(Color.WHITE);
        
        // Tarjetas de Métricas (GridLayout 1x3)
        JPanel metricsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        metricsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        metricsPanel.setBackground(Color.WHITE);
        
        metricsPanel.add(crearTarjetaStat("Total Ventas", "1,216", new Color(230, 242, 255), "0.5% vs mes anterior")); 
        metricsPanel.add(crearTarjetaStat("Ingresos", "€24,310", new Color(230, 255, 230), "4.2% vs mes anterior"));
        metricsPanel.add(crearTarjetaStat("Productos en Stock", "183", new Color(255, 240, 230), "Ver stock bajo"));
        
        panel.add(metricsPanel, BorderLayout.NORTH);

        // Área de Gráficos (GridLayout 1x2)
        JPanel graphArea = new JPanel(new GridLayout(1, 2, 10, 10));
        graphArea.setBorder(new EmptyBorder(0, 10, 10, 10));
        graphArea.add(crearSimulacionGrafico("Evolución de Ventas"));
        graphArea.add(crearSimulacionGrafico("Ventas por Talla"));
        
        panel.add(graphArea, BorderLayout.CENTER);

        return panel;
    }
    
    // Panel 3: Estructura de Registro/Detalles del Producto (Parte del Split Vertical del Panel 1)
    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder("Panel 3: Registro de Productos"));
        panel.setBackground(Color.WHITE);

        // Usamos EmptyBorder solo para espacio interior
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 5));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        form.setBackground(Color.WHITE);
        
        // Campos de información
        form.add(new JLabel("Nombre del Producto:"));
        form.add(new JTextField("Camiseta Básica Azul"));
        form.add(new JLabel("Código SKU:"));
        form.add(new JTextField("CA0001"));
        form.add(new JLabel("Precio (90):"));
        form.add(new JTextField("19.99"));
        form.add(new JLabel("Cantidad en Stock:"));
        form.add(new JTextField("50"));
        form.add(new JLabel("Categoría:"));
        form.add(new JComboBox<>(new String[]{"Selecciona categoría", "Carretas", "Accesorios"}));

        // Características y Opciones (al sur del Border Layout principal)
        JPanel southPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        southPanel.setBackground(Color.WHITE);
        
        JTextArea caracteristicas = new JTextArea("Material: 100% Algodón\nGénero: Hombre/Mujer/Unisex");
        caracteristicas.setBorder(new TitledBorder("Características"));
        JScrollPane scrollCaract = new JScrollPane(caracteristicas);
        
        JPanel opciones = new JPanel(new GridLayout(2, 1));
        opciones.setBorder(new TitledBorder("Opciones Adicionales"));
        opciones.add(new JCheckBox("Incluir envío gratis"));
        opciones.add(new JCheckBox("Marcar como producto destacado"));
        
        southPanel.add(scrollCaract);
        southPanel.add(opciones);
        
        panel.add(form, BorderLayout.NORTH);
        panel.add(southPanel, BorderLayout.CENTER);
        
        return panel;
    }

    // Panel 4: Configuración del Sistema
    private JPanel crearPanelConfiguracion() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                                        "Panel 4: Configuración del Sistema", TitledBorder.LEFT, TitledBorder.TOP, 
                                        new Font("Arial", Font.BOLD, 14), Color.DARK_GRAY));
        panel.setBackground(Color.WHITE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.setBackground(Color.WHITE);

        // Opciones de Envío
        content.add(new JLabel("Opciones de Envío:"));
        content.add(new JRadioButton("Envío Estándar (3-5 días) - €5.99"));
        content.add(new JRadioButton("Envío Express (24-48h) - €8.99"));
        content.add(new JRadioButton("Envío gratis (pedidos > €50)"));

        // Notificaciones y Alertas
        content.add(Box.createVerticalStrut(15));
        content.add(new JLabel("Notificaciones y Alertas:"));
        content.add(new JCheckBox("Alertas de Stock: Recibir notificaciones cuando el stock está bajo"));
        content.add(new JCheckBox("Alertas de Ventas: Notificar cada nueva venta realizada"));

        // Métodos de Pago
        content.add(Box.createVerticalStrut(15));
        content.add(new JLabel("Métodos de Pago Aceptados:"));
        content.add(new JCheckBox("Tarjeta de Crédito/Débito"));
        content.add(new JCheckBox("PayPal"));
        
        panel.add(new JScrollPane(content), BorderLayout.CENTER);
        return panel;
    }
    
    // --- MÉTODOS AUXILIARES DE DISEÑO ---
    
    private JPanel crearTarjetaStat(String titulo, String valor, Color color, String subtitulo) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(color);
        tarjeta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JLabel title = new JLabel(titulo);
        title.setFont(new Font("Arial", Font.PLAIN, 12));
        title.setBorder(new EmptyBorder(5, 5, 0, 5));
        
        JLabel value = new JLabel(valor);
        value.setFont(new Font("Arial", Font.BOLD, 24));
        
        JLabel subtitle = new JLabel(subtitulo);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 10));
        subtitle.setForeground(Color.DARK_GRAY);
        subtitle.setBorder(new EmptyBorder(0, 5, 5, 5));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(color);
        centerPanel.add(value, BorderLayout.NORTH);
        centerPanel.add(subtitle, BorderLayout.SOUTH);
        
        tarjeta.add(title, BorderLayout.NORTH);
        tarjeta.add(centerPanel, BorderLayout.CENTER);
        
        return tarjeta;
    }

    private JPanel crearSimulacionGrafico(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(titulo));
        panel.setBackground(new Color(250, 250, 250));
        panel.add(new JLabel("<< Simulando Gráfico >>", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    // --- MÉTODOS DE SESIÓN ---
    
    public void cargarDatosUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        if (usuario != null) {
            welcomeLabel.setText("¡Hola, " + usuario.getNombre() + "! (" + usuario.getEmail() + ")");
        }
    }
    
    private void cerrarSesion() {
        this.usuarioActual = null;
        cardLayout.show(mainPanel, "LoginPanel");
        welcomeLabel.setText("¡Hola, Usuario!");
        
        JOptionPane.showMessageDialog(this, 
                "Has cerrado sesión exitosamente.", 
                "Sesión Terminada", 
                JOptionPane.INFORMATION_MESSAGE);
    }
}