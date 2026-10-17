package com.ut3g6.aed_ut3_proyecto.view;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GeneralTemplateView extends BorderPane {

    private final BorderPane contenido;
    private final VBox subMenu;
    private final Label lblTitulo;
    private Button botonActivo;

    private final String colorBase = "#243447";
    private final String colorActivo = "#40566D";
    private final String colorHover = "#344A60";

    private final HBox zonaAcciones = new HBox(10);

    private final Button btnAnadir = new Button("Añadir");
    private final Button btnModificar = new Button("Modificar");
    private final Button btnEliminar = new Button("Eliminar");
    private final Button btnExportar = new Button("Exportar a CSV");

    public GeneralTemplateView() {
        // Cabecera del contenido
        lblTitulo = new Label();
        lblTitulo.setStyle("-fx-font-size: 24px; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: " + colorBase + ";");

        Label lblRuta = new Label("Sistema de gestión de inventario");
        lblRuta.setStyle("-fx-text-fill: #64748B;");

        VBox cabecera = new VBox(5, lblTitulo, lblRuta);
        cabecera.setPadding(new Insets(20));
        cabecera.setStyle("-fx-background-color: white;");

        // Menu lateral
        Label lblLogo = new Label("INVENTARIO");
        lblLogo.setStyle("-fx-font-size: 18px; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: white;");

        Button btnInicio = crearBoton(
                "Inicio", FontAwesomeSolid.HOME);

        Button btnInventario = crearBoton(
                "Inventario", FontAwesomeSolid.BOXES);

        subMenu = new VBox(5);
        subMenu.setPadding(new Insets(0, 0, 0, 15));

        Button btnArticulos = crearBoton(
                "Artículos", FontAwesomeSolid.BOX);

        Button btnDepositos = crearBoton(
                "Depósitos", FontAwesomeSolid.WAREHOUSE);

        Button btnEstantes = crearBoton(
                "Estantes", FontAwesomeSolid.TH_LARGE);

        Button btnComponentes = crearBoton(
                "Componentes", FontAwesomeSolid.COG);

        subMenu.getChildren().addAll(
                btnArticulos, btnDepositos,
                btnEstantes, btnComponentes);

        subMenu.setVisible(false);
        subMenu.setManaged(false);

        btnInicio.setOnAction(e ->
                mostrarContenido("Panel de control",
                        new StartView(), btnInicio));

        btnInventario.setOnAction(e -> {
            boolean mostrar = !subMenu.isVisible();
            subMenu.setVisible(mostrar);
            subMenu.setManaged(mostrar);
        });

        btnArticulos.setOnAction(e -> {
            ArticulosView vista =
                new ArticulosView(this::actualizarSeleccion);

            mostrarContenido(
            "Gestión de artículos", vista, btnArticulos);
        });

        btnDepositos.setOnAction(e -> {
            DepositosView vista =
                new DepositosView(this::actualizarSeleccion);
                
            mostrarContenido(
            "Gestión de depósitos", vista, btnDepositos);
        });

        btnEstantes.setOnAction(e -> {
            EstantesView vista =
                new EstantesView(this::actualizarSeleccion);

            mostrarContenido(
            "Gestión de estantes", vista, btnEstantes);
        });

        btnComponentes.setOnAction(e -> {
            ComponentesView vista =
                new ComponentesView(this::actualizarSeleccion);

            mostrarContenido(
            "Gestión de componentes", vista, btnComponentes);
        });

        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20, 12, 20, 12));
        menu.setPrefWidth(220);
        menu.setStyle("-fx-background-color: " + colorBase + ";");
        menu.getChildren().addAll(
                lblLogo, btnInicio, btnInventario, subMenu);

                
        // Zona central
        contenido = new BorderPane();
        crearZonaAcciones();
        VBox zonaSuperior = new VBox(cabecera, zonaAcciones);
        contenido.setTop(zonaSuperior);
        contenido.setStyle("-fx-background-color: #F5F7FA;");

        setLeft(menu);
        setCenter(contenido);
        setStyle("-fx-background-color: #F5F7FA;");

        mostrarContenido("Panel de control",
                new StartView(), btnInicio);
    }

    private void crearZonaAcciones() {
        zonaAcciones.setPadding(new Insets(10, 20, 10, 20));
        zonaAcciones.setAlignment(Pos.CENTER_LEFT);

        aplicarEstiloAccion(btnAnadir, colorBase);
        aplicarEstiloAccion(btnModificar, colorBase);
        aplicarEstiloAccion(btnEliminar, "#DC2626");
        aplicarEstiloAccion(btnExportar, colorBase);

        btnModificar.setVisible(false);
        btnModificar.setManaged(false);

        btnEliminar.setVisible(false);
        btnEliminar.setManaged(false);

        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);

        zonaAcciones.getChildren().addAll(
                btnAnadir, btnModificar, btnEliminar, btnExportar);

        zonaAcciones.setVisible(false);
        zonaAcciones.setManaged(false);
    }

    private Button crearBoton(
            String texto, FontAwesomeSolid simbolo) {

        FontIcon icono = new FontIcon(simbolo);
        icono.setIconSize(16);
        icono.setIconColor(javafx.scene.paint.Color.WHITE);

        Label etiqueta = new Label(texto);
        etiqueta.setStyle("-fx-text-fill: white;");

        HBox grafico = new HBox(12, icono, etiqueta);
        grafico.setAlignment(Pos.CENTER_LEFT);

        Button boton = new Button();
        boton.setGraphic(grafico);
        boton.setMaxWidth(Double.MAX_VALUE);
        boton.setAlignment(Pos.CENTER_LEFT);

        aplicarEstilo(boton, colorBase);

        boton.setOnMouseEntered(e -> {
            if (boton != botonActivo) {
                aplicarEstilo(boton, colorHover);
            }
        });

        boton.setOnMouseExited(e -> {
            if (boton != botonActivo) {
                aplicarEstilo(boton, colorBase);
            }
        });

        return boton;
    }

    public void mostrarContenido(
            String titulo, Node vista, Button boton) {
        if (botonActivo != null) {
            aplicarEstilo(botonActivo, colorBase);
        }

        botonActivo = boton;
        aplicarEstilo(botonActivo, colorActivo);

        lblTitulo.setText(titulo);
        contenido.setCenter(vista);

        if(vista instanceof ArticulosView){
            configurarAcciones(vista instanceof ArticulosView);
        }
        if(vista instanceof DepositosView){
            configurarAcciones(vista instanceof DepositosView);
        }
        if(vista instanceof EstantesView){
            configurarAcciones(vista instanceof EstantesView);
        }
        if(vista instanceof ComponentesView){
            configurarAcciones(vista instanceof ComponentesView);
        }
    }

    private void aplicarEstilo(Button boton, String color) {
        boton.setStyle(
                "-fx-background-color: " + color + "; "
                + "-fx-background-radius: 6px; "
                + "-fx-border-color: transparent; "
                + "-fx-text-fill: white; "
                + "-fx-padding: 10 12 10 12; "
                + "-fx-cursor: hand; "
                + "-fx-font-size: 14px;");
    }

    private void aplicarEstiloAccion(Button boton, String color) {
        boton.setStyle(
            "-fx-background-color: " + color + ";"
            + "-fx-text-fill: white;"
            + "-fx-background-radius: 6px;"
            + "-fx-padding: 3 9 3 9;"
            + "-fx-cursor: hand;"
            + "-fx-font-size: 13px;");
    }

    private void configurarAcciones(boolean mostrar) {
        zonaAcciones.setVisible(mostrar);
        zonaAcciones.setManaged(mostrar);

        btnModificar.setVisible(false);
        btnModificar.setManaged(false);

        btnEliminar.setVisible(false);
        btnEliminar.setManaged(false);

        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    public void actualizarSeleccion(boolean seleccionado) {
        btnModificar.setVisible(seleccionado);
        btnModificar.setManaged(seleccionado);

        btnEliminar.setVisible(seleccionado);
        btnEliminar.setManaged(seleccionado);

        btnModificar.setDisable(!seleccionado);
        btnEliminar.setDisable(!seleccionado);
    }
}