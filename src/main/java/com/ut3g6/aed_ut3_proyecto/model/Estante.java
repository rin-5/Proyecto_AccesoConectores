package com.ut3g6.aed_ut3_proyecto.model;

public class Estante {
    private String id_estante;
    private String nombre;
    private int id_deposito;
 
    public Estante(String id_estante, String nombre, int id_deposito) {
        this.id_estante = id_estante;
        this.nombre = nombre;
        this.id_deposito = id_deposito;
    }
 
    public String getId_estante() {
        return id_estante;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public int getId_deposito() {
        return id_deposito;
    }
 
}
 
