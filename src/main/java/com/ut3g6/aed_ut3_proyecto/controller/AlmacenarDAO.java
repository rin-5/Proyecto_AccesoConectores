package com.ut3g6.aed_ut3_proyecto.controller;

import com.ut3g6.aed_ut3_proyecto.model.Almacenar;
import com.ut3g6.aed_ut3_proyecto.model.Conexion;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class AlmacenarDAO {

    public void insertar(Almacenar a) throws SQLException {
        String sql = "INSERT INTO almacenar_articulo (id_articulo, id_estanteria, cantidad_disponible) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getId_articulo());
            ps.setString(2, a.getId_estanteria());
            ps.setInt(3, a.getCantidad_disponible());
            ps.executeUpdate();
        }
    }


    
    public void actualizar(Almacenar a) throws SQLException {
        String sql = "UPDATE almacenar_articulo SET cantidad_disponible = ? WHERE id_articulo = ? AND id_estanteria = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getCantidad_disponible());
            ps.setString(2, a.getId_articulo());
            ps.setString(3, a.getId_estanteria());
            ps.executeUpdate();
        }
    }



    public List<Almacenar> listar() throws SQLException {
        List<Almacenar> lista = new ArrayList<>();
        String sql = "SELECT * FROM almacenar_articulo";
        try (Connection con = Conexion.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Almacenar(rs.getString("id_articulo"), rs.getString("id_estanteria"), rs.getInt("cantidad_disponible")));
            }
        }
        return lista;
    }

    

    public List<Almacenar> listarPorArticulo(String id_articulo) throws SQLException {
        List<Almacenar> lista = new ArrayList<>();
        String sql = "SELECT * FROM almacenar_articulo WHERE id_articulo = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id_articulo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Almacenar(rs.getString("id_articulo"), rs.getString("id_estanteria"), rs.getInt("cantidad_disponible")));
                }
            }
        }
        return lista;
    }
}
