import java.sql.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CitaDAO {

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final OdontologoDAO odontologoDAO = new OdontologoDAO();
    private final SalaDAO salaDAO = new SalaDAO();
    private final TratamientoDAO tratamientoDAO = new TratamientoDAO();
    private final HerramientaDAO herramientaDAO = new HerramientaDAO();

    private void close(Connection cn) {
        try { if (cn != null) cn.close(); } catch (SQLException ignore) {}
    }

    private Cita mapResultSetToCita(ResultSet rs) throws SQLException {

        int nCita = rs.getInt("nCita");
        Date fecha = rs.getTimestamp("fecha");
        int dniPaciente = rs.getInt("dniPaciente");
        int idOdontologo = rs.getInt("idOdontologo");
        int idTratamiento = rs.getInt("idTratamiento");
        int idHerramienta = rs.getInt("idHerramienta");
        int idSala = rs.getInt("idSala");

        Paciente paciente = pacienteDAO.buscarPorDNI(dniPaciente);
        Odontologo odontologo = odontologoDAO.buscarPorId(idOdontologo);
        Tratamiento tratamiento = tratamientoDAO.buscarPorId(idTratamiento);
        Herramienta herramienta = herramientaDAO.buscarPorId(idHerramienta);
        Sala sala = salaDAO.buscarPorId(idSala);

        if (paciente == null || odontologo == null || tratamiento == null || herramienta == null || sala == null) {
            System.err.println("Advertencia: No se pudo cargar la Cita ID " + nCita + " debido a una referencia FK faltante.");
            return null;
        }

        return new Cita(fecha, paciente, herramienta, tratamiento, sala, odontologo);
    }

    public boolean verificarDisponibilidad(Date fecha, int idSala, int idOdontologo) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "SELECT COUNT(*) FROM Cita WHERE fecha = ? AND (idSala = ? OR idOdontologo = ?)";
        boolean disponible = true;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(fecha.getTime()));
            ps.setInt(2, idSala);
            ps.setInt(3, idOdontologo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    disponible = false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar disponibilidad en DB: " + e.getMessage());
        } finally {
            close(cn);
        }
        return disponible;
    }

    public boolean insertarCita(Cita c) {

        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "INSERT INTO Cita (fecha, dniPaciente, idOdontologo, idTratamiento, idHerramienta, idSala) VALUES (?, ?, ?, ?, ?, ?)";
        boolean exito = false;

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, new Timestamp(c.getFecha().getTime()));
            ps.setInt(2, c.getPaciente().getDNIPaciente());
            ps.setInt(3, c.getOdontologo().getIdOdontologo());
            ps.setInt(4, c.getTratamiento().getIdTratamiento());
            ps.setInt(5, c.getHerramientas().getIdHerramienta());
            ps.setInt(6, c.getSala().getIdSala());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setnCita(rs.getInt(1));
                    }
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar Cita en DB: " + e.getMessage());
        } finally {
            close(cn);
        }
        return exito;
    }

    public Cita buscarCitaPorId(int nCita) {
        Connection cn = Conexion.conectar();
        if (cn == null) return null;

        String sql = "SELECT * FROM Cita WHERE nCita = ?";
        Cita cita = null;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, nCita);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cita = mapResultSetToCita(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Cita: " + e.getMessage());
        } finally {
            close(cn);
        }
        return cita;
    }

    public Set<Cita> listarCitas() {
        Set<Cita> lista = new HashSet<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Cita ORDER BY fecha DESC";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cita cita = mapResultSetToCita(rs);
                if (cita != null) {
                    lista.add(cita);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Citas: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public boolean actualizarFechaCita(int nCita, Date nuevaFecha) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Cita SET fecha = ? WHERE nCita = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(nuevaFecha.getTime()));
            ps.setInt(2, nCita);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar fecha de Cita: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean cancelarCita(int nCita) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "DELETE FROM Cita WHERE nCita = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, nCita);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cancelar Cita en DB: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public Set<Cita> listarCitasPorPaciente(int dniPaciente) {
        Set<Cita> lista = new HashSet<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Cita WHERE dniPaciente = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, dniPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita cita = mapResultSetToCita(rs);
                    if (cita != null) {
                        lista.add(cita);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Citas por Paciente: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }
}