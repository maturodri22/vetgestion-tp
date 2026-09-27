package vetgestion.dao;

import vetgestion.db.ConexionBD;
import vetgestion.model.Veterinario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDAO {

    public int insertar(Veterinario v) throws SQLException {
        String sql = "INSERT INTO veterinarios (nombre, matricula) VALUES (?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getNombre());
            ps.setString(2, v.getMatricula());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Veterinario> listarTodos() throws SQLException {
        String sql = "SELECT veterinario_id, nombre, matricula FROM veterinarios";
        List<Veterinario> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Veterinario(rs.getInt("veterinario_id"), rs.getString("nombre"), rs.getString("matricula")));
            }
        }
        return lista;
    }
}
