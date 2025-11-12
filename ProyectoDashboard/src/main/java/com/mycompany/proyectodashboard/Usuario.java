package com.mycompany.proyectodashboard;

public class Usuario {
    
    private int id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String genero; 

    public Usuario(int id, String nombre, String apellidos, String email, String telefono, String genero) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.genero = genero;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getGenero() { return genero; }
}