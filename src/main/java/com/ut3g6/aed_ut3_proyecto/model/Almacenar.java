package com.ut3g6.aed_ut3_proyecto.model;

public class Almacenar {
    private String id_articulo;
    private String id_estanteria;
    private int cantidad_disponible;

    public Almacenar(String id_articulo, String id_estanteria, int cantidad_disponible) {
        this.id_articulo = id_articulo;
        this.id_estanteria = id_estanteria;
        this.cantidad_disponible = cantidad_disponible;
    }

    public String getId_articulo() {
        return id_articulo;
    }

    public String getId_estanteria() {
        return id_estanteria;
    }

    public int getCantidad_disponible() {
        return cantidad_disponible;
    }

}