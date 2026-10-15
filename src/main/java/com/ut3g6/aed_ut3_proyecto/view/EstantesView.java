package com.ut3g6.aed_ut3_proyecto.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EstantesView extends VBox {

    public EstantesView() {
        Label titulo = new Label("Estantes");
        titulo.setStyle("-fx-font-size: 24px; "
                + "-fx-font-weight: bold;");

        setAlignment(Pos.TOP_LEFT);
        setPadding(new Insets(20));
        setSpacing(15);
        getChildren().add(titulo);
    }
}
