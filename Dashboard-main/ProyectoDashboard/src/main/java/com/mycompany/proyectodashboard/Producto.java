package com.mycompany.proyectodashboard;

public class Producto {
    
    private String nombre;
    private String sku;
    private double precio;
    private int stock;
    private String categoria;
    private String material;
    private String talla;
    private String color;
    private String genero;
    private boolean envioGratis;
    private boolean destacado;

    public Producto(String nombre, String sku, double precio, int stock, String categoria, String material, String talla, String color, String genero, boolean envioGratis, boolean destacado) {
        this.nombre = nombre;
        this.sku = sku;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.material = material;
        this.talla = talla;
        this.color = color;
        this.genero = genero;
        this.envioGratis = envioGratis;
        this.destacado = destacado;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getSku() { return sku; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public String getCategoria() { return categoria; }
    public String getMaterial() { return material; }
    public String getTalla() { return talla; }
    public String getColor() { return color; }
    public String getGenero() { return genero; }
    public boolean isEnvioGratis() { return envioGratis; }
    public boolean isDestacado() { return destacado; }
}