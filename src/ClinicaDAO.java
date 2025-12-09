import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClinicaDAO {

    private void close(Connection cn) {
        try { if (cn != null) cn.close(); } catch (SQLException ignore) {}
    }

    public boolean insertarClinica(Clinica c) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "INSERT INTO Clinica (nombre, RUC, telefono, direccion) VALUES (?, ?, ?, ?)";
        boolean exito = false;

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getRUC());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getDireccion());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setIdClinica(rs.getInt(1));
                    }
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar Clínica: " + e.getMessage());
        } finally {
            close(cn);
        }
        return exito;
    }

    public List<Clinica> listarClinicas() {
        List<Clinica> lista = new ArrayList<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT idClinica, nombre, RUC, telefono, direccion FROM Clinica";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Clinica c = new Clinica(
                        rs.getString("nombre"),
                        rs.getString("RUC"),
                        rs.getString("telefono"),
                        rs.getString("direccion")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Clínicas: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public Clinica buscarPorId(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return null;

        String sql = "SELECT * FROM Clinica WHERE idClinica = ?";
        Clinica c = null;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new Clinica(
                            rs.getString("nombre"),
                            rs.getString("RUC"),
                            rs.getString("telefono"),
                            rs.getString("direccion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Clínica: " + e.getMessage());
        } finally {
            close(cn);
        }
        return c;
    }

    public boolean editarClinica(Clinica c) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Clinica SET nombre=?, RUC=?, telefono=?, direccion=? WHERE idClinica=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getRUC());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getDireccion());
            ps.setInt(5, c.getIdClinica());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar Clínica: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean eliminarClinica(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "DELETE FROM Clinica WHERE idClinica = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar Clínica: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }
}