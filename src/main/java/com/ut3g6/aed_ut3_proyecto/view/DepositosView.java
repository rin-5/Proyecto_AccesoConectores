package com.ut3g6.aed_ut3_proyecto.view;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.ut3g6.aed_ut3_proyecto.controller.ConsultasAdicionalesDAO;
import com.ut3g6.aed_ut3_proyecto.controller.ConsultasAdicionalesDAO.DepositoInventario;

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

public class DepositosView extends VBox {

    private final Consumer<Boolean> cambioSeleccion;

    private final ConsultasAdicionalesDAO consultas =
            new ConsultasAdicionalesDAO();

    private final TableView<DepositoFila> tabla = new TableView<>();
    private final ComboBox<String> filtro = new ComboBox<>();
    private List<DepositoInventario> datos = List.of();

    public DepositosView(Consumer<Boolean> cambioSeleccion) {
        this.cambioSeleccion = cambioSeleccion;

        setAlignment(Pos.TOP_LEFT);
        setSpacing(15);
        setPadding(new Insets(20));

        // Filtros
        filtro.getItems().addAll("Todo", "Con estantes", "Sin estantes");
        filtro.setValue("Todo");

        Button btnBuscar = new Button("Buscar");

        HBox zonaFiltros = new HBox(10, filtro, btnBuscar);
        zonaFiltros.setAlignment(Pos.CENTER_LEFT);

        // Tabla
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

        cargarDepositos();
        btnBuscar.setOnAction(e -> aplicarFiltro());

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

    
    private void cargarDepositos() {
        try {
            datos = consultas.listarDepositos();

            filtro.getItems().setAll(
                    "Todo", "Con estantes", "Sin estantes");
            filtro.setValue("Todo");

            aplicarFiltro();

        } catch (SQLException ex) {
            mostrarError("No se han podido cargar los depósitos", ex);
        }
    }

    private void aplicarFiltro() {
        String seleccion = filtro.getValue();

        List<DepositoFila> filas = datos.stream()
            .filter(d -> {
                if (seleccion == null || seleccion.equals("Todo")) {
                    return true;
                }

                if (seleccion.equals("Con estantes")) {
                    return d.getEstantes() > 0;
                }

                if (seleccion.equals("Sin estantes")) {
                    return d.getEstantes() == 0;
                }

                return true;
            })
            .map(d -> new DepositoFila(
                    d.getId(),
                    d.getDescripcion(),
                    d.getDireccion(),
                    d.getEstantes()))
            .collect(Collectors.toList());

        tabla.setItems(FXCollections.observableArrayList(filas));
    }

    private void mostrarError(String mensaje, SQLException ex) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error de base de datos");
        alerta.setHeaderText(mensaje);
        alerta.setContentText(ex.getMessage());
        alerta.showAndWait();
    }

    public DepositoFila obtenerDepositoSeleccionado() {
        List<DepositoFila> seleccionados = tabla.getItems()
                .stream()
                .filter(DepositoFila::isSeleccionado)
                .toList();

        if (seleccionados.size() != 1) {
            return null;
        }

        return seleccionados.get(0);
    }

    public void recargarDepositos() {
        cargarDepositos();
    }

    public List<Integer> obtenerIdsSeleccionados() {
        return tabla.getItems().stream()
                .filter(DepositoFila::isSeleccionado)
                .map(DepositoFila::getId)
                .collect(java.util.stream.Collectors.toList());
    }
}