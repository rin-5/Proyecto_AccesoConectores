/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.ut3g6.aed_ut3_proyecto;

import com.ut3g6.aed_ut3_proyecto.model.Conexion;
import java.sql.Connection;
import java.sql.SQLException;
import com.ut3g6.aed_ut3_proyecto.view.MainMenuView;
import javafx.application.Application;

public class AED_UT3_Proyecto {

    public static void main(String[] args) {
        try (Connection conexion = Conexion.getConexion()) {
            if (conexion != null && !conexion.isClosed()) {
                System.out.println("Conexión establecida correctamente con la base de datos.");
                Application.launch(MainMenuView.class, args);
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos.");
       
        }
    }

}
