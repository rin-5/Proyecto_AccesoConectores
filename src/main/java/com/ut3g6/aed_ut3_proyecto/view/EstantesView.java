package com.ut3g6.aed_ut3_proyecto.view;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.ut3g6.aed_ut3_proyecto.controller.ConsultasAdicionalesDAO;
import com.ut3g6.aed_ut3_proyecto.controller.ConsultasAdicionalesDAO.EstanteInventario;

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

public class EstantesView extends VBox {

    private final Consumer<Boolean> cambioSeleccion;

    private final ConsultasAdicionalesDAO consultas =
        new ConsultasAdicionalesDAO();

    private final TableView<EstanteFila> tabla = new TableView<>();
    private final ComboBox<String> filtro = new ComboBox<>();
    private List<EstanteInventario> datos = List.of();

    public EstantesView(Consumer<Boolean> cambioSeleccion) {
        this.cambioSeleccion = cambioSeleccion;

        setAlignment(Pos.TOP_LEFT);
        setSpacing(15);
        setPadding(new Insets(20));

        // Filtros
        filtro.getItems().addAll(
                "Todo", "Depósito principal", "Depósito secundario");
        filtro.setValue("Todo");

        Button btnBuscar = new Button("Buscar");

        HBox zonaFiltros = new HBox(10, filtro, btnBuscar);
        zonaFiltros.setAlignment(Pos.CENTER_LEFT);

        // Tabla
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

        TableColumn<EstanteFila, String> colId =
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

        // carga de datos
        cargarEstantes();
        btnBuscar.setOnAction(e -> aplicarFiltro());

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

        private final String id;
        private final String nombre;
        private final String deposito;
        private boolean seleccionado;

        public EstanteFila(String id, String nombre, String deposito) {
            this.id = id;
            this.nombre = nombre;
            this.deposito = deposito;
            this.seleccionado = false;
        }

        public String getId() {
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

    
    private void cargarEstantes() {
        try {
            datos = consultas.listarEstantes();

            filtro.getItems().clear();
            filtro.getItems().add("Todo");

            datos.stream()
                .map(EstanteInventario::getDeposito)
                .filter(nombre -> !nombre.equals("-"))
                .distinct()
                .sorted()
                .forEach(filtro.getItems()::add);

            filtro.setValue("Todo");
            aplicarFiltro();

        } catch (SQLException ex) {
            mostrarError("No se han podido cargar los estantes", ex);
        }
    }

    private void aplicarFiltro() {
        String seleccion = filtro.getValue();

        List<EstanteFila> filas = datos.stream()
            .filter(e -> seleccion == null
                    || seleccion.equals("Todo")
                    || seleccion.equals(e.getDeposito()))
            .map(e -> new EstanteFila(
                    e.getId(),
                    e.getNombre(),
                    e.getDeposito()))
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

}