package com.mycompany.proyectodashboard;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:usuarios.db";
    private Connection connection;

    public DatabaseManager() {
        try {
            // Cargar el driver de SQLite
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Crear las tablas necesarias
    private void createTables() {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "apellidos TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "telefono TEXT," +
                "genero TEXT," +
                "password TEXT NOT NULL," +
                "fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String sqlCodigosVerificacion = "CREATE TABLE IF NOT EXISTS codigos_verificacion (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT NOT NULL," +
                "codigo TEXT NOT NULL," +
                "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "usado INTEGER DEFAULT 0" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlCodigosVerificacion);
        } catch (SQLException e) {
            System.err.println("Error al crear tablas: " + e.getMessage());
        }
    }

    // Encriptar contraseña con SHA-256
    private String encriptarPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error de algoritmo de cifrado: " + e.getMessage());
            return null;
        }
    }

    // Registrar nuevo usuario
    public boolean registrarUsuario(String nombre, String apellidos, String email, 
                                     String telefono, String genero, String password) {
        String sql = "INSERT INTO usuarios (nombre, apellidos, email, telefono, genero, password) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellidos);
            pstmt.setString(3, email);
            pstmt.setString(4, telefono);
            pstmt.setString(5, genero);
            pstmt.setString(6, encriptarPassword(password));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            // Error: probable email duplicado (UNIQUE constraint)
            return false;
        }
    }

    // Iniciar sesión
    public Usuario iniciarSesion(String email, String password) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, encriptarPassword(password));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getString("genero")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error en iniciarSesion: " + e.getMessage());
        }
        return null;
    }

    // Verificar si el email existe
    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error en emailExiste: " + e.getMessage());
        }
        return false;
    }

    // Generar código de verificación
    public String generarCodigoVerificacion(String email) {
        Random random = new Random();
        // Genera un código de 6 dígitos con ceros iniciales si es necesario
        String codigo = String.format("%06d", random.nextInt(1000000));

        String sql = "INSERT INTO codigos_verificacion (email, codigo) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, codigo);
            pstmt.executeUpdate();
            return codigo;
        } catch (SQLException e) {
            System.err.println("Error al generar código: " + e.getMessage());
        }
        return null;
    }

    // Verificar código
    public boolean verificarCodigo(String email, String codigo) {
        // Busca el código más reciente y no usado para el email
        String sql = "SELECT id FROM codigos_verificacion WHERE email = ? AND codigo = ? " +
                     "AND usado = 0 ORDER BY fecha_creacion DESC LIMIT 1";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, codigo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Marcar el código como usado (prevención de reutilización)
                String updateSql = "UPDATE codigos_verificacion SET usado = 1 WHERE id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, rs.getInt("id"));
                    updateStmt.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar código: " + e.getMessage());
        }
        return false;
    }

    // Cambiar contraseña
    public boolean cambiarPassword(String email, String nuevaPassword) {
        String sql = "UPDATE usuarios SET password = ? WHERE email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, encriptarPassword(nuevaPassword));
            pstmt.setString(2, email);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar contraseña: " + e.getMessage());
        }
        return false;
    }

    // Cerrar conexión
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}