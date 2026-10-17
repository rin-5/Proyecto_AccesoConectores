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

public class EstantesView extends VBox {

    private final Consumer<Boolean> cambioSeleccion;

    public EstantesView(Consumer<Boolean> cambioSeleccion) {
        this.cambioSeleccion = cambioSeleccion;

        setAlignment(Pos.TOP_LEFT);
        setSpacing(15);
        setPadding(new Insets(20));

        // Filtros
        ComboBox<String> filtro = new ComboBox<>();
        filtro.getItems().addAll(
                "Todo", "Depósito principal", "Depósito secundario");
        filtro.setValue("Todo");

        Button btnBuscar = new Button("Buscar");

        HBox zonaFiltros = new HBox(10, filtro, btnBuscar);
        zonaFiltros.setAlignment(Pos.CENTER_LEFT);

        // Tabla
        TableView<EstanteFila> tabla = new TableView<>();

        TableColumn<EstanteFila, Boolean> colSeleccion =
                new TableColumn<>("");

        colSeleccion.setCellValueFactory(
                new PropertyValueFactory<>("seleccionado"));

        colSeleccion.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    EstanteFila fila = getTableRow().getItem();

                    if (fila != null) {
                        fila.setSeleccionado(checkBox.isSelected());

                        cambioSeleccion.accept(
                                tabla.getItems().stream()
                                        .anyMatch(
                                                EstanteFila::isSeleccionado));
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

        TableColumn<EstanteFila, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn<EstanteFila, String> colNombre =
                new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));

        TableColumn<EstanteFila, String> colDeposito =
                new TableColumn<>("Depósito");
        colDeposito.setCellValueFactory(
                new PropertyValueFactory<>("deposito"));

        tabla.getColumns().addAll(
                colSeleccion, colId, colNombre, colDeposito);

        tabla.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Datos provisionales
        tabla.setItems(FXCollections.observableArrayList(
                new EstanteFila(1, "Estante E-01", "Depósito principal"),
                new EstanteFila(2, "Estante E-02", "Depósito principal"),
                new EstanteFila(3, "Estante E-03", "Depósito secundario")
        ));

        // Seleccionar todos
        CheckBox seleccionarTodos =
                new CheckBox("Seleccionar todo");

        seleccionarTodos.setOnAction(e -> {
            for (EstanteFila estante : tabla.getItems()) {
                estante.setSeleccionado(
                        seleccionarTodos.isSelected());
            }

            tabla.refresh();

            cambioSeleccion.accept(
                    tabla.getItems().stream()
                            .anyMatch(EstanteFila::isSeleccionado));
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

    public static class EstanteFila {

        private final int id;
        private final String nombre;
        private final String deposito;
        private boolean seleccionado;

        public EstanteFila(int id, String nombre, String deposito) {
            this.id = id;
            this.nombre = nombre;
            this.deposito = deposito;
            this.seleccionado = false;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDeposito() {
            return deposito;
        }

        public boolean isSeleccionado() {
            return seleccionado;
        }

        public void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
        }
    }
}