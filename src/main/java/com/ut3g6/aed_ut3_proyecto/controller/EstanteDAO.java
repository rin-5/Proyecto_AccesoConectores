package com.ut3g6.aed_ut3_proyecto.controller;

import com.ut3g6.aed_ut3_proyecto.model.Conexion;
import com.ut3g6.aed_ut3_proyecto.model.Estante;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class EstanteDAO {

    public void insertar(Estante e) throws SQLException {
        String sql = "INSERT INTO estante (nombre, id_deposito) VALUES (?, ?)";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getId_deposito());
            ps.executeUpdate();
        }
    }



    public void actualizar(Estante e) throws SQLException {
        String sql = "UPDATE estante SET nombre = ?, id_deposito = ? WHERE id_estante = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getId_deposito());
            ps.setString(3, e.getId_estante());
            ps.executeUpdate();
        }
    }


    
    public List<Estante> listar() throws SQLException {
        List<Estante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estante";
        try (Connection con = Conexion.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Estante(rs.getString("id_estante"), rs.getString("nombre"), rs.getInt("id_deposito")));
            }
        }
        return lista;
    }
}