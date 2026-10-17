package com.ut3g6.aed_ut3_proyecto.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ut3g6.aed_ut3_proyecto.model.Conexion;

public class ConsultasAdicionalesDAO {

    private static final int UMBRAL_STOCK_BAJO = 10;

    public static class ArticuloInventario {
        private final String id;
        private final String nombre;
        private final double precio;
        private final String codigo;
        private final int cantidad;
        private final String estante;
        private final String deposito;
        private final String estado;

        public ArticuloInventario(
                String id, String nombre, double precio, String codigo,
                int cantidad, String estante, String deposito) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.codigo = codigo;
            this.cantidad = cantidad;
            this.estante = estante;
            this.deposito = deposito;

            if (cantidad <= 0) {
                this.estado = "Sin stock";
            } else if (cantidad <= UMBRAL_STOCK_BAJO) {
                this.estado = "Bajo stock";
            } else {
                this.estado = "En stock";
            }
        }

        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public double getPrecio() { return precio; }
        public String getCodigo() { return codigo; }
        public int getCantidad() { return cantidad; }
        public String getEstante() { return estante; }
        public String getDeposito() { return deposito; }
        public String getEstado() { return estado; }
    }

    public List<ArticuloInventario> listarArticulos()
            throws SQLException {

        List<ArticuloInventario> lista = new ArrayList<>();

        String sql = """
            SELECT
                a.id_articulo,
                a.nombre,
                a.precio,
                a.id_cod,
                COALESCE(SUM(aa.cantidad_disponible), 0) AS cantidad,
                COALESCE(
                    GROUP_CONCAT(DISTINCT e.nombre
                        ORDER BY e.nombre SEPARATOR ', '),
                    '-'
                ) AS estantes,
                COALESCE(
                    GROUP_CONCAT(DISTINCT d.descripcion
                        ORDER BY d.descripcion SEPARATOR ', '),
                    '-'
                ) AS depositos
            FROM articulo a
            LEFT JOIN almacenar_articulo aa
                ON aa.id_articulo = CAST(a.id_articulo AS CHAR)
            LEFT JOIN estante e
                ON e.id_estante = aa.id_estanteria
            LEFT JOIN deposito d
                ON d.id_deposito = e.id_deposito
            GROUP BY
                a.id_articulo, a.nombre, a.precio, a.id_cod
            ORDER BY a.id_articulo
            """;

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ArticuloInventario(
                    rs.getString("id_articulo"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getString("id_cod"),
                    rs.getInt("cantidad"),
                    rs.getString("estantes"),
                    rs.getString("depositos")
                ));
            }
        }

        return lista;
    }

    
    // Datos de un depósito junto con el número de estantes.
    public static class DepositoInventario {
        private final int id;
        private final String descripcion;
        private final String direccion;
        private final int estantes;

        public DepositoInventario(
                int id, String descripcion,
                String direccion, int estantes) {
            this.id = id;
            this.descripcion = descripcion;
            this.direccion = direccion;
            this.estantes = estantes;
        }

        public int getId() { return id; }
        public String getDescripcion() { return descripcion; }
        public String getDireccion() { return direccion; }
        public int getEstantes() { return estantes; }
    }

    // Datos de un estante junto con el nombre de su depósito.
    public static class EstanteInventario {
        private final String id;
        private final String nombre;
        private final String deposito;

        public EstanteInventario(
                String id, String nombre, String deposito) {
            this.id = id;
            this.nombre = nombre;
            this.deposito = deposito;
        }

        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public String getDeposito() { return deposito; }
    }

    public List<DepositoInventario> listarDepositos()
            throws SQLException {

        List<DepositoInventario> lista = new ArrayList<>();

        String sql = """
            SELECT
                d.id_deposito,
                d.descripcion,
                d.direccion,
                COUNT(e.id_estante) AS total_estantes
            FROM deposito d
            LEFT JOIN estante e
                ON e.id_deposito = d.id_deposito
            GROUP BY
                d.id_deposito, d.descripcion, d.direccion
            ORDER BY d.id_deposito
            """;

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new DepositoInventario(
                    rs.getInt("id_deposito"),
                    rs.getString("descripcion"),
                    rs.getString("direccion"),
                    rs.getInt("total_estantes")
                ));
            }
        }

        return lista;
    }

    public List<EstanteInventario> listarEstantes()
            throws SQLException {

        List<EstanteInventario> lista = new ArrayList<>();

        String sql = """
            SELECT
                e.id_estante,
                e.nombre,
                COALESCE(d.descripcion, '-') AS deposito
            FROM estante e
            LEFT JOIN deposito d
                ON d.id_deposito = e.id_deposito
            ORDER BY e.id_estante
            """;

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new EstanteInventario(
                    rs.getString("id_estante"),
                    rs.getString("nombre"),
                    rs.getString("deposito")
                ));
            }
        }

        return lista;
    }

    public List<ComponenteInventario> listarComponentesInventario()
        throws SQLException {

        List<ComponenteInventario> lista = new ArrayList<>();

        String sql = """
            SELECT
                c.id_componente,
                c.descripcion,
                COALESCE(SUM(cc.cantidad_componente), 0) AS cantidad,
                COUNT(cc.id_componente_secundario) AS num_subcomponentes,
                GROUP_CONCAT(
                    CONCAT(
                        COALESCE(
                            NULLIF(TRIM(cs.descripcion), ''),
                            TRIM(cs.id_componente)
                        ),
                        ' (', COALESCE(cc.cantidad_componente, 0), ')'
                    )
                    ORDER BY cs.id_componente
                    SEPARATOR ', '
                ) AS subcomponentes
            FROM componente c
            LEFT JOIN componer_componente cc
                ON c.id_componente = cc.id_componente_principal
            LEFT JOIN componente cs
                ON cs.id_componente = cc.id_componente_secundario
            GROUP BY c.id_componente, c.descripcion
            ORDER BY c.id_componente
            """;

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String subcomponentes = rs.getString("subcomponentes");

                if (subcomponentes == null || subcomponentes.isBlank()) {
                    subcomponentes = "Ninguno";
                }

                lista.add(new ComponenteInventario(
                        rs.getString("id_componente").trim(),
                        rs.getString("descripcion"),
                        rs.getInt("cantidad"),
                        subcomponentes,
                        rs.getInt("num_subcomponentes") > 0
                ));
            }
        }

        return lista;
    }

    public static class ComponenteInventario {

        private final String id;
        private final String descripcion;
        private final int cantidad;
        private final String subcomponentes;
        private final boolean tieneSubcomponentes;

        public ComponenteInventario(
                String id,
                String descripcion,
                int cantidad,
                String subcomponentes,
                boolean tieneSubcomponentes) {
            this.id = id;
            this.descripcion = descripcion;
            this.cantidad = cantidad;
            this.subcomponentes = subcomponentes;
            this.tieneSubcomponentes = tieneSubcomponentes;
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
    }
}

