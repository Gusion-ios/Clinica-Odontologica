import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TratamientoDAO {

    private void close(Connection cn) {
        try { if (cn != null) cn.close(); } catch (SQLException ignore) {}
    }


    private PacienteDAO pacienteDAO = new PacienteDAO();
    private ProductoDAO productoDAO = new ProductoDAO();

    public boolean insertarTratamiento(Tratamiento t) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "INSERT INTO Tratamiento (motivo, estado, duracionSesiones, dniPaciente, idProducto) VALUES (?, ?, ?, ?, ?)";
        boolean exito = false;

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getMotivo());
            ps.setString(2, t.getEstado());
            ps.setInt(3, t.getDuracionSesiones());
            ps.setInt(4, t.getPaciente());
            ps.setInt(5, t.getProducto());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        t.setIdTratamiento(rs.getInt(1));
                    }
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar Tratamiento: " + e.getMessage());
        } finally {
            close(cn);
        }
        return exito;
    }

    public Tratamiento buscarPorId(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return null;

        String sql = "SELECT * FROM Tratamiento WHERE idTratamiento = ?";
        Tratamiento t = null;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    t = new Tratamiento(
                            rs.getInt("idTratamiento"),
                            rs.getString("motivo"),
                            rs.getString("estado"),
                            rs.getInt("duracionSesiones"),
                            rs.getInt("dniPaciente"),
                            rs.getInt("idProducto")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Tratamiento: " + e.getMessage());
        } finally {
            close(cn);
        }
        return t;
    }

    public List<Tratamiento> listarTratamientos() {
        List<Tratamiento> lista = new ArrayList<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Tratamiento";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tratamiento t = new Tratamiento(
                        rs.getInt("idTratamiento"),
                        rs.getString("motivo"),
                        rs.getString("estado"),
                        rs.getInt("duracionSesiones"),
                        rs.getInt("dniPaciente"),
                        rs.getInt("idProducto")
                );
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Tratamientos: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public boolean cambiarEstado(int idTratamiento, String nuevoEstado) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Tratamiento SET estado = ? WHERE idTratamiento = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idTratamiento);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de Tratamiento: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean actualizarDuracion(int idTratamiento, int nuevaDuracion) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Tratamiento SET duracionSesiones = ? WHERE idTratamiento = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, nuevaDuracion);
            ps.setInt(2, idTratamiento);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar duración: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean eliminarTratamiento(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "DELETE FROM Tratamiento WHERE idTratamiento = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar Tratamiento: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public List<Tratamiento> listarTratamientosPorPaciente(int dniPaciente) {
        List<Tratamiento> lista = new ArrayList<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Tratamiento WHERE dniPaciente = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, dniPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tratamiento t = new Tratamiento(
                            rs.getInt("idTratamiento"),
                            rs.getString("motivo"),
                            rs.getString("estado"),
                            rs.getInt("duracionSesiones"),
                            rs.getInt("dniPaciente"),
                            rs.getInt("idProducto")
                    );
                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar tratamientos por paciente: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }
}