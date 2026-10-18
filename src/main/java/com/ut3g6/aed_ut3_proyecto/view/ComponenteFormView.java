
package com.ut3g6.aed_ut3_proyecto.view;

import java.sql.SQLException;

import com.ut3g6.aed_ut3_proyecto.controller.OperacionesAdicionalesDAO;
import com.ut3g6.aed_ut3_proyecto.view.ComponentesView.ComponenteFila;

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

public class ComponenteFormView extends Stage {

    private final OperacionesAdicionalesDAO operaciones =
            new OperacionesAdicionalesDAO();

    private final boolean modoEdicion;

    private final TextField txtId = new TextField();
    private final TextField txtDescripcion = new TextField();
    private final TextField txtIdArticulo = new TextField();

    public ComponenteFormView() {
        this(null);
    }

    public ComponenteFormView(ComponenteFila componente) {
        modoEdicion = componente != null;

        initModality(Modality.APPLICATION_MODAL);
        setTitle(modoEdicion
                ? "Modificar componente"
                : "Añadir componente");

        construirFormulario();

        if (modoEdicion) {
            cargarDatos(componente);
        }
    }

    private void construirFormulario() {
        txtId.setPromptText("4 caracteres");
        txtDescripcion.setPromptText("Descripción del componente");
        txtIdArticulo.setPromptText("ID artículo (opcional)");

        txtDescripcion.setMaxWidth(Double.MAX_VALUE);
        txtIdArticulo.setMaxWidth(Double.MAX_VALUE);

        GridPane campos = new GridPane();
        campos.setHgap(12);
        campos.setVgap(12);

        campos.addRow(0, new Label("ID componente:"), txtId);
        campos.addRow(1, new Label("Descripción:"), txtDescripcion);
        campos.addRow(2, new Label("ID artículo:"), txtIdArticulo);

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.setOnAction(e -> guardar());
        btnCancelar.setOnAction(e -> close());

        HBox botones = new HBox(10, btnGuardar, btnCancelar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        VBox contenido = new VBox(
                18,
                new Label(modoEdicion
                        ? "Modificar los datos del componente"
                        : "Introducir los datos del nuevo componente"),
                campos,
                botones
        );

        contenido.setPadding(new Insets(20));

        setScene(new Scene(contenido, 460, 280));
        setResizable(false);
    }

    private void cargarDatos(ComponenteFila componente) {
        txtId.setText(componente.getId());
        txtId.setDisable(true);

        txtDescripcion.setText(componente.getDescripcion());

        // El ID del artículo se consulta en el DAO.
        try {
            String idArticulo =
                    operaciones.obtenerIdArticuloComponente(
                            componente.getId());

            txtIdArticulo.setText(
                    idArticulo == null ? "" : idArticulo);

        } catch (SQLException ex) {
            mostrarError(
                    "No se ha podido consultar el artículo asociado: "
                            + ex.getMessage());
        }
    }

    private void guardar() {
        String id = obtenerTexto(txtId);
        String descripcion = obtenerTexto(txtDescripcion);
        String idArticulo = obtenerTexto(txtIdArticulo);

        if (id.isEmpty() || id.length() != 4) {
            mostrarAviso(
                    "El ID del componente debe tener 4 caracteres.");
            return;
        }

        if (descripcion.isEmpty() || descripcion.length() > 100) {
            mostrarAviso(
                    "La descripción es obligatoria y admite hasta 100 caracteres.");
            return;
        }

        if (!idArticulo.isEmpty() && idArticulo.length() != 4) {
            mostrarAviso(
                    "El ID del artículo debe tener 4 caracteres.");
            return;
        }

        try {
            if (modoEdicion) {
                operaciones.modificarComponente(
                        id, descripcion,
                        idArticulo.isEmpty() ? null : idArticulo);
            } else {
                operaciones.insertarComponente(
                        id, descripcion,
                        idArticulo.isEmpty() ? null : idArticulo);
            }

            mostrarInformacion(modoEdicion
                    ? "El componente se ha modificado correctamente."
                    : "El componente se ha añadido correctamente.");

            close();

        } catch (SQLException ex) {
            mostrarError(
                    "No se ha podido guardar el componente: "
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
