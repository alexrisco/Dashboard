package com.mycompany.proyectodashboard;

import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.FlowLayout;

public class DashboardPanel extends JPanel {
    
    private CardLayout cardLayout; 
    private JPanel mainPanel; 
    private DatabaseManager dbManager; 
    private Usuario usuarioActual; 
    
    // Componentes del Header
    private JLabel lblUsuario; 
    private JButton btnCerrarSesion = new JButton("Cerrar Sesión");

    // Componentes del Panel 3 (Registro de Productos)
    private JTextField nombreField = new JTextField(); 
    private JTextField skuField = new JTextField();
    private JTextField precioField = new JTextField();
    private JTextField stockField = new JTextField();
    private JComboBox<String> categoriaCombo = new JComboBox<>(); 
    private JTextField materialField = new JTextField();
    private JComboBox<String> tallaCombo = new JComboBox<>(); 
    private JTextField colorField = new JTextField();
    private JComboBox<String> generoCombo = new JComboBox<>(); 
    private JCheckBox envioGratisCheck = new JCheckBox("Incluir envío gratis"); 
    private JCheckBox destacadoCheck = new JCheckBox("Marcar como producto destacado"); 
    private JButton btnGuardarProducto = new JButton("Guardar Producto / Añadir Nuevo"); 

    // Tabla de Inventario (Panel 1)
    private JTable tablaProductos; 
    private DefaultTableModel modeloTabla;

    public DashboardPanel(CardLayout layout, JPanel panel, DatabaseManager dbManager) {
        this.cardLayout = layout;
        this.mainPanel = panel;
        this.dbManager = dbManager; 
        
        setLayout(new BorderLayout(10, 10));
        
        // 1. Inicialización de la UI
        inicializarHeader();
        inicializarComponentesRegistro();
        
        JTabbedPane panelInferiorLateral = crearPanelOpcionesYRegistro();
        
        JPanel centro = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Panel Izquierdo: Inventario + Registro
        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 10));
        panelIzquierdo.add(inicializarPanelInventario(), BorderLayout.NORTH); 
        panelIzquierdo.add(panelInferiorLateral, BorderLayout.CENTER);
        
        centro.add(panelIzquierdo); 
        
        // Panel Derecho: Gráficas + Configuración
        centro.add(crearPanelDerechoPlaceholder()); 

        add(centro, BorderLayout.CENTER);
        
        // 2. Conexiones de acción
        btnGuardarProducto.addActionListener(e -> guardarProductoAction()); 
        btnCerrarSesion.addActionListener(e -> cerrarSesionAction());
        
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }
    
    // --- MÉTODOS DE INICIALIZACIÓN DE UI ---

    private void inicializarComponentesRegistro() {
        categoriaCombo.addItem("Selecciona categoría");
        categoriaCombo.addItem("Camisetas");
        categoriaCombo.addItem("Accesorios");
        categoriaCombo.addItem("Pantalones");
        
        tallaCombo.addItem("S");
        tallaCombo.addItem("M");
        tallaCombo.addItem("L");
        tallaCombo.addItem("XL");
        
        generoCombo.addItem("Hombre");
        generoCombo.addItem("Mujer");
        generoCombo.addItem("Unisex");
        
        precioField.setText("0.00");
        stockField.setText("0");
    }
    
    private void inicializarHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Sistema de Gestión de Inventario", SwingConstants.LEFT);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(new EmptyBorder(0, 0, 0, 50));
        
        lblUsuario = new JLabel("¡Hola, usuario! | Email: - | Teléfono: -"); 
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 16)); 
        
        JPanel userArea = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userArea.add(lblUsuario); 
        userArea.add(btnCerrarSesion); 
        
        headerPanel.add(titulo, BorderLayout.WEST);
        headerPanel.add(userArea, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private JPanel inicializarPanelInventario() {
        JPanel panelInventario = new JPanel(new BorderLayout());
        panelInventario.setBorder(BorderFactory.createTitledBorder("Panel 1: Inventario de Productos")); 
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Buscar:"));
        searchPanel.add(new JTextField(20));
        searchPanel.add(new JButton("Buscar"));
        panelInventario.add(searchPanel, BorderLayout.NORTH);
        
        String[] columnas = {"SKU", "Producto", "Talla", "Color", "Stock", "Precio (RD$)", "Estado", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaProductos = new JTable(modeloTabla);
        
        panelInventario.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        panelInventario.setPreferredSize(new java.awt.Dimension(500, 250));
        return panelInventario;
    }
    
    private JTabbedPane crearPanelOpcionesYRegistro() {
        JTabbedPane tabbedPane = new JTabbedPane(); 
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        
        tabbedPane.addTab("Panel 3: Registro de Productos", crearPanelRegistro()); 
        tabbedPane.addTab("Panel 4: Configuración del Sistema", crearPanelConfiguracionPlaceholder()); 
        
        return tabbedPane;
    }
    
    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new BorderLayout(5, 5)); 
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 5));
        
        form.add(new JLabel("Nombre del Producto:"));
        form.add(nombreField);
        
        form.add(new JLabel("Código SKU:"));
        form.add(skuField);
        
        form.add(new JLabel("Cantidad en Stock:"));
        form.add(stockField);
        
        form.add(new JLabel("Precio (RD$):"));
        form.add(precioField);
        
        form.add(new JLabel("Categoría:"));
        form.add(categoriaCombo);
        
        form.add(new JLabel("Material:"));
        form.add(materialField);
        
        form.add(new JLabel("Talla:"));
        form.add(tallaCombo);
        
        form.add(new JLabel("Color:"));
        form.add(colorField);
        
        form.add(new JLabel("Género:"));
        form.add(generoCombo);
        
        JPanel opcionesAdicionales = new JPanel(new GridLayout(2, 1));
        opcionesAdicionales.setBorder(BorderFactory.createTitledBorder("Opciones Adicionales"));
        opcionesAdicionales.add(envioGratisCheck); 
        opcionesAdicionales.add(destacadoCheck); 
        
        panel.add(form, BorderLayout.NORTH);
        panel.add(opcionesAdicionales, BorderLayout.CENTER);
        panel.add(btnGuardarProducto, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelConfiguracionPlaceholder() {
        // Contenido de la pestaña de Configuración
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Opciones de Envío:"));
        panel.add(new JRadioButton("Envío Estándar (3-5 días)"));
        panel.add(new JRadioButton("Envío Express (24-48h)"));
        panel.add(new JRadioButton("Envío gratis (pedidos > €50)"));
        
        panel.add(new JLabel("Notificaciones y Alertas:"));
        panel.add(new JCheckBox("Alertas de Stock: Recibir notificaciones cuando el stock esté bajo"));
        panel.add(new JCheckBox("Alertas de Ventas: Notificar cada nueva venta realizada"));
        
        panel.add(new JLabel("Métodos de Pago Aceptados:"));
        panel.add(new JCheckBox("Tarjeta de Crédito/Débito"));
        panel.add(new JCheckBox("PayPal"));
        
        return panel;
    }
    
    private JPanel crearPanelDerechoPlaceholder() {
        JPanel panelDerecho = new JPanel(new GridLayout(2, 1, 10, 10)); 
        
        JPanel panelEstadisticas = new JPanel(new BorderLayout());
        panelEstadisticas.setBorder(BorderFactory.createTitledBorder("Panel 2: Estadísticas y Gráficas")); 
        panelEstadisticas.add(new JLabel("<< Simulación de Gráficas de Ventas >>", SwingConstants.CENTER), BorderLayout.CENTER);
        
        JPanel panelConfiguracion = new JPanel(new BorderLayout());
        panelConfiguracion.setBorder(BorderFactory.createTitledBorder("Panel 4: Configuración del Sistema"));
        panelConfiguracion.add(new JLabel("<< Gráficos y Opciones de Configuración >>", SwingConstants.CENTER), BorderLayout.CENTER);
        
        panelDerecho.add(panelEstadisticas);
        panelDerecho.add(panelConfiguracion);
        
        return panelDerecho;
    }


    // --- LÓGICA DE DATOS Y ACCIONES ---

    public void cargarDatosUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        if (usuario != null) {
            lblUsuario.setText("¡Hola, " + usuario.getNombre() + "! | Email: " + usuario.getEmail() + " | Teléfono: " + usuario.getTelefono()); 
        }
        actualizarTablaInventario();
    }
    
    public void actualizarTablaInventario() {
        List<Producto> productos = dbManager.obtenerTodosProductos(); 

        modeloTabla.setRowCount(0);

        for (Producto p : productos) {
            String estado = (p.getStock() > 0) ? "Disponible" : "Agotado";
            
            Object[] fila = new Object[]{
                p.getSku(),
                p.getNombre(),
                p.getTalla(),
                p.getColor(),
                p.getStock(),
                p.getPrecio(),
                estado,
                "Ver / Editar" 
            };
            modeloTabla.addRow(fila);
        }
    }

    private void guardarProductoAction() {
        try {
            // 1. Recoger datos y validar (maneja comas y puntos en el precio)
            String nombre = nombreField.getText().trim();
            String sku = skuField.getText().trim();
            double precio = Double.parseDouble(precioField.getText().trim().replace(",", ".")); 
            int stock = Integer.parseInt(stockField.getText().trim());
            String categoria = (String) categoriaCombo.getSelectedItem();
            String material = materialField.getText().trim();
            String talla = (String) tallaCombo.getSelectedItem();
            String color = colorField.getText().trim();
            String genero = (String) generoCombo.getSelectedItem();
            boolean envioGratis = envioGratisCheck.isSelected();
            boolean destacado = destacadoCheck.isSelected();
            
            if (nombre.isEmpty() || sku.isEmpty() || categoria.contains("Selecciona")) {
                JOptionPane.showMessageDialog(this, "Por favor, complete los campos obligatorios (Nombre, SKU, Categoría).", "Error de Validación", JOptionPane.ERROR_MESSAGE); 
                return;
            }

            // 2. Crear Producto y Guardar
            Producto nuevoProducto = new Producto(
                nombre, sku, precio, stock, categoria, material, talla, color, genero, envioGratis, destacado
            );

            if (dbManager.guardarProducto(nuevoProducto)) { 
                JOptionPane.showMessageDialog(this, "Producto '" + nombre + "' guardado/actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTablaInventario(); 
                limpiarCamposRegistro(); 
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el producto. Verifique los datos o la conexión a la BD.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE); 
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error de formato: Asegúrese de que Precio y Stock sean números válidos.", "Error", JOptionPane.ERROR_MESSAGE); 
        }
    }
    
    private void limpiarCamposRegistro() {
        nombreField.setText("");
        skuField.setText("");
        precioField.setText("0.00");
        stockField.setText("0");
        categoriaCombo.setSelectedIndex(0); 
        materialField.setText("");
        tallaCombo.setSelectedIndex(0);
        colorField.setText("");
        generoCombo.setSelectedIndex(0);
        envioGratisCheck.setSelected(false);
        destacadoCheck.setSelected(false);
    }
    
    private void cerrarSesionAction() {
        usuarioActual = null;
        cardLayout.show(mainPanel, "LoginPanel");
    }
}