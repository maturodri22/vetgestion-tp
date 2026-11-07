package vetgestion.mvc.modelo;

import vetgestion.db.ConexionBD;
import vetgestion.excepcion.PersistenciaException;

import java.sql.*;
import java.util.ArrayList;

/**
 * MODELO del patron MVC. Es el unico que conoce JDBC: abre conexiones, ejecuta
 * consultas parametrizadas y traduce las SQLException a PersistenciaException.
 * No imprime ni lee nada de pantalla.
 */
public class ModeloStock {

    /** Etiquetas de los niveles de stock (arreglo complementario al ArrayList). */
    public static final String[] NIVELES = {"Sin stock", "A reponer", "Suficiente"};

    private static final String SELECT_ITEMS =
            "SELECT insumo_id, nombre, tipo, cantidad_actual, cantidad_minima, fecha_vencimiento FROM insumos ";

    /** Consulta: todos los insumos, cargados en un ArrayList. */
    public ArrayList<ItemStock> listarTodos() throws PersistenciaException {
        return consultar(SELECT_ITEMS + "ORDER BY nombre", "No se pudo consultar el stock");
    }

    /** Consulta: insumos cuyo stock llego al minimo o lo supero hacia abajo. */
    public ArrayList<ItemStock> listarAReponer() throws PersistenciaException {
        return consultar(SELECT_ITEMS + "WHERE cantidad_actual <= cantidad_minima ORDER BY cantidad_actual",
                "No se pudo consultar los insumos a reponer");
    }

    private ArrayList<ItemStock> consultar(String sql, String contexto) throws PersistenciaException {
        ArrayList<ItemStock> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Date venc = rs.getDate("fecha_vencimiento");
                lista.add(new ItemStock(rs.getInt("insumo_id"), rs.getString("nombre"), rs.getString("tipo"),
                        rs.getInt("cantidad_actual"), rs.getInt("cantidad_minima"),
                        venc == null ? null : venc.toLocalDate()));
            }
        } catch (SQLException e) {
            throw traducir(contexto, e);
        }
        return lista;
    }

    /**
     * UPDATE: suma (ingreso) o resta (egreso) unidades al stock de un insumo y devuelve
     * la cantidad resultante. Si el stock quedaria negativo, MySQL rechaza la
     * modificacion por la restriccion CHECK y se informa con PersistenciaException.
     */
    public int ajustarStock(int insumoId, int delta) throws PersistenciaException {
        String leer = "SELECT cantidad_actual FROM insumos WHERE insumo_id = ?";
        String actualizar = "UPDATE insumos SET cantidad_actual = cantidad_actual + ? WHERE insumo_id = ?";
        try (Connection con = ConexionBD.obtenerConexion()) {
            int actual;
            try (PreparedStatement ps = con.prepareStatement(leer)) {
                ps.setInt(1, insumoId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new PersistenciaException("No existe un insumo con id " + insumoId + ".");
                    }
                    actual = rs.getInt(1);
                }
            }
            try (PreparedStatement ps = con.prepareStatement(actualizar)) {
                ps.setInt(1, delta);
                ps.setInt(2, insumoId);
                ps.executeUpdate();
            }
            return actual + delta;
        } catch (SQLException e) {
            throw traducir("No se pudo ajustar el stock del insumo " + insumoId, e);
        }
    }

    /** DELETE: elimina un insumo. Devuelve false si el id no existe. */
    public boolean eliminar(int insumoId) throws PersistenciaException {
        String sql = "DELETE FROM insumos WHERE insumo_id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, insumoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw traducir("No se pudo eliminar el insumo " + insumoId, e);
        }
    }

    /** Cuenta, con un arreglo int[], cuantos insumos hay en cada nivel de NIVELES. */
    public int[] distribucion(ArrayList<ItemStock> items) {
        int[] cantidades = new int[NIVELES.length];
        for (ItemStock item : items) {
            cantidades[item.getNivel()]++;
        }
        return cantidades;
    }

    /** Total de unidades en stock, calculado en memoria sobre el ArrayList. */
    public int totalUnidades(ArrayList<ItemStock> items) {
        int total = 0;
        for (ItemStock item : items) total += item.getCantidadActual();
        return total;
    }

    /** Convierte la SQLException en un mensaje de dominio segun su causa. */
    private PersistenciaException traducir(String contexto, SQLException e) {
        String estado = e.getSQLState() == null ? "" : e.getSQLState();
        String detalle;
        if (estado.startsWith("08")) {
            detalle = "no hay conexion con la base de datos (revisar que MySQL este activo y config.properties)";
        } else if (e.getErrorCode() == 3819 || e.getErrorCode() == 4025) {
            detalle = "el stock no puede quedar negativo";
        } else if (e.getErrorCode() == 1451) {
            detalle = "el insumo tiene vacunaciones registradas y no se puede eliminar";
        } else if (estado.startsWith("23")) {
            detalle = "se violo una restriccion de integridad";
        } else if (estado.startsWith("28")) {
            detalle = "usuario o contrasena de MySQL incorrectos";
        } else {
            detalle = e.getMessage();
        }
        return new PersistenciaException(contexto + ": " + detalle, e);
    }
}
