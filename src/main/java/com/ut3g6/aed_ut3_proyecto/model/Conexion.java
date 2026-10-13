package com.ut3g6.aed_ut3_proyecto.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mariadb://localhost:3306/almacen";
    private static final String USER = "root";
    private static final String PASS = "";
    
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASS);
    }
}
