package com.ut3g6.aed_ut3_proyecto.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.ut3g6.aed_ut3_proyecto.model.Conexion;

public class EliminarRegistrosDAO {
    
    public void eliminarArticulos(List<String> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        ejecutarTransaccion(conexion ->
            eliminarPorIds(conexion, "articulo", "id_articulo", ids)
        );
    }

    public void eliminarDepositos(List<Integer> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        ejecutarTransaccion(conexion -> {
            String sql =
                "DELETE FROM almacenar_articulo " +
                "WHERE id_estanteria IN (" +
                "SELECT id_estante FROM estante " +
                "WHERE id_deposito IN (" + placeholders(ids.size()) + "))";

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                enlazarIds(ps, ids);
                ps.executeUpdate();
            }

            eliminarPorIds(conexion, "deposito", "id_deposito", ids);
        });
    }

    public void eliminarEstantes(List<String> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        ejecutarTransaccion(conexion -> {
            eliminarPorIds(
                conexion, "almacenar_articulo", "id_estanteria", ids
            );

            eliminarPorIds(conexion, "estante", "id_estante", ids);
        });
    }

    public void eliminarComponentes(List<String> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        ejecutarTransaccion(conexion ->
            eliminarPorIds(conexion, "componente", "id_componente", ids)
        );
    }

    @FunctionalInterface
    private interface OperacionTransaccional {
        void ejecutar(Connection conexion) throws SQLException;
    }

    private void ejecutarTransaccion(OperacionTransaccional operacion)
            throws SQLException {

        try (Connection conexion = Conexion.getConexion()) {
            boolean autoCommitOriginal = conexion.getAutoCommit();
            conexion.setAutoCommit(false);

            try {
                operacion.ejecutar(conexion);
                conexion.commit();

            } catch (SQLException | RuntimeException ex) {
                try {
                    conexion.rollback();
                } catch (SQLException errorRollback) {
                    ex.addSuppressed(errorRollback);
                }
                throw ex;

            } finally {
                try {
                    conexion.setAutoCommit(autoCommitOriginal);
                } catch (SQLException ignored) {
                    // La conexión se cerrará al salir del try-with-resources.
                }
            }
        }
    }

    private static void eliminarPorIds(
            Connection conexion,
            String tabla,
            String columna,
            List<?> ids) throws SQLException {

        if (ids == null || ids.isEmpty()) {
            return;
        }

        String sql = "DELETE FROM " + tabla
                + " WHERE " + columna
                + " IN (" + placeholders(ids.size()) + ")";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            enlazarIds(ps, ids);
            ps.executeUpdate();
        }
    }

    private static String placeholders(int cantidad) {
        return String.join(
                ",",
                java.util.Collections.nCopies(cantidad, "?")
        );
    }

    private static void enlazarIds(
            PreparedStatement ps,
            List<?> ids) throws SQLException {

        for (int i = 0; i < ids.size(); i++) {
            ps.setObject(i + 1, ids.get(i));
        }
    }
}
