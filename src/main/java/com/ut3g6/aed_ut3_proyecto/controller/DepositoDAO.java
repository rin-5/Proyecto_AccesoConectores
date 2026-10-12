package com.ut3g6.aed_ut3_proyecto.controller;

import com.ut3g6.aed_ut3_proyecto.model.Conexion;
import com.ut3g6.aed_ut3_proyecto.model.Deposito;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class DepositoDAO {

    public void insertar(Deposito d) throws SQLException {
        String sql = "INSERT INTO deposito (descripcion, direccion, nombre_estante) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getDescripcion());
            ps.setString(2, d.getDireccion());
            ps.setString(3, d.getNombre_estante());
            ps.executeUpdate();
        }
    }



    public void actualizar(Deposito d) throws SQLException {
        String sql = "UPDATE deposito SET descripcion = ?, direccion = ?, nombre_estante = ? WHERE id_deposito = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getDescripcion());
            ps.setString(2, d.getDireccion());
            ps.setString(3, d.getNombre_estante());
            ps.setInt(4, d.getId_deposito());
            ps.executeUpdate();
        }
    }



    public List<Deposito> listar() throws SQLException {
        List<Deposito> lista = new ArrayList<>();
        String sql = "SELECT * FROM deposito";
        try (Connection con = Conexion.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Deposito(rs.getInt("id_deposito"), rs.getString("descripcion"), rs.getString("direccion"), rs.getString("nombre_estante")));
            }
        }
        return lista;
    }
}