
package com.ut3g6.aed_ut3_proyecto.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public class GeneralTemplateView extends BorderPane {

    private final BorderPane contenido;
    private final VBox subMenu;
    private final Label lblTitulo;
    private Button botonActivo;

    private final String colorBase = "#243447";
    private final String colorActivo = "#40566D";
    private final String colorHover = "#344A60";

    //private final Button btnInventario;//

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

        btnArticulos.setOnAction(e ->
                mostrarContenido("Gestión de artículos",
                        new ArticulosView(), btnArticulos));

        btnDepositos.setOnAction(e ->
                mostrarContenido("Gestión de depósitos",
                        new DepositosView(), btnDepositos));

        btnEstantes.setOnAction(e ->
                mostrarContenido("Gestión de estantes",
                        new EstantesView(), btnEstantes));

        btnComponentes.setOnAction(e ->
                mostrarContenido("Gestión de componentes",
                        new ComponentesView(), btnComponentes));

        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20, 12, 20, 12));
        menu.setPrefWidth(220);
        menu.setStyle("-fx-background-color: " + colorBase + ";");
        menu.getChildren().addAll(
                lblLogo, btnInicio, btnInventario, subMenu);

        // Zona central
        contenido = new BorderPane();
        contenido.setTop(cabecera);
        contenido.setStyle("-fx-background-color: #F5F7FA;");

        setLeft(menu);
        setCenter(contenido);
        setStyle("-fx-background-color: #F5F7FA;");

        mostrarContenido("Panel de control",
                new StartView(), btnInicio);
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
}
