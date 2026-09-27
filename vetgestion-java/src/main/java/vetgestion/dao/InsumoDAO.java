package vetgestion.dao;

import vetgestion.db.ConexionBD;
import vetgestion.model.Insumo;
import vetgestion.model.Medicamento;
import vetgestion.model.Vacuna;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsumoDAO {

    public int insertarVacuna(Vacuna v) throws SQLException {
        String sql = "INSERT INTO insumos (nombre, tipo, cantidad_actual, cantidad_minima, fecha_vencimiento, " +
                "enfermedad_prevenida, dosis_requeridas) VALUES (?, 'vacuna', ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getNombre());
            ps.setInt(2, v.getCantidadActual());
            ps.setInt(3, v.getCantidadMinima());
            ps.setDate(4, v.getFechaVencimiento() != null ? Date.valueOf(v.getFechaVencimiento()) : null);
            ps.setString(5, v.getEnfermedadPrevenida());
            ps.setInt(6, v.getDosisRequeridas());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public int insertarMedicamento(Medicamento m) throws SQLException {
        String sql = "INSERT INTO insumos (nombre, tipo, cantidad_actual, cantidad_minima, " +
                "principio_activo, requiere_receta) VALUES (?, 'medicamento', ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNombre());
            ps.setInt(2, m.getCantidadActual());
            ps.setInt(3, m.getCantidadMinima());
            ps.setString(4, m.getPrincipioActivo());
            ps.setBoolean(5, m.isRequiereReceta());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void actualizarCantidad(int insumoId, int nuevaCantidad) throws SQLException {
        String sql = "UPDATE insumos SET cantidad_actual = ? WHERE insumo_id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, insumoId);
            ps.executeUpdate();
        }
    }

    public Insumo buscarPorId(int insumoId) throws SQLException {
        String sql = "SELECT * FROM insumos WHERE insumo_id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, insumoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<Insumo> listarTodos() throws SQLException {
        String sql = "SELECT * FROM insumos";
        List<Insumo> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /** Insumos en stock minimo (Consulta 3 del informe, RF08). */
    public List<Insumo> listarEnStockMinimo() throws SQLException {
        List<Insumo> lista = new ArrayList<>();
        for (Insumo i : listarTodos()) {
            if (i.estaEnStockMinimo()) lista.add(i);
        }
        return lista;
    }

    private Insumo mapear(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        Date venc = rs.getDate("fecha_vencimiento");
        Insumo insumo;
        if ("vacuna".equals(tipo)) {
            insumo = new Vacuna(rs.getString("nombre"), rs.getInt("cantidad_actual"), rs.getInt("cantidad_minima"),
                    venc != null ? venc.toLocalDate() : null,
                    rs.getString("enfermedad_prevenida"), rs.getInt("dosis_requeridas"));
        } else {
            insumo = new Medicamento(rs.getString("nombre"), rs.getInt("cantidad_actual"), rs.getInt("cantidad_minima"),
                    venc != null ? venc.toLocalDate() : null,
                    rs.getString("principio_activo"), rs.getBoolean("requiere_receta"));
        }
        insumo.setInsumoId(rs.getInt("insumo_id"));
        return insumo;
    }
}
