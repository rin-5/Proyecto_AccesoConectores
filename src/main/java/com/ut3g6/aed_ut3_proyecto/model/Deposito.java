package com.ut3g6.aed_ut3_proyecto.model;

public class Deposito {
    private int id_deposito;
    private String descripcion;
    private String direccion;
    private String nombre_estante;
 
    public Deposito(int id_deposito, String descripcion, String direccion, String nombre_estante) {
        this.id_deposito = id_deposito;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.nombre_estante = nombre_estante;
    }
 
    public int getId_deposito() {
        return id_deposito;
    }
 
    public String getDescripcion() {
        return descripcion;
    }
 
    public String getDireccion() {
        return direccion;
    }
 
    public String getNombre_estante() {
        return nombre_estante;
    }
 
}
