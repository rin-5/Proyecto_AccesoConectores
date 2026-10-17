package com.ut3g6.aed_ut3_proyecto.view;

import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DepositosView extends VBox {

    private final Consumer<Boolean> cambioSeleccion;

    public DepositosView(Consumer<Boolean> cambioSeleccion) {
        this.cambioSeleccion = cambioSeleccion;

        setAlignment(Pos.TOP_LEFT);
        setSpacing(15);
        setPadding(new Insets(20));

        // Filtros
        ComboBox<String> filtro = new ComboBox<>();
        filtro.getItems().addAll("Todo", "Con estantes", "Sin estantes");
        filtro.setValue("Todo");

        Button btnBuscar = new Button("Buscar");

        HBox zonaFiltros = new HBox(10, filtro, btnBuscar);
        zonaFiltros.setAlignment(Pos.CENTER_LEFT);

        // Tabla
        TableView<DepositoFila> tabla = new TableView<>();

        TableColumn<DepositoFila, Boolean> colSeleccion =
                new TableColumn<>("");

        colSeleccion.setCellValueFactory(
                new PropertyValueFactory<>("seleccionado"));

        colSeleccion.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    DepositoFila fila = getTableRow().getItem();

                    if (fila != null) {
                        fila.setSeleccionado(checkBox.isSelected());

                        cambioSeleccion.accept(
                                tabla.getItems().stream()
                                        .anyMatch(
                                                DepositoFila::isSeleccionado));
                    }
                });
            }

            @Override
            protected void updateItem(Boolean seleccionado, boolean vacio) {
                super.updateItem(seleccionado, vacio);

                if (vacio) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(
                            Boolean.TRUE.equals(seleccionado));
                    setGraphic(checkBox);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        colSeleccion.setPrefWidth(45);

        TableColumn<DepositoFila, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn<DepositoFila, String> colDescripcion =
                new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(
                new PropertyValueFactory<>("descripcion"));

        TableColumn<DepositoFila, String> colDireccion =
                new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(
                new PropertyValueFactory<>("direccion"));

        TableColumn<DepositoFila, Integer> colEstantes =
                new TableColumn<>("Estantes");
        colEstantes.setCellValueFactory(
                new PropertyValueFactory<>("estantes"));

        tabla.getColumns().addAll(
                colSeleccion, colId, colDescripcion,
                colDireccion, colEstantes);

        tabla.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Datos provisionales
        tabla.setItems(FXCollections.observableArrayList(
                new DepositoFila(
                        1, "Depósito principal", "Calle Mayor 10", 12),
                new DepositoFila(
                        2, "Depósito secundario", "Calle Norte 25", 6),
                new DepositoFila(
                        3, "Depósito auxiliar", "Avenida Central 8", 0)
        ));

        // Seleccionar todos
        CheckBox seleccionarTodos =
                new CheckBox("Seleccionar todo");

        seleccionarTodos.setOnAction(e -> {
            for (DepositoFila deposito : tabla.getItems()) {
                deposito.setSeleccionado(
                        seleccionarTodos.isSelected());
            }

            tabla.refresh();

            cambioSeleccion.accept(
                    tabla.getItems().stream()
                            .anyMatch(DepositoFila::isSeleccionado));
        });

        HBox zonaAcciones = new HBox(10, seleccionarTodos);
        zonaAcciones.setAlignment(Pos.CENTER_LEFT);

        // Paginación visual
        Label lblPagina = new Label("Página 1 de 1");
        Button btnAnterior = new Button("Anterior");
        Button btnSiguiente = new Button("Siguiente");

        HBox paginacion = new HBox(
                10, btnAnterior, lblPagina, btnSiguiente);
        paginacion.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(
                zonaFiltros, tabla, zonaAcciones, paginacion);

        VBox.setVgrow(tabla, Priority.ALWAYS);
    }

    public static class DepositoFila {

        private final int id;
        private final String descripcion;
        private final String direccion;
        private final int estantes;
        private boolean seleccionado;

        public DepositoFila(int id, String descripcion,
                            String direccion, int estantes) {
            this.id = id;
            this.descripcion = descripcion;
            this.direccion = direccion;
            this.estantes = estantes;
            this.seleccionado = false;
        }

        public int getId() {
            return id;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getDireccion() {
            return direccion;
        }

        public int getEstantes() {
            return estantes;
        }

        public boolean isSeleccionado() {
            return seleccionado;
        }

        public void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
        }
    }
}