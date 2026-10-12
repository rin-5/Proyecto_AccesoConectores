package com.ut3g6.aed_ut3_proyecto.controller;

import com.ut3g6.aed_ut3_proyecto.model.Articulo;
import com.ut3g6.aed_ut3_proyecto.model.Conexion;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ArticuloDAO {
    public void insert(Articulo a) throws SQLException {
        String sql = "INSERT INTO articulos (id_artculo, nombre, precio, id_cod) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getId_articulo());
            ps.setString(2, a.getNombre());
            ps.setFloat(3, a.getPrecio());
            ps.setInt(4, a.getId_cod());
            ps.executeUpdate();
        }
    }
    
    public void actualizar(Articulo a) throws SQLException {
        String sql = "UPDATE articulos SET nombre = ?, precio = ?, id_cod = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setFloat(2, a.getPrecio());
            ps.setInt(3, a.getId_cod());
            ps.executeUpdate();
        }
    }
    
    public List<Articulo> listar() throws SQLException {
        List<Articulo> lista = new ArrayList<>();
        String sql = "SELECT * FROM articulos";
        try (Connection con = Conexion.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Articulo(rs.getInt("id_articulo"), rs.getString("nombre"), rs.getFloat("precio"), rs.getInt("id_cod")));
            }
        }
        return lista;
    }
}
