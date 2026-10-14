
package com.ut3g6.aed_ut3_proyecto.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class GeneralTemplateView extends BorderPane {

    private final BorderPane contenido;
    private final VBox subMenu;
    private final Button btnInventario;

    public GeneralTemplateView() {

        Label titulo = new Label("Gestión de inventario");
        titulo.setStyle("-fx-font-size: 22px; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: white;");

        HBox cabecera = new HBox(titulo);
        cabecera.setAlignment(Pos.CENTER_LEFT);
        cabecera.setPadding(new Insets(20));
        cabecera.setStyle("-fx-background-color: #243447;");

        Button btnInicio = new Button("Inicio");
        btnInventario = new Button("Inventario");

        btnInicio.setMaxWidth(Double.MAX_VALUE);
        btnInventario.setMaxWidth(Double.MAX_VALUE);

        subMenu = new VBox(5);
        subMenu.setPadding(new Insets(0, 0, 0, 15));

        Button btnArticulos = new Button("Artículos");
        Button btnDepositos = new Button("Depósitos");
        Button btnEstantes = new Button("Estantes");
        Button btnComponentes = new Button("Componentes");

        Button[] botones = {
            btnArticulos, btnDepositos, btnEstantes, btnComponentes
        };

        for (Button boton : botones) {
            boton.setMaxWidth(Double.MAX_VALUE);
            subMenu.getChildren().add(boton);
        }

        subMenu.setVisible(false);
        subMenu.setManaged(false);

        btnInicio.setOnAction(e ->
                mostrarContenido(new InicioView()));

        btnInventario.setOnAction(e -> {
            boolean mostrar = !subMenu.isVisible();
            subMenu.setVisible(mostrar);
            subMenu.setManaged(mostrar);
        });

        btnArticulos.setOnAction(e ->
                mostrarContenido(new ArticulosView()));

        btnDepositos.setOnAction(e ->
                mostrarContenido(new DepositosView()));

        btnEstantes.setOnAction(e ->
                mostrarContenido(new EstantesView()));

        btnComponentes.setOnAction(e ->
                mostrarContenido(new ComponentesView()));

        VBox menu = new VBox(10, btnInicio, btnInventario, subMenu);
        menu.setPadding(new Insets(15));
        menu.setPrefWidth(220);
        menu.setStyle("-fx-background-color: #E8EEF5;");

        contenido = new BorderPane();
        contenido.setPadding(new Insets(20));

        setTop(cabecera);
        setLeft(menu)
        setCenter(contenido);
        setStyle("-fx-background-color: #F5F7FA;");

        mostrarContenido(new InicioView());
    }

    public void mostrarContenido(Node vista) {
        contenido.setCenter(vista);
    }
}
