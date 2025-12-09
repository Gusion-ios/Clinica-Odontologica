import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class PacienteDAO {

    private void close(Connection cn) {
        try { if (cn != null) cn.close(); } catch (SQLException ignore) {}
    }

    public boolean insertarPaciente(Paciente p) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "INSERT INTO Paciente (dniPaciente, nombres, apellidos, edad, genero, telefono, idAlergia) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, p.getDNIPaciente());
            ps.setString(2, p.getNombres());
            ps.setString(3, p.getApellidos());
            ps.setInt(4, p.getEdad());
            ps.setString(5, p.getGenero());
            ps.setInt(6, p.getTelefono());

            if (p.getIdAlergia() != 0) {
                ps.setInt(7, p.getIdAlergia());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                System.err.println("ERROR SQL: El DNI ya está registrado (Clave duplicada).");
            } else {
                System.err.println("Error al registrar Paciente: " + e.getMessage());
            }
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean eliminarPaciente(int dni) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "DELETE FROM Paciente WHERE dniPaciente = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, dni);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                System.err.println("ERROR: No se puede eliminar. El paciente tiene registros asociados (citas, tratamientos).");
            } else {
                System.err.println("Error al eliminar Paciente: " + e.getMessage());
            }
            return false;
        } finally {
            close(cn);
        }
    }

    public Paciente buscarPorDNI(int dni) {
        Connection cn = Conexion.conectar();
        if (cn == null) return null;

        String sql = "SELECT * FROM Paciente WHERE dniPaciente = ?";
        Paciente p = null;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Paciente(
                            rs.getInt("dniPaciente"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getInt("edad"),
                            rs.getString("genero"),
                            rs.getInt("telefono"),
                            rs.getInt("idAlergia")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Paciente: " + e.getMessage());
        } finally {
            close(cn);
        }
        return p;
    }

    public boolean modificarPaciente(Paciente p) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Paciente SET nombres=?, apellidos=?, edad=?, genero=?, telefono=?, idAlergia=? WHERE dniPaciente=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getNombres());
            ps.setString(2, p.getApellidos());
            ps.setInt(3, p.getEdad());
            ps.setString(4, p.getGenero());
            ps.setInt(5, p.getTelefono());

            if (p.getIdAlergia() != 0) {
                ps.setInt(6, p.getIdAlergia());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.setInt(7, p.getDNIPaciente());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar Paciente: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public Set<Paciente> listarPacientes() {
        Set<Paciente> lista = new HashSet<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Paciente";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente p = new Paciente(
                        rs.getInt("dniPaciente"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getInt("edad"),
                        rs.getString("genero"),
                        rs.getInt("telefono"),
                        rs.getInt("idAlergia")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Pacientes: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public int contarPacientes() {
        Connection cn = Conexion.conectar();
        if (cn == null) return 0;

        String sql = "SELECT COUNT(*) FROM Paciente";
        int total = 0;

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar Pacientes: " + e.getMessage());
        } finally {
            close(cn);
        }
        return total;
    }
}