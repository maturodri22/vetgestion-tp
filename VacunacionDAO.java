package vetgestion.dao;

import vetgestion.db.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VacunacionDAO {

    public int registrar(int consultaId, int insumoId, LocalDate fechaAplicacion, LocalDate fechaRefuerzo) throws SQLException {
        String sql = "INSERT INTO vacunaciones (consulta_id, insumo_id, fecha_aplicacion, fecha_refuerzo) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, consultaId);
            ps.setInt(2, insumoId);
            ps.setDate(3, Date.valueOf(fechaAplicacion));
            ps.setDate(4, fechaRefuerzo != null ? Date.valueOf(fechaRefuerzo) : null);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    /** Vacunas aplicadas a una mascota, con proxima fecha de refuerzo (Consulta 4 del informe). */
    public List<String> historialPorMascota(int mascotaId) throws SQLException {
        String sql = "SELECT m.nombre AS mascota, i.nombre AS vacuna, vc.fecha_aplicacion, vc.fecha_refuerzo " +
                "FROM vacunaciones vc " +
                "JOIN consultas c ON vc.consulta_id = c.consulta_id " +
                "JOIN mascotas m ON c.mascota_id = m.mascota_id " +
                "JOIN insumos i ON vc.insumo_id = i.insumo_id " +
                "WHERE m.mascota_id = ?";
        List<String> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mascotaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(rs.getString("mascota") + " | " + rs.getString("vacuna")
                            + " | aplicada: " + rs.getDate("fecha_aplicacion")
                            + " | refuerzo: " + rs.getDate("fecha_refuerzo"));
                }
            }
        }
        return resultado;
    }
}
