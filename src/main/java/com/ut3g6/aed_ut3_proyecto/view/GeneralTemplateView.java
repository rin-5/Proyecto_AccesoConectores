
package com.ut3g6.aed_ut3_proyecto.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class GeneralTemplateView extends BorderPane {

    public GeneralTemplateView(String titulo, Node contenido,
            Runnable volverMenu) {

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 24px; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: white;");

        Button btnVolver = new Button("Volver al menú");
        btnVolver.setOnAction(e -> volverMenu.run());

        VBox cabecera = new VBox(10, lblTitulo, btnVolver);
        cabecera.setAlignment(Pos.CENTER_LEFT);
        cabecera.setPadding(new Insets(20));
        cabecera.setStyle("-fx-background-color: #243447;");

        setTop(cabecera);
        setCenter(contenido);
        setStyle("-fx-background-color: #F5F7FA;");
    }
}
