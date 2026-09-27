package vetgestion.dao;

import vetgestion.db.ConexionBD;
import vetgestion.model.ConsultaMedica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    public int insertar(ConsultaMedica c) throws SQLException {
        String sql = "INSERT INTO consultas (mascota_id, veterinario_id, fecha, diagnostico, tratamiento, peso, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getMascotaId());
            ps.setInt(2, c.getVeterinarioId());
            ps.setDate(3, Date.valueOf(c.getFecha()));
            ps.setString(4, c.getDiagnostico());
            ps.setString(5, c.getTratamiento());
            ps.setDouble(6, c.getPeso());
            ps.setString(7, c.getObservaciones());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            // Puede fallar por chk_peso_rango si el peso esta fuera de (0, 200]
            throw e;
        }
        return -1;
    }

    /** Historial clinico completo de una mascota (Consulta 2 del informe). */
    public List<String> historialPorMascota(int mascotaId) throws SQLException {
        String sql = "SELECT c.fecha, v.nombre AS veterinario, c.diagnostico, c.tratamiento, c.peso " +
                "FROM consultas c " +
                "JOIN veterinarios v ON c.veterinario_id = v.veterinario_id " +
                "WHERE c.mascota_id = ? ORDER BY c.fecha DESC";
        List<String> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mascotaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(rs.getDate("fecha") + " | " + rs.getString("veterinario")
                            + " | " + rs.getString("diagnostico") + " | " + rs.getString("tratamiento")
                            + " | peso: " + rs.getDouble("peso") + "kg");
                }
            }
        }
        return resultado;
    }
}
