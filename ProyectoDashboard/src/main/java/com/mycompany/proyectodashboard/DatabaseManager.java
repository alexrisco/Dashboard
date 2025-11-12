package com.mycompany.proyectodashboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    
    private static final String URL = "jdbc:sqlite:proyecto_dashboard.db";
    
    public DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            crearTablas(); 
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró el driver SQLite JDBC.");
            e.printStackTrace();
        }
    }

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public void crearTablas() {
        // 1. Tabla de Usuarios (Incluye 'genero')
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (\n"
                           + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                           + "    nombre TEXT NOT NULL,\n"
                           + "    apellidos TEXT,\n"
                           + "    email TEXT UNIQUE NOT NULL,\n"
                           + "    telefono TEXT,\n"
                           + "    genero TEXT,\n"
                           + "    password TEXT NOT NULL\n"
                           + ");";
        
        // 2. Tabla de Productos
        String sqlProductos = "CREATE TABLE IF NOT EXISTS productos (\n"
                            + "    sku TEXT PRIMARY KEY,\n"
                            + "    nombre TEXT NOT NULL,\n"
                            + "    precio REAL NOT NULL,\n"
                            + "    stock INTEGER NOT NULL,\n"
                            + "    categoria TEXT,\n"
                            + "    material TEXT,\n"
                            + "    talla TEXT,\n"
                            + "    color TEXT,\n"
                            + "    genero TEXT,\n"
                            + "    envioGratis INTEGER DEFAULT 0,\n" 
                            + "    destacado INTEGER DEFAULT 0\n"     
                            + ");";
        
        // 3. Tabla de Códigos de Verificación
        String sqlCodigos = "CREATE TABLE IF NOT EXISTS codigos_verificacion (\n"
                          + "    email TEXT PRIMARY KEY,\n"
                          + "    codigo TEXT NOT NULL,\n"
                          + "    expiracion INTEGER NOT NULL\n"
                          + ");";
        
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlProductos);
            stmt.execute(sqlCodigos);
        } catch (SQLException e) {
            System.err.println("Error al crear tablas: " + e.getMessage());
        }
    }

    // --- MÉTODOS DE USUARIO ---
    
    public boolean registrarUsuario(String nombre, String apellidos, String email, String telefono, String genero, String password) {
        String sql = "INSERT INTO usuarios (nombre, apellidos, email, telefono, genero, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellidos);
            pstmt.setString(3, email);
            pstmt.setString(4, telefono);
            pstmt.setString(5, genero);
            pstmt.setString(6, password); 
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
        
    public Usuario iniciarSesion(String email, String password) {
        // SELECT corregido para incluir 'genero'
        String sql = "SELECT id, nombre, apellidos, email, telefono, genero FROM usuarios WHERE email = ? AND password = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Se utiliza el constructor de Usuario con 6 argumentos
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getString("genero")
                ); 
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Error de inicio de sesión: " + e.getMessage());
            return null;
        }
    }
    
    // --- MÉTODOS DE PRODUCTOS ---

    public boolean guardarProducto(Producto p) {
        // Usa INSERT OR REPLACE INTO para guardar o actualizar por SKU.
        String sql = "INSERT OR REPLACE INTO productos ("
                   + "sku, nombre, precio, stock, categoria, material, talla, color, genero, envioGratis, destacado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getSku());
            pstmt.setString(2, p.getNombre());
            pstmt.setDouble(3, p.getPrecio());
            pstmt.setInt(4, p.getStock());
            pstmt.setString(5, p.getCategoria());
            pstmt.setString(6, p.getMaterial());
            pstmt.setString(7, p.getTalla());
            pstmt.setString(8, p.getColor());
            pstmt.setString(9, p.getGenero());
            // Convierte boolean a INTEGER (0 o 1)
            pstmt.setInt(10, p.isEnvioGratis() ? 1 : 0);
            pstmt.setInt(11, p.isDestacado() ? 1 : 0);
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar producto: " + e.getMessage());
            return false;
        }
    }
    
    public List<Producto> obtenerTodosProductos() {
        String sql = "SELECT * FROM productos";
        List<Producto> productos = new ArrayList<>();
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                    rs.getString("nombre"),
                    rs.getString("sku"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getString("categoria"),
                    rs.getString("material"),
                    rs.getString("talla"),
                    rs.getString("color"),
                    rs.getString("genero"),
                    rs.getInt("envioGratis") == 1,
                    rs.getInt("destacado") == 1
                );
                productos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        return productos;
    }
    
    // --- MÉTODOS DE RECUPERACIÓN DE CONTRASEÑA ---
    
    public boolean emailExiste(String email) {
        String sql = "SELECT count(*) FROM usuarios WHERE email = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
        }
        return false;
    }
    
    public String generarCodigoVerificacion(String email) {
        String codigo = String.valueOf((int)(Math.random() * 9000) + 1000); 
        long expiracion = System.currentTimeMillis() + (5 * 60 * 1000); // 5 minutos
        
        String sql = "INSERT OR REPLACE INTO codigos_verificacion (email, codigo, expiracion) VALUES (?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, codigo);
            pstmt.setLong(3, expiracion);
            
            if (pstmt.executeUpdate() > 0) {
                return codigo;
            }
        } catch (SQLException e) {
            System.err.println("Error al generar código: " + e.getMessage());
        }
        return null;
    }
    
    public boolean verificarCodigo(String email, String codigo) {
        String sql = "SELECT expiracion FROM codigos_verificacion WHERE email = ? AND codigo = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, codigo);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                long expiracion = rs.getLong("expiracion");
                if (System.currentTimeMillis() < expiracion) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar código: " + e.getMessage());
        }
        return false;
    }
    
    public boolean cambiarPassword(String email, String nuevaPassword) {
        String sql = "UPDATE usuarios SET password = ? WHERE email = ?";
        String sqlDeleteCode = "DELETE FROM codigos_verificacion WHERE email = ?";
        
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             PreparedStatement pstmtDelete = conn.prepareStatement(sqlDeleteCode)) {
            
            pstmt.setString(1, nuevaPassword);
            pstmt.setString(2, email);
            int filasActualizadas = pstmt.executeUpdate();
            
            pstmtDelete.setString(1, email);
            pstmtDelete.executeUpdate();
            
            return filasActualizadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar password: " + e.getMessage());
            return false;
        }
    }
}