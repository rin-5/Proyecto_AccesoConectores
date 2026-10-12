package com.ut3g6.aed_ut3_proyecto.controller;

import com.ut3g6.aed_ut3_proyecto.model.Componer;
import java.sql.SQLException;
import com.ut3g6.aed_ut3_proyecto.model.Conexion;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ComponerDAO {
    
    public void insertar(Componer c) throws SQLException {
        String sql = "INSERT INTO componer (id_componente_principal, id_componente_secundario, cantidad_componentes) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getId_componente_principal());
            ps.setString(2, c.getId_componente_secundario());
            ps.setInt(3, c.getCantidad_componente());
            ps.executeUpdate();
        }
    }
    
    public void actualizar(Componer c) throws SQLException {
        String sql = "UPDATE componer SET id_componente_principal = ?, id_componente_secundario = ?, cantidad_componente = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getId_componente_principal());
            ps.setString(2, c.getId_componente_secundario());
            ps.setInt(3, c.getCantidad_componente());
            ps.executeUpdate();
        }
    }
    
    public List<Componer> listar() throws SQLException {
        List<Componer> lista = new ArrayList<>();
        String sql = "SELECT * FROM componer";
        try (Connection con = Conexion.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Componer(rs.getString("id_articulo_principal"), rs.getString("id_articulo_secundario"), rs.getInt("cantidad_componente")));
            }
        }
        return lista;
    }
}
