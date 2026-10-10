package com.ut3g6.aed_ut3_proyecto.model;

public class Articulo {
    private int id_articulo;
    private String nombre;
    private float precio;
    private int id_cod;
    
    public Articulo(int id_articulo, String nombre, float precio, int id_cod){
        this.id_articulo = id_articulo;
        this.nombre = nombre;
        this.precio = precio;
        this.id_cod = id_cod;
    }

    public int getId_articulo() {
        return id_articulo;
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public int getId_cod() {
        return id_cod;
    }
    
}
