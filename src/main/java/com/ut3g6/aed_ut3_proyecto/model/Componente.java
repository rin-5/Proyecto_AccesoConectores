package com.ut3g6.aed_ut3_proyecto.model;

public class Componente {
    private char id_componente;
    private String descripcion;
    private int id_art;

    public Componente(char id_componente, String descripcion, int id_art) {
        this.id_componente = id_componente;
        this.descripcion = descripcion;
        this.id_art = id_art;
    }

    public char getId_componente() {
        return id_componente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getId_art() {
        return id_art;
    }
    
    
}
