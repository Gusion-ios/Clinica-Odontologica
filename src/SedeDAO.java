import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SedeDAO {

    private void close(Connection cn) {
        try { if (cn != null) cn.close(); } catch (SQLException ignore) {}
    }

    private Sede mapResultSetToSede(ResultSet rs) throws SQLException {
        return new Sede(
                rs.getInt("idSede"),
                rs.getInt("capacidad"),
                rs.getBoolean("disponible"),
                rs.getString("ubicacion"),
                rs.getInt("idClinica")
        );
    }

    public boolean insertarSede(Sede s) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "INSERT INTO Sede (capacidad, disponible, ubicacion, idClinica) VALUES (?, ?, ?, ?)";
        boolean exito = false;

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getCapacidad());
            ps.setBoolean(2, s.isDisponible());
            ps.setString(3, s.getUbicacion());
            ps.setInt(4, s.getIdClinica());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        s.setIdSede(rs.getInt(1));
                    }
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar Sede: " + e.getMessage());
        } finally {
            close(cn);
        }
        return exito;
    }

    public Sede buscarPorId(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return null;

        String sql = "SELECT * FROM Sede WHERE idSede = ?";
        Sede s = null;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    s = mapResultSetToSede(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Sede: " + e.getMessage());
        } finally {
            close(cn);
        }
        return s;
    }

    public List<Sede> listarSedes() {
        List<Sede> lista = new ArrayList<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Sede";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToSede(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Sedes: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public boolean editarSede(Sede s) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Sede SET capacidad=?, ubicacion=?, idClinica=? WHERE idSede=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, s.getCapacidad());
            ps.setString(2, s.getUbicacion());
            ps.setInt(3, s.getIdClinica());
            ps.setInt(4, s.getIdSede());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar Sala: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean cambiarDisponibilidad(int idSede, boolean nuevoEstado) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Sede SET disponible = ? WHERE idSede = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idSede);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de Sede: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean eliminarSede(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "DELETE FROM Sede WHERE idSede = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar Sede: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public int contarSedesPorClinica(int idClinica) {
        Connection cn = Conexion.conectar();
        if (cn == null) return 0;

        String sql = "SELECT COUNT(*) FROM Sede WHERE idClinica = ?";
        int contador = 0;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idClinica);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    contador = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al contar sedes: " + e.getMessage());
        } finally {
            close(cn);
        }
        return contador;
    }

    public List<Sede> listarSedesDisponibles() {
        List<Sede> lista = new ArrayList<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Sede WHERE disponible = 1";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToSede(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar sedes disponibles: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public List<Sede> listarPorClinica(int idClinica) {
        List<Sede> lista = new ArrayList<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Sede WHERE idClinica = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idClinica);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToSede(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Sedes por Clínica: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public List<Sede> buscarPorUbicacion(String ubicacion) {
        List<Sede> lista = new ArrayList<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Sede WHERE ubicacion LIKE ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, "%" + ubicacion + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToSede(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por Ubicación: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public List<Sede> listarPorCapacidadMinima(int capacidadMinima) {
        List<Sede> lista = new ArrayList<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Sede WHERE capacidad >= ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, capacidadMinima);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToSede(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar por Capacidad Mínima: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public int contarSedes() {
        Connection cn = Conexion.conectar();
        if (cn == null) return 0;
        String sql = "SELECT COUNT(*) FROM Sede";
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error contando sedes: " + e.getMessage());
        } finally {
            close(cn);
        }
        return 0;
    }

    public int contarDisponibles() {
        Connection cn = Conexion.conectar();
        if (cn == null) return 0;
        String sql = "SELECT COUNT(*) FROM Sede WHERE disponible = 1";
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error contando disponibles: " + e.getMessage());
        } finally {
            close(cn);
        }
        return 0;
    }

    public int sumarCapacidadTotal() {
        Connection cn = Conexion.conectar();
        if (cn == null) return 0;
        String sql = "SELECT SUM(capacidad) FROM Sede";
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error sumando capacidad: " + e.getMessage());
        } finally {
            close(cn);
        }
        return 0;
    }
}