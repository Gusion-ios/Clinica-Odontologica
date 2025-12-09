import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class PagoDAO {

    private final PacienteDAO pacienteDAO = new PacienteDAO();

    private void close(Connection cn) {
        try { if (cn != null) cn.close(); } catch (SQLException ignore) {}
    }

    private Pago mapResultSetToPago(ResultSet rs) throws SQLException {
        return new Pago(
                rs.getInt("idPago"),
                rs.getInt("dniPaciente"),
                rs.getTimestamp("fecha"),
                rs.getBoolean("estadoPagado"),
                rs.getString("metodo")
        );
    }

    public boolean insertarPago(Pago p) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "INSERT INTO Pago (dniPaciente, fecha, estadoPagado, metodo) VALUES (?, ?, ?, ?)";
        boolean exito = false;

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getDniPaciente());
            ps.setTimestamp(2, new Timestamp(p.getFecha().getTime()));
            ps.setBoolean(3, p.isEstadoPagado());
            ps.setString(4, p.getMetodo());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setIdPago(rs.getInt(1));
                    }
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar Pago: " + e.getMessage());
        } finally {
            close(cn);
        }
        return exito;
    }

    public Set<Pago> listarPagos() {
        Set<Pago> lista = new HashSet<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Pago ORDER BY fecha DESC";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToPago(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Pagos: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public Pago buscarPagoPorId(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return null;

        String sql = "SELECT * FROM Pago WHERE idPago = ?";
        Pago p = null;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = mapResultSetToPago(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Pago: " + e.getMessage());
        } finally {
            close(cn);
        }
        return p;
    }

    public Set<Pago> buscarPagoPorDNI(int dni) {
        Set<Pago> lista = new HashSet<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Pago WHERE dniPaciente = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToPago(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Pago por DNI: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public boolean actualizarEstadoPago(int id, boolean nuevoEstado) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Pago SET estadoPagado = ? WHERE idPago = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del Pago: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean eliminarPago(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "DELETE FROM Pago WHERE idPago = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar Pago: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }
}