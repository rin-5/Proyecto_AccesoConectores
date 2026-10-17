package com.ut3g6.aed_ut3_proyecto.view;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class StartView extends ScrollPane  {

    private final VBox contenido = new VBox();

    private final String colorBase = "#243447";
    private final String colorFondo = "#FFFFFF";

    private final GridPane tarjetas = new GridPane();
    private final GridPane accesos = new GridPane();

    private final List<VBox> listaTarjetas = new ArrayList<>();
    private final List<Button> listaAccesos = new ArrayList<>();

    private final Consumer<String> navegar;

    private static final double ANCHO_MINIMO = 1000;

    public StartView(Consumer<String> navegar) {
        this.navegar = navegar;

        contenido.setSpacing(25);
        contenido.setPadding(new Insets(20));
        contenido.setAlignment(Pos.TOP_LEFT);

        setContent(contenido);
        setFitToWidth(true);
        setFitToHeight(false);

        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

        setStyle(
                "-fx-background-color: #F5F7FA; "
                + "-fx-background: #F5F7FA;");

        // Tarjetas de información
        listaTarjetas.add(crearTarjeta(
                "Artículos registrados", "3", "#40566D"));
        listaTarjetas.add(crearTarjeta(
                "Depósitos", "2", "#2563EB"));
        listaTarjetas.add(crearTarjeta(
                "Estantes", "12", "#0F766E"));
        listaTarjetas.add(crearTarjeta(
                "Artículos con bajo stock", "1", "#DC2626"));

        // Accesos rápidos
        Label lblAccesos = new Label("Accesos rápidos");
        lblAccesos.setStyle(
                "-fx-font-size: 20px; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: " + colorBase + ";");

        listaAccesos.add(crearAcceso("Gestionar artículos", "Artículos"));
        listaAccesos.add(crearAcceso("Gestionar depósitos", "Depósitos"));
        listaAccesos.add(crearAcceso("Gestionar estantes", "Estantes"));
        listaAccesos.add(crearAcceso("Gestionar componentes", "Componentes"));

        contenido.getChildren().addAll(tarjetas, lblAccesos, accesos);

        // Adapta la distribución al ancho disponible.
        widthProperty().addListener((obs, anterior, actual) ->
                actualizarDistribucion(actual.doubleValue()));

        actualizarDistribucion(0);
    }

    private void actualizarDistribucion(double ancho) {
        int columnas = ancho >= ANCHO_MINIMO ? 4 : 2;

        organizarGrid(tarjetas, listaTarjetas, columnas);
        organizarGrid(accesos, listaAccesos, columnas);
    }

    private <T extends javafx.scene.Node> void organizarGrid(
            GridPane grid, List<T> elementos, int columnas) {

        grid.getChildren().clear();
        grid.getColumnConstraints().clear();

        grid.setHgap(20);
        grid.setVgap(20);
        grid.setMaxWidth(Double.MAX_VALUE);

        for (int i = 0; i < columnas; i++) {
            ColumnConstraints columna = new ColumnConstraints();
            columna.setPercentWidth(100.0 / columnas);
            columna.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(columna);
        }

        for (int i = 0; i < elementos.size(); i++) {
            T elemento = elementos.get(i);

            grid.add(elemento, i % columnas, i / columnas);
            GridPane.setHgrow(elemento, Priority.ALWAYS);
        }
    }

    private VBox crearTarjeta(String titulo, String valor,
                              String color) {

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle(
                "-fx-font-size: 14px; "
                + "-fx-text-fill: #64748B;");

        Label lblValor = new Label(valor);
        lblValor.setStyle(
                "-fx-font-size: 30px; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: " + color + ";");

        VBox tarjeta = new VBox(12, lblTitulo, lblValor);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        tarjeta.setMinHeight(120);
        tarjeta.setStyle(
                "-fx-background-color: " + colorFondo + "; "
                + "-fx-background-radius: 8px; "
                + "-fx-border-color: #E2E8F0; "
                + "-fx-border-radius: 8px;");

        return tarjeta;
    }

    private Button crearAcceso(String texto, String gestion) {
        Button boton = new Button(texto);
        boton.setMaxWidth(Double.MAX_VALUE);
        boton.setMinHeight(65);
        boton.setStyle(
                "-fx-background-color: white; "
                + "-fx-text-fill: " + colorBase + "; "
                + "-fx-font-size: 14px; "
                + "-fx-font-weight: bold; "
                + "-fx-background-radius: 8px; "
                + "-fx-border-color: #E2E8F0; "
                + "-fx-border-radius: 8px; "
                + "-fx-cursor: hand;");

        boton.setOnAction(e -> navegar.accept(gestion));

        return boton;
    }
}