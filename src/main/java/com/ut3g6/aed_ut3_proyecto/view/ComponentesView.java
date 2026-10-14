package com.ut3g6.aed_ut3_proyecto.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ComponentesView extends VBox {

    public ComponentesView() {
        Label titulo = new Label("Componentes");
        titulo.setStyle("-fx-font-size: 24px; "
                + "-fx-font-weight: bold;");

        setAlignment(Pos.TOP_LEFT);
        setSpacing(15);
        getChildren().add(titulo);
    }
}
