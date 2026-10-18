package com.ut3g6.aed_ut3_proyecto.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmarEliminacionView extends Stage {

    public ConfirmarEliminacionView(
            String tipoRegistro,
            int cantidad,
            Runnable accionEliminar) {

        initModality(Modality.APPLICATION_MODAL);
        setTitle("Confirmar eliminación");

        Label mensaje = new Label(
                "¿Seguro que deseas eliminar "
                + cantidad + " "
                + tipoRegistro
                + (cantidad == 1 ? "" : " seleccionados")
                + "?");

        Button btnConfirmar = new Button("Eliminar");
        Button btnCancelar = new Button("Cancelar");

        btnConfirmar.setOnAction(e -> {
            accionEliminar.run();
            close();
        });

        btnCancelar.setOnAction(e -> close());

        HBox botones = new HBox(10, btnConfirmar, btnCancelar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        VBox contenido = new VBox(20, mensaje, botones);
        contenido.setPadding(new Insets(20));

        setScene(new Scene(contenido, 360, 130));
        setResizable(false);
    }
}
