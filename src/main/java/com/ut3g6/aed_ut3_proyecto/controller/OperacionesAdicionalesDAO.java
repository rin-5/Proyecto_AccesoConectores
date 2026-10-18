package com.ut3g6.aed_ut3_proyecto.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ut3g6.aed_ut3_proyecto.model.Conexion;

public class OperacionesAdicionalesDAO {

    public void insertarArticulo(
            String id,
            String nombre,
            BigDecimal precio,
            String codigo,
            int cantidad,
            String idEstante) throws SQLException {

        String sqlArticulo = """
            INSERT INTO articulo
                (id_articulo, nombre, precio, id_cod)
            VALUES (?, ?, ?, ?)
            """;

        String sqlAlmacen = """
            INSERT INTO almacenar_articulo
                (id_articulo, id_estanteria, cantidad_disponible)
            VALUES (?, ?, ?)
            """;

        try (Connection con = Conexion.getConexion()) {
            boolean autocommitAnterior = con.getAutoCommit();

            try {
                con.setAutoCommit(false);

                try (PreparedStatement ps =
                             con.prepareStatement(sqlArticulo)) {
                    ps.setString(1, id);
                    ps.setString(2, nombre);
                    ps.setBigDecimal(3, precio);

                    if (codigo == null || codigo.isBlank()) {
                        ps.setNull(4, java.sql.Types.CHAR);
                    } else {
                        ps.setString(4, codigo);
                    }

                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                             con.prepareStatement(sqlAlmacen)) {
                    ps.setString(1, id);
                    ps.setString(2, idEstante);
                    ps.setInt(3, cantidad);
                    ps.executeUpdate();
                }

                con.commit();

            } catch (SQLException ex) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    ex.addSuppressed(rollbackEx);
                }
                throw ex;

            } finally {
                con.setAutoCommit(autocommitAnterior);
            }
        }
    }

    public void modificarArticulo(
            String id,
            String nombre,
            BigDecimal precio,
            String codigo,
            int cantidad,
            String idEstante) throws SQLException {

        String sqlArticulo = """
            UPDATE articulo
            SET nombre = ?, precio = ?, id_cod = ?
            WHERE id_articulo = ?
            """;

        String sqlExisteAlmacen = """
            SELECT COUNT(*)
            FROM almacenar_articulo
            WHERE id_articulo = ?
            """;

        String sqlActualizarAlmacen = """
            UPDATE almacenar_articulo
            SET id_estanteria = ?, cantidad_disponible = ?
            WHERE id_articulo = ?
            """;

        String sqlInsertarAlmacen = """
            INSERT INTO almacenar_articulo
                (id_articulo, id_estanteria, cantidad_disponible)
            VALUES (?, ?, ?)
            """;

        try (Connection con = Conexion.getConexion()) {
            boolean autocommitAnterior = con.getAutoCommit();

            try {
                con.setAutoCommit(false);

                try (PreparedStatement ps =
                             con.prepareStatement(sqlArticulo)) {
                    ps.setString(1, nombre);
                    ps.setBigDecimal(2, precio);

                    if (codigo == null || codigo.isBlank()) {
                        ps.setNull(3, java.sql.Types.CHAR);
                    } else {
                        ps.setString(3, codigo);
                    }

                    ps.setString(4, id);

                    if (ps.executeUpdate() == 0) {
                        throw new SQLException(
                                "No se encontró el artículo " + id);
                    }
                }

                boolean existeAlmacen;

                try (PreparedStatement ps =
                             con.prepareStatement(sqlExisteAlmacen)) {
                    ps.setString(1, id);

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        existeAlmacen = rs.getInt(1) > 0;
                    }
                }

                if (existeAlmacen) {
                    try (PreparedStatement ps =
                                 con.prepareStatement(
                                         sqlActualizarAlmacen)) {
                        ps.setString(1, idEstante);
                        ps.setInt(2, cantidad);
                        ps.setString(3, id);
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps =
                                 con.prepareStatement(
                                         sqlInsertarAlmacen)) {
                        ps.setString(1, id);
                        ps.setString(2, idEstante);
                        ps.setInt(3, cantidad);
                        ps.executeUpdate();
                    }
                }

                con.commit();

            } catch (SQLException ex) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    ex.addSuppressed(rollbackEx);
                }
                throw ex;

            } finally {
                con.setAutoCommit(autocommitAnterior);
            }
        }
    }

    public String obtenerIdEstanteArticulo(String idArticulo)
        throws SQLException {

        String sql = """
            SELECT id_estanteria
            FROM almacenar_articulo
            WHERE id_articulo = ?
            LIMIT 1
            """;

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idArticulo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id_estanteria");
                }
            }
        }

        return null;
    }

    public void insertarDeposito(
        String descripcion,
        String direccion,
        String nombreEstante) throws SQLException {

        String sql = """
            INSERT INTO deposito
                (descripcion, direccion, nombre_estante)
            VALUES (?, ?, ?)
            """;

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, descripcion);
            ps.setString(2, direccion);
            ps.setString(3, nombreEstante);
            ps.executeUpdate();
        }
    }

    public void modificarDeposito(
            int id,
            String descripcion,
            String direccion,
            String nombreEstante) throws SQLException {

        String sql = """
            UPDATE deposito
            SET descripcion = ?, direccion = ?, nombre_estante = ?
            WHERE id_deposito = ?
            """;

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, descripcion);
            ps.setString(2, direccion);
            ps.setString(3, nombreEstante);
            ps.setInt(4, id);

            if (ps.executeUpdate() == 0) {
                throw new SQLException(
                        "No se encontró el depósito con ID " + id);
            }
        }
    }

    public void insertarEstante(
        String id,
        String nombre,
        int idDeposito) throws SQLException {

        String sql = """
            INSERT INTO estante
                (id_estante, nombre, id_deposito)
            VALUES (?, ?, ?)
            """;

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, nombre);
            ps.setInt(3, idDeposito);
            ps.executeUpdate();
        }
    }

    public void modificarEstante(
            String id,
            String nombre,
            int idDeposito) throws SQLException {

        String sql = """
            UPDATE estante
            SET nombre = ?, id_deposito = ?
            WHERE id_estante = ?
            """;

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, idDeposito);
            ps.setString(3, id);

            if (ps.executeUpdate() == 0) {
                throw new SQLException(
                        "No se encontró el estante con ID " + id);
            }
        }
    }

    public int obtenerIdDepositoEstante(String idEstante)
        throws SQLException {

        String sql = """
            SELECT id_deposito
            FROM estante
            WHERE id_estante = ?
            """;

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idEstante);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_deposito");
                }
            }
        }

        throw new SQLException(
                "No se encontró el estante con ID " + idEstante);
    }

    public void insertarComponente(
        String id,
        String descripcion,
        String idArticulo) throws SQLException {

        String sql = """
            INSERT INTO componente
                (id_componente, descripcion, id_art)
            VALUES (?, ?, ?)
            """;

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, descripcion);

            if (idArticulo == null || idArticulo.isBlank()) {
                ps.setNull(3, java.sql.Types.CHAR);
            } else {
                ps.setString(3, idArticulo);
            }

            ps.executeUpdate();
        }
    }

    public void modificarComponente(
            String id,
            String descripcion,
            String idArticulo) throws SQLException {

        String sql = """
            UPDATE componente
            SET descripcion = ?, id_art = ?
            WHERE id_componente = ?
            """;

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, descripcion);

            if (idArticulo == null || idArticulo.isBlank()) {
                ps.setNull(2, java.sql.Types.CHAR);
            } else {
                ps.setString(2, idArticulo);
            }

            ps.setString(3, id);

            if (ps.executeUpdate() == 0) {
                throw new SQLException(
                        "No se encontró el componente con ID " + id);
            }
        }
    }

    public String obtenerIdArticuloComponente(String idComponente)
        throws SQLException {

        String sql = """
            SELECT id_art
            FROM componente
            WHERE id_componente = ?
            """;

        try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idComponente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id_art");
                }
            }
        }

        throw new SQLException(
                "No se encontró el componente con ID " + idComponente);
    }
}
