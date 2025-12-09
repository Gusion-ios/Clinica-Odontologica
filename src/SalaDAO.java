import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class SalaDAO {

    private void close(Connection cn) {
        try { if (cn != null) cn.close(); } catch (SQLException ignore) {}
    }

    public boolean insertarSala(Sala s) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "INSERT INTO Sala (tipo, capacidad, estadoLibre, ubicacion, sede) VALUES (?, ?, ?, ?, ?)";
        boolean exito = false;

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getTipo());
            ps.setInt(2, s.getCapacidad());
            ps.setBoolean(3, s.isEstadoLibre());
            ps.setString(4, s.getUbicacion());
            ps.setInt(5, s.getSede());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        s.setIdSala(rs.getInt(1));
                    }
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar Sala: " + e.getMessage());
        } finally {
            close(cn);
        }
        return exito;
    }

    public Sala buscarPorId(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return null;

        String sql = "SELECT * FROM Sala WHERE idSala = ?";
        Sala s = null;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    s = new Sala(
                            rs.getInt("idSala"),
                            rs.getString("tipo"),
                            rs.getInt("capacidad"),
                            rs.getBoolean("estadoLibre"),
                            rs.getString("ubicacion"),
                            rs.getInt("sede")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Sala: " + e.getMessage());
        } finally {
            close(cn);
        }
        return s;
    }

    public Set<Sala> listarSalas() {
        Set<Sala> lista = new HashSet<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Sala";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Sala s = new Sala(
                        rs.getInt("idSala"),
                        rs.getString("tipo"),
                        rs.getInt("capacidad"),
                        rs.getBoolean("estadoLibre"),
                        rs.getString("ubicacion"),
                        rs.getInt("sede")
                );
                lista.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Salas: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public boolean editarSala(Sala s) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Sala SET tipo=?, capacidad=?, ubicacion=?, sede=? WHERE idSala=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, s.getTipo());
            ps.setInt(2, s.getCapacidad());
            ps.setString(3, s.getUbicacion());
            ps.setInt(4, s.getSede());
            ps.setInt(5, s.getIdSala());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar Sala: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean cambiarEstado(int idSala, boolean nuevoEstado) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Sala SET estadoLibre = ? WHERE idSala = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idSala);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de la Sala: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean eliminarSala(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "DELETE FROM Sala WHERE idSala = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar Sala: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }
}