package vetgestion.dao;

import vetgestion.db.ConexionBD;
import vetgestion.model.Mascota;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAO {

    public int insertar(Mascota m) throws SQLException {
        String sql = "INSERT INTO mascotas (cliente_id, nombre, especie, raza, fecha_nacimiento) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getClienteId());
            ps.setString(2, m.getNombre());
            ps.setString(3, m.getEspecie());
            ps.setString(4, m.getRaza());
            if (m.getFechaNacimiento() != null) {
                ps.setDate(5, Date.valueOf(m.getFechaNacimiento()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Mascota> listarPorCliente(int clienteId) throws SQLException {
        String sql = "SELECT mascota_id, cliente_id, nombre, especie, raza, fecha_nacimiento " +
                "FROM mascotas WHERE cliente_id = ?";
        List<Mascota> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public Mascota buscarPorId(int mascotaId) throws SQLException {
        String sql = "SELECT mascota_id, cliente_id, nombre, especie, raza, fecha_nacimiento " +
                "FROM mascotas WHERE mascota_id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mascotaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    private Mascota mapear(ResultSet rs) throws SQLException {
        Date fn = rs.getDate("fecha_nacimiento");
        return new Mascota(rs.getInt("mascota_id"), rs.getInt("cliente_id"), rs.getString("nombre"),
                rs.getString("especie"), rs.getString("raza"), fn != null ? fn.toLocalDate() : null);
    }
}
