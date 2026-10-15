package com.ut3g6.aed_ut3_proyecto.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ArticulosView extends VBox {

    public ArticulosView() {

        setAlignment(Pos.TOP_LEFT);
        setSpacing(15);
        setPadding(new Insets(20));

        // Filtros
        ComboBox<String> filtro = new ComboBox<>();
        filtro.getItems().addAll(
                "Todo", "En stock", "Ordenar pronto", "Sin/Bajo stock");
        filtro.setValue("Todo");

        Button btnBuscar = new Button("Buscar");
        HBox zonaFiltros = new HBox(10, filtro, btnBuscar);
        zonaFiltros.setAlignment(Pos.CENTER_LEFT);

        // Tabla
        TableView<ArticuloFila> tabla = new TableView<>();

        TableColumn<ArticuloFila, Boolean> colSeleccion =
                new TableColumn<>("");
        colSeleccion.setCellValueFactory(
                new PropertyValueFactory<>("seleccionado"));
        colSeleccion.setCellFactory(col ->
                new javafx.scene.control.TableCell<>() {
                    private final CheckBox checkBox = new CheckBox();

                    {
                        checkBox.setOnAction(e -> {
                            ArticuloFila fila = getTableRow().getItem();
                            if (fila != null) {
                                fila.setSeleccionado(checkBox.isSelected());
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

        // Datos provisionales
        tabla.setItems(FXCollections.observableArrayList(
                new ArticuloFila(1, "Tornillo", 0.15,
                        "TOR001", 250, "E-01", "D-01", "En stock"),
                new ArticuloFila(2, "Tuerca", 0.10,
                        "TUE001", 80, "E-02", "D-01", "En stock"),
                new ArticuloFila(3, "Motor", 45.50,
                        "MOT001", 5, "E-03", "D-02", "Bajo stock")
        ));

        // Seleccionar todos
        CheckBox seleccionarTodos = new CheckBox("Seleccionar todo");
        seleccionarTodos.setOnAction(e -> {
            for (ArticuloFila articulo : tabla.getItems()) {
                articulo.setSeleccionado(seleccionarTodos.isSelected());
            }
            tabla.refresh();
        });

        // Botones de acciones
        Button btnModificar = new Button("Modificar");
        Button btnEliminar = new Button("Eliminar");

        HBox zonaAcciones = new HBox(
                10, seleccionarTodos, btnModificar, btnEliminar);
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

        VBox.setVgrow(tabla, javafx.scene.layout.Priority.ALWAYS);
    }

    public static class ArticuloFila {

        private final int id;
        private final String nombre;
        private final double precio;
        private final String codigo;
        private final int cantidad;
        private final String estante;
        private final String deposito;
        private final String estado;
        private boolean seleccionado;

        public ArticuloFila(int id, String nombre, double precio,
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

        public int getId() { return id; }
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
}