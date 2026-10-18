package com.ut3g6.aed_ut3_proyecto.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ut3g6.aed_ut3_proyecto.model.Conexion;

public class TotalArticulosDAO {
    public int totalArticulos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM articulos";

        try (Connection con = Conexion.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getInt(1);
        }
    }

    public int articulosBajoStock() throws SQLException {
        String sql = """
            SELECT COUNT(*)
            FROM articulos
            WHERE cantidad > 0 AND cantidad <= 10
            """;

        try (Connection con = Conexion.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getInt(1);
        }
    }
}
