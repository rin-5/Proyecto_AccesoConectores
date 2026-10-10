package com.ut3g6.aed_ut3_proyecto.model;

public class Componer {
    private String id_componente_principal;
    private String id_componente_secundario;
    private int cantidad_componente;
    
    public Componer(String id_componente_principal, String id_componente_secundario, int cantidad_componente){
        this.id_componente_principal = id_componente_principal;
        this.id_componente_secundario = id_componente_secundario;
        this.cantidad_componente = cantidad_componente;
    }

    public String getId_componente_principal() {
        return id_componente_principal;
    }

    public String getId_componente_secundario() {
        return id_componente_secundario;
    }

    public int getCantidad_componente() {
        return cantidad_componente;
    }
    
}
