package com.ut3g6.aed_ut3_proyecto.view;

import java.sql.SQLException;

import com.ut3g6.aed_ut3_proyecto.controller.OperacionesAdicionalesDAO;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DepositoFormView extends Stage {

    private final OperacionesAdicionalesDAO operaciones =
            new OperacionesAdicionalesDAO();

    private final boolean modoEdicion;
    private final TextField txtId = new TextField();
    private final TextField txtDescripcion = new TextField();
    private final TextField txtDireccion = new TextField();
    private final TextField txtNombreEstante = new TextField();

    public DepositoFormView() {
        this(null);
    }

    public DepositoFormView(DepositosView.DepositoFila deposito) {

        modoEdicion = deposito != null;

        initModality(Modality.APPLICATION_MODAL);
        setTitle(modoEdicion ? "Modificar depósito" : "Añadir depósito");

        construirFormulario();

        if (modoEdicion) {
            cargarDatos(deposito);
        }

        txtId.setDisable(true);
        txtId.setPromptText("Autogenerado");
    }

    private void construirFormulario() {
        txtDescripcion.setPromptText("Descripción del depósito");
        txtDireccion.setPromptText("Dirección");
        txtNombreEstante.setPromptText("Nombre de estante");

        GridPane campos = new GridPane();
        campos.setHgap(12);
        campos.setVgap(12);

        campos.addRow(0, new Label("ID depósito:"), txtId);
        campos.addRow(1, new Label("Descripción:"), txtDescripcion);
        campos.addRow(2, new Label("Dirección:"), txtDireccion);
        campos.addRow(3, new Label("Nombre estante:"), txtNombreEstante);

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.setOnAction(e -> guardar());
        btnCancelar.setOnAction(e -> close());

        HBox botones = new HBox(10, btnGuardar, btnCancelar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        VBox contenido = new VBox(
                18,
                new Label(modoEdicion
                        ? "Modificar los datos del depósito"
                        : "Introducir los datos del nuevo depósito"),
                campos,
                botones
        );

        contenido.setPadding(new Insets(20));

        setScene(new Scene(contenido, 460, 320));
        setResizable(false);
    }

    private void cargarDatos(DepositosView.DepositoFila deposito) {
        txtId.setText(String.valueOf(deposito.getId()));
        txtDescripcion.setText(deposito.getDescripcion());
        txtDireccion.setText(deposito.getDireccion());

        // Este campo no está disponible en DepositoFila.
        // Se deja vacío al editar.
        txtNombreEstante.clear();
    }

    private void guardar() {
        String descripcion = obtenerTexto(txtDescripcion);
        String direccion = obtenerTexto(txtDireccion);
        String nombreEstante = obtenerTexto(txtNombreEstante);

        if (descripcion.isEmpty() || descripcion.length() > 200) {
            mostrarAviso(
                    "La descripción es obligatoria y admite hasta 200 caracteres.");
            return;
        }

        if (direccion.length() > 200) {
            mostrarAviso("La dirección admite hasta 200 caracteres.");
            return;
        }

        if (nombreEstante.length() > 20) {
            mostrarAviso("El nombre de estante admite hasta 20 caracteres.");
            return;
        }

        try {
            if (modoEdicion) {
                operaciones.modificarDeposito(
                        Integer.parseInt(txtId.getText()),
                        descripcion, direccion, nombreEstante);
            } else {
                operaciones.insertarDeposito(
                        descripcion, direccion, nombreEstante);
            }

            mostrarInformacion(modoEdicion
                    ? "El depósito se ha modificado correctamente."
                    : "El depósito se ha añadido correctamente.");

            close();

        } catch (SQLException | NumberFormatException ex) {
            mostrarError("No se ha podido guardar el depósito: "
                    + ex.getMessage());
        }
    }

    private String obtenerTexto(TextField campo) {
        String texto = campo.getText();
        return texto == null ? "" : texto.trim();
    }

    private void mostrarAviso(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Validación");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarInformacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Operación completada");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error de base de datos");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
