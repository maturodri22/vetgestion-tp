package vetgestion.dao;

import vetgestion.db.ConexionBD;
import vetgestion.model.Turno;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TurnoDAO {

    /** Verifica disponibilidad (regla de negocio: un veterinario no puede tener dos turnos en el mismo horario). */
    public boolean estaDisponible(int veterinarioId, LocalDate fecha, LocalTime hora) throws SQLException {
        String sql = "SELECT COUNT(*) AS ocupados FROM turnos " +
                "WHERE veterinario_id = ? AND fecha = ? AND hora = ? AND estado <> 'cancelado'";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, veterinarioId);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setTime(3, Time.valueOf(hora));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("ocupados") == 0;
            }
        }
        return false;
    }

    public int insertar(Turno t) throws SQLException {
        String sql = "INSERT INTO turnos (mascota_id, veterinario_id, fecha, hora, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getMascotaId());
            ps.setInt(2, t.getVeterinarioId());
            ps.setDate(3, Date.valueOf(t.getFecha()));
            ps.setTime(4, Time.valueOf(t.getHora()));
            ps.setString(5, t.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void actualizarEstado(int turnoId, String nuevoEstado) throws SQLException {
        String sql = "UPDATE turnos SET estado = ? WHERE turno_id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, turnoId);
            ps.executeUpdate();
        }
    }

    /** Turnos agendados en una fecha especifica (Consulta 1 del informe). */
    public List<String> turnosPorFecha(LocalDate fecha) throws SQLException {
        String sql = "SELECT m.nombre AS mascota, c.nombre AS cliente, v.nombre AS veterinario, t.hora, t.estado " +
                "FROM turnos t " +
                "JOIN mascotas m ON t.mascota_id = m.mascota_id " +
                "JOIN clientes c ON m.cliente_id = c.cliente_id " +
                "JOIN veterinarios v ON t.veterinario_id = v.veterinario_id " +
                "WHERE t.fecha = ?";
        List<String> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(rs.getString("mascota") + " | " + rs.getString("cliente")
                            + " | " + rs.getString("veterinario") + " | " + rs.getTime("hora")
                            + " | " + rs.getString("estado"));
                }
            }
        }
        return resultado;
    }
}
