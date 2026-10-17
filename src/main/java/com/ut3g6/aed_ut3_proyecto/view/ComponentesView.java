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

public class ComponentesView extends VBox {

    private final Consumer<Boolean> cambioSeleccion;

    public ComponentesView(Consumer<Boolean> cambioSeleccion) {
        this.cambioSeleccion = cambioSeleccion;

        setAlignment(Pos.TOP_LEFT);
        setSpacing(15);
        setPadding(new Insets(20));

        // Filtros
        ComboBox<String> filtro = new ComboBox<>();
        filtro.getItems().addAll(
                "Todo", "Con subcomponentes", "Sin subcomponentes");
        filtro.setValue("Todo");

        Button btnBuscar = new Button("Buscar");

        HBox zonaFiltros = new HBox(10, filtro, btnBuscar);
        zonaFiltros.setAlignment(Pos.CENTER_LEFT);

        // Tabla
        TableView<ComponenteFila> tabla = new TableView<>();

        TableColumn<ComponenteFila, Boolean> colSeleccion =
                new TableColumn<>("");

        colSeleccion.setCellValueFactory(
                new PropertyValueFactory<>("seleccionado"));

        colSeleccion.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    ComponenteFila fila = getTableRow().getItem();

                    if (fila != null) {
                        fila.setSeleccionado(checkBox.isSelected());

                        cambioSeleccion.accept(
                                tabla.getItems().stream()
                                        .anyMatch(
                                                ComponenteFila::isSeleccionado));
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

        TableColumn<ComponenteFila, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn<ComponenteFila, String> colDescripcion =
                new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(
                new PropertyValueFactory<>("descripcion"));

        TableColumn<ComponenteFila, Integer> colCantidad =
                new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(
                new PropertyValueFactory<>("cantidad"));

        TableColumn<ComponenteFila, String> colSubcomponentes =
                new TableColumn<>("Subcomponentes");
        colSubcomponentes.setCellValueFactory(
                new PropertyValueFactory<>("subcomponentes"));

        tabla.getColumns().addAll(
                colSeleccion, colId, colDescripcion,
                colCantidad, colSubcomponentes);

        tabla.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Datos provisionales
        tabla.setItems(FXCollections.observableArrayList(
                new ComponenteFila(
                        1, "Motor eléctrico", 5, "Rotor, carcasa"),
                new ComponenteFila(
                        2, "Sistema de transmisión", 10, "Engranajes"),
                new ComponenteFila(
                        3, "Tornillo", 250, "Ninguno")
        ));

        // Seleccionar todos
        CheckBox seleccionarTodos =
                new CheckBox("Seleccionar todo");

        seleccionarTodos.setOnAction(e -> {
            for (ComponenteFila componente : tabla.getItems()) {
                componente.setSeleccionado(
                        seleccionarTodos.isSelected());
            }

            tabla.refresh();

            cambioSeleccion.accept(
                    tabla.getItems().stream()
                            .anyMatch(
                                    ComponenteFila::isSeleccionado));
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

    public static class ComponenteFila {

        private final int id;
        private final String descripcion;
        private final int cantidad;
        private final String subcomponentes;
        private boolean seleccionado;

        public ComponenteFila(int id, String descripcion,
                              int cantidad, String subcomponentes) {
            this.id = id;
            this.descripcion = descripcion;
            this.cantidad = cantidad;
            this.subcomponentes = subcomponentes;
            this.seleccionado = false;
        }

        public int getId() {
            return id;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public int getCantidad() {
            return cantidad;
        }

        public String getSubcomponentes() {
            return subcomponentes;
        }

        public boolean isSeleccionado() {
            return seleccionado;
        }

        public void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
        }
    }
}