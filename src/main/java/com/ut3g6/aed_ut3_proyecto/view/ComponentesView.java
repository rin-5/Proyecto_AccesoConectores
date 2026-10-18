package com.ut3g6.aed_ut3_proyecto.view;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.ut3g6.aed_ut3_proyecto.controller.ConsultasAdicionalesDAO;
import com.ut3g6.aed_ut3_proyecto.controller.ConsultasAdicionalesDAO.ComponenteInventario;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
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

    private final TableView<ComponenteFila> tabla = new TableView<>();
    private final ComboBox<String> filtro = new ComboBox<>();
    private final ConsultasAdicionalesDAO consultas =
            new ConsultasAdicionalesDAO();

    public ComponentesView(Consumer<Boolean> cambioSeleccion) {
        this.cambioSeleccion = cambioSeleccion;

        setAlignment(Pos.TOP_LEFT);
        setSpacing(15);
        setPadding(new Insets(20));

        // Filtros
        filtro.getItems().addAll(
                "Todo", "Con subcomponentes", "Sin subcomponentes");
        filtro.setValue("Todo");

        Button btnBuscar = new Button("Buscar");

        HBox zonaFiltros = new HBox(10, filtro, btnBuscar);
        zonaFiltros.setAlignment(Pos.CENTER_LEFT);

        // Tabla
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

        cargarComponentes();
        btnBuscar.setOnAction(e -> aplicarFiltro());

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

        private final String id;
        private final String descripcion;
        private final int cantidad;
        private final String subcomponentes;
        private final boolean tieneSubcomponentes;
        private boolean seleccionado;

        public ComponenteFila(String id, String descripcion,
                              int cantidad, String subcomponentes, boolean tieneSubcomponentes) {
            this.id = id;
            this.descripcion = descripcion;
            this.cantidad = cantidad;
            this.subcomponentes = subcomponentes;
            this.tieneSubcomponentes = tieneSubcomponentes;
            this.seleccionado = false;
        }

        public String getId() {
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

        public boolean isTieneSubcomponentes() {
            return tieneSubcomponentes;
        }

        public boolean isSeleccionado() {
            return seleccionado;
        }

        public void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
        }
    }

    private void cargarComponentes() {
        try {
            List<ComponenteInventario> componentes =
                    consultas.listarComponentesInventario();

            tabla.setItems(FXCollections.observableArrayList(
                    componentes.stream()
                            .map(c -> new ComponenteFila(
                                    c.getId(),
                                    c.getDescripcion(),
                                    c.getCantidad(),
                                    c.getSubcomponentes(),
                                    c.isTieneSubcomponentes()))
                            .collect(Collectors.toList())
            ));

            aplicarFiltro();

        } catch (SQLException ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de base de datos");
            alerta.setHeaderText(
                    "No se han podido cargar los componentes");
            alerta.setContentText(ex.getMessage());
            alerta.showAndWait();
        }
    }

    private void aplicarFiltro() {
        try {
            List<ComponenteInventario> componentes =
                    consultas.listarComponentesInventario();

            String opcionSeleccionada = filtro.getValue();

            List<ComponenteFila> filas = componentes.stream()
                    .filter(c -> {
                        if (opcionSeleccionada == null
                                || opcionSeleccionada.equals("Todo")) {
                            return true;
                        }

                        if (opcionSeleccionada.equals(
                                "Con subcomponentes")) {
                            return c.isTieneSubcomponentes();
                        }

                        if (opcionSeleccionada.equals(
                                "Sin subcomponentes")) {
                            return !c.isTieneSubcomponentes();
                        }

                        return true;
                    })
                    .map(c -> new ComponenteFila(
                            c.getId(),
                            c.getDescripcion(),
                            c.getCantidad(),
                            c.getSubcomponentes(),
                            c.isTieneSubcomponentes()))
                    .collect(Collectors.toList());

            tabla.setItems(
                    FXCollections.observableArrayList(filas));

        } catch (SQLException ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de base de datos");
            alerta.setHeaderText(
                    "No se ha podido aplicar el filtro");
            alerta.setContentText(ex.getMessage());
            alerta.showAndWait();
        }
    }

    public ComponenteFila obtenerComponenteSeleccionado() {
        List<ComponenteFila> seleccionados = tabla.getItems()
                .stream()
                .filter(ComponenteFila::isSeleccionado)
                .toList();

        if (seleccionados.size() != 1) {
                return null;
        }

        return seleccionados.get(0);
    }

    public void recargarComponentes() {
        cargarComponentes();
    }
}