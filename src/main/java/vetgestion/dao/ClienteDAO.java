package vetgestion.dao;

import vetgestion.db.ConexionBD;
import vetgestion.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public int insertar(Cliente c) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, apellido, telefono, email) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT cliente_id, nombre, apellido, telefono, email FROM clientes";
        List<Cliente> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Cliente(rs.getInt("cliente_id"), rs.getString("nombre"),
                        rs.getString("apellido"), rs.getString("telefono"), rs.getString("email")));
            }
        }
        return lista;
    }
}
