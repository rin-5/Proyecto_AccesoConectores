package com.ut3g6.aed_ut3_proyecto.view;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.ut3g6.aed_ut3_proyecto.controller.ConsultasAdicionalesDAO;
import com.ut3g6.aed_ut3_proyecto.controller.ConsultasAdicionalesDAO.ArticuloInventario;
import com.ut3g6.aed_ut3_proyecto.view.ArticulosView.ArticuloFila;
import com.ut3g6.aed_ut3_proyecto.view.ComponentesView.ComponenteFila;

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

public class ArticulosView extends VBox {

    private final Consumer<Boolean> cambioSeleccion;

    private final TableView<ArticuloFila> tabla = new TableView<>();
    private final ComboBox<String> filtro = new ComboBox<>();
    private final ConsultasAdicionalesDAO consultas =
            new ConsultasAdicionalesDAO();

    public ArticulosView(Consumer<Boolean> cambioSeleccion) {
        this.cambioSeleccion = cambioSeleccion;
        
        setAlignment(Pos.TOP_LEFT);
        setSpacing(15);
        setPadding(new Insets(20));

        // Filtros
        filtro.getItems().addAll(
                "Todo", "En stock", "Ordenar pronto", "Sin/Bajo stock");
        filtro.setValue("Todo");

        Button btnBuscar = new Button("Buscar");

        HBox zonaFiltros = new HBox(10, filtro, btnBuscar);
        zonaFiltros.setAlignment(Pos.CENTER_LEFT);

        // Tabla
        TableColumn<ArticuloFila, Boolean> colSeleccion =
                new TableColumn<>("");

        colSeleccion.setCellValueFactory(
                new PropertyValueFactory<>("seleccionado"));

        colSeleccion.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    ArticuloFila fila = getTableRow().getItem();
                    if (fila != null) {
                        fila.setSeleccionado(checkBox.isSelected());
                        cambioSeleccion.accept(
                            tabla.getItems().stream()
                                .anyMatch(ArticuloFila::isSeleccionado));
                    }
                });
            }

            @Override
            protected void updateItem(Boolean seleccionado,
                                        boolean vacio) {
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

        TableColumn<ArticuloFila, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        
        TableColumn<ArticuloFila, String> colNombre =
                new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));

        TableColumn<ArticuloFila, Double> colPrecio =
                new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio"));

        TableColumn<ArticuloFila, String> colCodigo =
                new TableColumn<>("Código");
        colCodigo.setCellValueFactory(
                new PropertyValueFactory<>("codigo"));

        TableColumn<ArticuloFila, Integer> colCantidad =
                new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(
                new PropertyValueFactory<>("cantidad"));

        TableColumn<ArticuloFila, String> colEstante =
                new TableColumn<>("Estante");
        colEstante.setCellValueFactory(
                new PropertyValueFactory<>("estante"));

        TableColumn<ArticuloFila, String> colDeposito =
                new TableColumn<>("Depósito");
        colDeposito.setCellValueFactory(
                new PropertyValueFactory<>("deposito"));

        TableColumn<ArticuloFila, String> colEstado =
                new TableColumn<>("Estado");
        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado"));

        tabla.getColumns().addAll(
                colSeleccion, colId, colNombre, colPrecio,
                colCodigo, colCantidad, colEstante,
                colDeposito, colEstado);

        tabla.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Carga inicial desde la base de datos
        cargarArticulos();

        // Aplicar el filtro seleccionado
        btnBuscar.setOnAction(e -> aplicarFiltro());

        // Seleccionar todos
        CheckBox seleccionarTodos = new CheckBox("Seleccionar todo");
        seleccionarTodos.setOnAction(e -> {
            for (ArticuloFila articulo : tabla.getItems()) {
                articulo.setSeleccionado(seleccionarTodos.isSelected());
            }
            tabla.refresh();
            cambioSeleccion.accept(
                tabla.getItems().stream()
                    .anyMatch(ArticuloFila::isSeleccionado));
        });

        HBox zonaAcciones = new HBox(
                10, seleccionarTodos);
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


    public static class ArticuloFila {

        private final String id;
        private final String nombre;
        private final double precio;
        private final String codigo;
        private final int cantidad;
        private final String estante;
        private final String deposito;
        private final String estado;
        private boolean seleccionado;

        public ArticuloFila(String id, String nombre, double precio,
                            String codigo, int cantidad, String estante,
                            String deposito, String estado) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.codigo = codigo;
            this.cantidad = cantidad;
            this.estante = estante;
            this.deposito = deposito;
            this.estado = estado;
            this.seleccionado = false;
        }

        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public double getPrecio() { return precio; }
        public String getCodigo() { return codigo; }
        public int getCantidad() { return cantidad; }
        public String getEstante() { return estante; }
        public String getDeposito() { return deposito; }
        public String getEstado() { return estado; }
        public boolean isSeleccionado() { return seleccionado; }
        public void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
        }
    }

    
    private void cargarArticulos() {
        try {
            List<ArticuloInventario> articulos =
                    consultas.listarArticulos();

            tabla.setItems(FXCollections.observableArrayList(
                articulos.stream()
                    .map(a -> new ArticuloFila(
                        a.getId(),
                        a.getNombre(),
                        a.getPrecio(),
                        a.getCodigo(),
                        a.getCantidad(),
                        a.getEstante(),
                        a.getDeposito(),
                        a.getEstado()
                    ))
                    .collect(Collectors.toList())
            ));

            aplicarFiltro();

        } catch (SQLException ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de base de datos");
            alerta.setHeaderText("No se han podido cargar los artículos");
            alerta.setContentText(ex.getMessage());
            alerta.showAndWait();
        }
    }

    private void aplicarFiltro() {
        try {
            List<ArticuloInventario> articulos =
                    consultas.listarArticulos();

            String estadoSeleccionado = filtro.getValue();

            List<ArticuloFila> filas = articulos.stream()
                .filter(a -> {
                    if (estadoSeleccionado == null
                            || estadoSeleccionado.equals("Todo")) {
                        return true;
                    }

                    if (estadoSeleccionado.equals("En stock")) {
                        return a.getEstado().equals("En stock");
                    }

                    if (estadoSeleccionado.equals("Ordenar pronto")) {
                        return a.getEstado().equals("Bajo stock");
                    }

                    if (estadoSeleccionado.equals("Sin/Bajo stock")) {
                        return a.getEstado().equals("Sin stock")
                                || a.getEstado().equals("Bajo stock");
                    }

                    return true;
                })
                .map(a -> new ArticuloFila(
                    a.getId(),
                    a.getNombre(),
                    a.getPrecio(),
                    a.getCodigo(),
                    a.getCantidad(),
                    a.getEstante(),
                    a.getDeposito(),
                    a.getEstado()
                ))
                .collect(Collectors.toList());

            tabla.setItems(FXCollections.observableArrayList(filas));

        } catch (SQLException ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de base de datos");
            alerta.setHeaderText("No se ha podido aplicar el filtro");
            alerta.setContentText(ex.getMessage());
            alerta.showAndWait();
        }
    }

}