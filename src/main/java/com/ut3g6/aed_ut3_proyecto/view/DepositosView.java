package com.ut3g6.aed_ut3_proyecto.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DepositosView extends VBox {

    public DepositosView() {
        Label titulo = new Label("Depósitos");
        titulo.setStyle("-fx-font-size: 24px; "
                + "-fx-font-weight: bold;");

        setAlignment(Pos.TOP_LEFT);
        setSpacing(15);
        getChildren().add(titulo);
    }
}
