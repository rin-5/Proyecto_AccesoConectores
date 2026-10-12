
package com.ut3g6.aed_ut3_proyecto.controller;

import com.ut3g6.aed_ut3_proyecto.model.Componente;
import java.util.ArrayList;
import java.util.List;


public class ComponenteController {

    private final List<Componente> componentes;

    public ComponenteController() {
        componentes = new ArrayList<>();
    }


    public boolean agregarComponente(Componente componente) {
        if (componente == null ||
                buscarComponente(componente.getId_componente()) != null) {
            return false;
        }

        componentes.add(componente);
        return true;
    }

    public List<Componente> listarComponentes() {
        return new ArrayList<>(componentes);
    }

    public Componente buscarComponente(String id) {
        for (Componente componente : componentes) {
            if (componente.getId_componente().equals(id)) {
                return componente;
            }
        }
        return null;
    }

    public boolean modificarComponente(String id, String descripcion) {
        Componente componente = buscarComponente(id);

        if (componente != null) {
            componente.setDescripcion(descripcion);
            return true;
        }

        return false;
    }

    public boolean eliminarComponente(String id) {
        Componente componente = buscarComponente(id);

        if (componente != null) {
            componentes.remove(componente);
            return true;
        }

        return false;
    }
}
