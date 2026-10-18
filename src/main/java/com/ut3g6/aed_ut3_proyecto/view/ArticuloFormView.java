package com.ut3g6.aed_ut3_proyecto.view;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.ut3g6.aed_ut3_proyecto.controller.OperacionesAdicionalesDAO;
import com.ut3g6.aed_ut3_proyecto.view.ArticulosView.ArticuloFila;

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

public class ArticuloFormView extends Stage {

    private final OperacionesAdicionalesDAO operaciones =
            new OperacionesAdicionalesDAO();

    private final boolean modoEdicion;

    private final TextField txtId = new TextField();
    private final TextField txtNombre = new TextField();
    private final TextField txtPrecio = new TextField();
    private final TextField txtCodigo = new TextField();
    private final TextField txtCantidad = new TextField();
    private final TextField txtEstante = new TextField();

    public ArticuloFormView() {
        this(null);
    }

    public ArticuloFormView(ArticuloFila articulo) {
        modoEdicion = articulo != null;

        initModality(Modality.APPLICATION_MODAL);
        setTitle(modoEdicion ? "Modificar artículo" : "Añadir artículo");

        construirFormulario();

        if (modoEdicion) {
            cargarDatos(articulo);
        }
    }

    private void construirFormulario() {
        txtId.setPromptText("4 caracteres");
        txtNombre.setPromptText("Nombre del artículo");
        txtPrecio.setPromptText("Ej. 12.50");
        txtCodigo.setPromptText("Código de componente (opcional)");
        txtCantidad.setPromptText("Cantidad disponible");
        txtEstante.setPromptText("ID del estante (4 caracteres)");

        txtNombre.setMaxWidth(Double.MAX_VALUE);
        txtPrecio.setMaxWidth(Double.MAX_VALUE);
        txtCodigo.setMaxWidth(Double.MAX_VALUE);
        txtCantidad.setMaxWidth(Double.MAX_VALUE);
        txtEstante.setMaxWidth(Double.MAX_VALUE);

        GridPane campos = new GridPane();
        campos.setHgap(12);
        campos.setVgap(12);

        campos.addRow(0, new Label("ID artículo:"), txtId);
        campos.addRow(1, new Label("Nombre:"), txtNombre);
        campos.addRow(2, new Label("Precio:"), txtPrecio);
        campos.addRow(3, new Label("Código componente:"), txtCodigo);
        campos.addRow(4, new Label("Cantidad:"), txtCantidad);
        campos.addRow(5, new Label("ID estante:"), txtEstante);

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.setOnAction(e -> guardar());
        btnCancelar.setOnAction(e -> close());

        HBox botones = new HBox(10, btnGuardar, btnCancelar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        VBox contenido = new VBox(
                18,
                new Label(modoEdicion
                        ? "Modificar los datos del artículo"
                        : "Introducir los datos del nuevo artículo"),
                campos,
                botones
        );

        contenido.setPadding(new Insets(20));

        setScene(new Scene(contenido, 440, 380));
        setResizable(false);
    }

    private void cargarDatos(ArticuloFila articulo) {
        txtId.setText(articulo.getId());
        txtId.setDisable(true);

        txtNombre.setText(articulo.getNombre());
        txtPrecio.setText(
                BigDecimal.valueOf(articulo.getPrecio()).toPlainString());
        txtCodigo.setText(articulo.getCodigo());
        txtCantidad.setText(String.valueOf(articulo.getCantidad()));

        try {
            String idEstante =
                    operaciones.obtenerIdEstanteArticulo(articulo.getId());

            txtEstante.setText(idEstante != null ? idEstante : "");

            if (idEstante == null) {
                mostrarAviso(
                        "No se ha encontrado un estante asociado al artículo.");
            }

        } catch (SQLException ex) {
            mostrarError(
                    "No se ha podido consultar el estante: " + ex.getMessage());
        }
    }

    private void guardar() {
        String id = obtenerTexto(txtId);
        String nombre = obtenerTexto(txtNombre);
        String precioTexto = obtenerTexto(txtPrecio);
        String codigo = obtenerTexto(txtCodigo);
        String cantidadTexto = obtenerTexto(txtCantidad);
        String idEstante = obtenerTexto(txtEstante);

        if (id.isEmpty() || id.length() > 4) {
            mostrarAviso("El ID debe tener entre 1 y 4 caracteres.");
            return;
        }

        if (nombre.isEmpty() || nombre.length() > 20) {
            mostrarAviso(
                    "El nombre es obligatorio y no puede superar "
                            + "20 caracteres.");
            return;
        }

        if (!codigo.isEmpty() && codigo.length() != 4) {
            mostrarAviso(
                    "El código del componente debe tener 4 caracteres.");
            return;
        }

        if (idEstante.length() != 4) {
            mostrarAviso(
                    "El ID del estante debe tener 4 caracteres.");
            return;
        }

        BigDecimal precio;
        int cantidad;

        try {
            precio = new BigDecimal(
                    precioTexto.replace(',', '.'));

            if (precio.scale() > 2) {
                mostrarAviso("El precio admite como máximo dos decimales.");
                return;
            }

            if (precio.compareTo(BigDecimal.ZERO) < 0
                    || precio.compareTo(new BigDecimal("99.99")) > 0) {
                mostrarAviso(
                        "El precio debe estar entre 0 y 99,99.");
                return;
            }

        } catch (NumberFormatException ex) {
            mostrarAviso("Introduce un precio válido, por ejemplo 12.50.");
            return;
        }

        try {
            cantidad = Integer.parseInt(cantidadTexto);

            if (cantidad < 0) {
                mostrarAviso("La cantidad no puede ser negativa.");
                return;
            }

        } catch (NumberFormatException ex) {
            mostrarAviso("La cantidad debe ser un número entero.");
            return;
        }

        try {
            if (modoEdicion) {
                operaciones.modificarArticulo(
                        id, nombre, precio,
                        codigo.isEmpty() ? null : codigo,
                        cantidad, idEstante);
            } else {
                operaciones.insertarArticulo(
                        id, nombre, precio,
                        codigo.isEmpty() ? null : codigo,
                        cantidad, idEstante);
            }

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Operación completada");
            alerta.setHeaderText(null);
            alerta.setContentText(modoEdicion
                    ? "El artículo se ha modificado correctamente."
                    : "El artículo se ha añadido correctamente.");
            alerta.showAndWait();

            close();

        } catch (SQLException ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de base de datos");
            alerta.setHeaderText(
                    "No se ha podido guardar el artículo.");
            alerta.setContentText(ex.getMessage());
            alerta.showAndWait();
        }
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

    private String obtenerTexto(TextField campo) {
        String texto = campo.getText();
        return texto == null ? "" : texto.trim();
    }
}
