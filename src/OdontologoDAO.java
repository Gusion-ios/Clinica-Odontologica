import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

public class OdontologoDAO {

    private void close(Connection cn) {
        try { if (cn != null) cn.close(); } catch (SQLException ignore) {}
    }

    public boolean existeOdontologo(String nombre, String apellido) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "SELECT COUNT(*) FROM Odontologo WHERE nombre = ? AND apellido = ?";
        boolean existe = false;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    existe = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de Odontólogo: " + e.getMessage());
        } finally {
            close(cn);
        }
        return existe;
    }

    public boolean insertarOdontologo(Odontologo o) {

        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "INSERT INTO Odontologo (nombre, apellido, especialidad, sedeMain) VALUES (?, ?, ?, ?)";
        boolean exito = false;

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, o.getNombre());
            ps.setString(2, o.getApellido());
            ps.setString(3, o.getEspecialidad());
            ps.setInt(4, o.getSedeMain());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        o.setIdOdontologo(rs.getInt(1));
                    }
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar Odontólogo: " + e.getMessage());
        } finally {
            close(cn);
        }
        return exito;
    }

    public Odontologo buscarPorId(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return null;

        String sql = "SELECT * FROM Odontologo WHERE idOdontologo = ?";
        Odontologo o = null;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    o = new Odontologo(
                            rs.getInt("idOdontologo"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("especialidad"),
                            rs.getInt("sedeMain")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Odontólogo: " + e.getMessage());
        } finally {
            close(cn);
        }
        return o;
    }

    public Set<Odontologo> listarOdontologos() {
        Set<Odontologo> lista = new HashSet<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT idOdontologo, nombre, apellido, especialidad, sedeMain FROM Odontologo";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Odontologo o = new Odontologo(
                        rs.getInt("idOdontologo"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("especialidad"),
                        rs.getInt("sedeMain")
                );
                lista.add(o);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Odontólogos: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public boolean editarOdontologo(Odontologo o) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Odontologo SET nombre=?, apellido=?, especialidad=?, sedeMain=? WHERE idOdontologo=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, o.getNombre());
            ps.setString(2, o.getApellido());
            ps.setString(3, o.getEspecialidad());
            ps.setInt(4, o.getSedeMain());
            ps.setInt(5, o.getIdOdontologo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar Odontólogo: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean eliminarOdontologo(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "DELETE FROM Odontologo WHERE idOdontologo = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar Odontólogo: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }
}