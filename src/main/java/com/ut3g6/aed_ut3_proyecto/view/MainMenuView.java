
package com.ut3g6.aed_ut3_proyecto.view;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuView extends Application {
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        Label titulo = new Label("Gestión de inventario");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitulo = new Label(
                "Sistema de gestión de artículos y almacenes");
        subtitulo.setStyle("-fx-font-size: 14px;");

        VBox cabecera = new VBox(8, titulo, subtitulo);
        cabecera.setAlignment(Pos.CENTER);
        cabecera.setPadding(new Insets(25));
        cabecera.setStyle("-fx-background-color: #243447; "
                + "-fx-text-fill: white;");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; "
                + "-fx-text-fill: white;");
        subtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Label textoMenu = new Label("Menú principal");
        textoMenu.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label instrucciones = new Label(
                "Selecciona el apartado que deseas gestionar.");

        GridPane opciones = new GridPane();
        opciones.setAlignment(Pos.CENTER);
        opciones.setHgap(20);
        opciones.setVgap(20);

        Button btnArticulos = crearBoton(
                "Artículos", "Consultar y gestionar artículos", FontAwesomeSolid.BOX);
        Button btnDepositos = crearBoton(
                "Depósitos", "Gestionar depósitos", FontAwesomeSolid.WAREHOUSE);
        Button btnEstantes = crearBoton(
                "Estantes", "Gestionar estantes", FontAwesomeSolid.TH_LARGE);
        Button btnComponentes = crearBoton(
                "Componentes", "Gestionar componentes", FontAwesomeSolid.COG);

        opciones.add(btnArticulos, 0, 0);
        opciones.add(btnDepositos, 1, 0);
        opciones.add(btnEstantes, 0, 1);
        opciones.add(btnComponentes, 1, 1);

        btnArticulos.setOnAction(e -> abrirAplicacion());
        btnDepositos.setOnAction(e -> abrirAplicacion());
        btnEstantes.setOnAction(e -> abrirAplicacion());
        btnComponentes.setOnAction(e -> abrirAplicacion());

        Label pie = new Label(
                "Aplicación de escritorio · Proyecto de acceso a datos");
        pie.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");

        VBox contenido = new VBox(15,
                textoMenu, instrucciones, opciones);
        contenido.setAlignment(Pos.CENTER);
        contenido.setPadding(new Insets(25));

        VBox raiz = new VBox(cabecera, contenido, pie);
        raiz.setAlignment(Pos.TOP_CENTER);
        raiz.setStyle("-fx-background-color: #F5F7FA;");
        VBox.setMargin(pie, new Insets(10, 10, 20, 10));

        Scene escena = new Scene(raiz, 750, 600);

        stage.setTitle("Gestión de inventario");
        stage.setScene(escena);
        stage.setMinWidth(650);
        stage.setMinHeight(500);
        stage.show();
    }

    private Button crearBoton(String titulo, String descripcion, FontAwesomeSolid simbolo) {
        FontIcon lblSimbolo = new FontIcon(simbolo);
        lblSimbolo.setIconSize(30);
        
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        Label lblDescripcion = new Label(descripcion);
        lblDescripcion.setStyle("-fx-font-size: 12px;");

        VBox contenido = new VBox(5, lblSimbolo, lblTitulo, lblDescripcion);
        contenido.setAlignment(Pos.CENTER);

        Button boton = new Button();
        boton.setGraphic(contenido);
        boton.setPrefSize(250, 130);

        boton.setStyle("-fx-background-color: white; "
                + "-fx-border-color: #D5DDE5; "
                + "-fx-border-radius: 6px; "
                + "-fx-background-radius: 6px; "
                + "-fx-cursor: hand;");

        return boton;
    }

    private void mostrarPendiente(String apartado) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Apartado pendiente");
        alerta.setHeaderText(apartado);
        alerta.setContentText("Esta vista todavía no está implementada.");
        alerta.showAndWait();
    }

    private void abrirAplicacion() {
        GeneralTemplateView plantilla = new GeneralTemplateView();

        stage.getScene().setRoot(plantilla);
    }

}
