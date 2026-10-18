
package com.ut3g6.aed_ut3_proyecto.view;

import java.sql.SQLException;

import com.ut3g6.aed_ut3_proyecto.controller.OperacionesAdicionalesDAO;
import com.ut3g6.aed_ut3_proyecto.view.EstantesView.EstanteFila;

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

public class EstanteFormView extends Stage {

    private final OperacionesAdicionalesDAO operaciones =
            new OperacionesAdicionalesDAO();

    private final boolean modoEdicion;

    private final TextField txtId = new TextField();
    private final TextField txtNombre = new TextField();
    private final TextField txtIdDeposito = new TextField();

    public EstanteFormView() {
        this(null);
    }

    public EstanteFormView(EstanteFila estante) {
        modoEdicion = estante != null;

        initModality(Modality.APPLICATION_MODAL);
        setTitle(modoEdicion ? "Modificar estante" : "Añadir estante");

        construirFormulario();

        if (modoEdicion) {
            cargarDatos(estante);
        }
    }

    private void construirFormulario() {
        txtId.setPromptText("4 caracteres");
        txtNombre.setPromptText("Nombre del estante");
        txtIdDeposito.setPromptText("ID numérico del depósito");

        txtNombre.setMaxWidth(Double.MAX_VALUE);
        txtIdDeposito.setMaxWidth(Double.MAX_VALUE);

        GridPane campos = new GridPane();
        campos.setHgap(12);
        campos.setVgap(12);

        campos.addRow(0, new Label("ID estante:"), txtId);
        campos.addRow(1, new Label("Nombre:"), txtNombre);
        campos.addRow(2, new Label("ID depósito:"), txtIdDeposito);

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.setOnAction(e -> guardar());
        btnCancelar.setOnAction(e -> close());

        HBox botones = new HBox(10, btnGuardar, btnCancelar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        VBox contenido = new VBox(
                18,
                new Label(modoEdicion
                        ? "Modificar los datos del estante"
                        : "Introducir los datos del nuevo estante"),
                campos,
                botones
        );

        contenido.setPadding(new Insets(20));

        setScene(new Scene(contenido, 440, 280));
        setResizable(false);
    }

    private void cargarDatos(EstanteFila estante) {
        txtId.setText(estante.getId());
        txtId.setDisable(true);
        txtNombre.setText(estante.getNombre());

        try {
            int idDeposito =
                    operaciones.obtenerIdDepositoEstante(estante.getId());

            txtIdDeposito.setText(String.valueOf(idDeposito));

        } catch (SQLException ex) {
            mostrarError(
                    "No se ha podido consultar el depósito: "
                            + ex.getMessage());
        }
    }

    private void guardar() {
        String id = obtenerTexto(txtId);
        String nombre = obtenerTexto(txtNombre);
        String idDepositoTexto = obtenerTexto(txtIdDeposito);

        if (id.isEmpty() || id.length() != 4) {
            mostrarAviso("El ID del estante debe tener 4 caracteres.");
            return;
        }

        if (nombre.isEmpty() || nombre.length() > 50) {
            mostrarAviso(
                    "El nombre es obligatorio y admite hasta 50 caracteres.");
            return;
        }

        int idDeposito;

        try {
            idDeposito = Integer.parseInt(idDepositoTexto);

            if (idDeposito <= 0) {
                mostrarAviso("El ID del depósito debe ser positivo.");
                return;
            }

        } catch (NumberFormatException ex) {
            mostrarAviso("El ID del depósito debe ser un número entero.");
            return;
        }

        try {
            if (modoEdicion) {
                operaciones.modificarEstante(
                        id, nombre, idDeposito);
            } else {
                operaciones.insertarEstante(
                        id, nombre, idDeposito);
            }

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Operación completada");
            alerta.setHeaderText(null);
            alerta.setContentText(modoEdicion
                    ? "El estante se ha modificado correctamente."
                    : "El estante se ha añadido correctamente.");
            alerta.showAndWait();

            close();

        } catch (SQLException ex) {
            mostrarError(
                    "No se ha podido guardar el estante: "
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

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error de base de datos");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
