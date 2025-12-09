import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class HerramientaDAO {

    private void close(Connection cn) {
        try { if (cn != null) cn.close(); } catch (SQLException ignore) {}
    }

    public boolean existeHerramienta(String nombre) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "SELECT COUNT(*) FROM Herramienta WHERE nombre = ?";
        boolean existe = false;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    existe = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia: " + e.getMessage());
        } finally {
            close(cn);
        }
        return existe;
    }

    public boolean insertarHerramienta(Herramienta h) {
        if (existeHerramienta(h.getNombre())) {
            System.out.println("Ya existe una herramienta con ese nombre.");
            return false;
        }

        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "INSERT INTO Herramienta (tipo, nombre, estado, cantidad, fechaAdqusicion) VALUES (?, ?, ?, ?, ?)";
        boolean exito = false;

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, h.getTipo());
            ps.setString(2, h.getNombre());
            ps.setBoolean(3, h.isEstado());
            ps.setInt(4, h.getCantidad());
            ps.setDate(5, new java.sql.Date(h.getFechaAdqusicion().getTime()));

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        h.setIdHerramienta(rs.getInt(1));
                    }
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar Herramienta: " + e.getMessage());
        } finally {
            close(cn);
        }
        return exito;
    }

    public Herramienta buscarPorId(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return null;

        String sql = "SELECT * FROM Herramienta WHERE idHerramienta = ?";
        Herramienta h = null;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    h = new Herramienta(
                            rs.getInt("idHerramienta"),
                            rs.getString("tipo"),
                            rs.getString("nombre"),
                            rs.getBoolean("estado"),
                            rs.getInt("cantidad"),
                            rs.getDate("fechaAdqusicion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Herramienta: " + e.getMessage());
        } finally {
            close(cn);
        }
        return h;
    }

    public boolean usarHerramienta(int id) {
        String sql = "UPDATE Herramienta SET cantidad = cantidad - 1 WHERE idHerramienta = ? AND cantidad > 0 AND estado = 1";

        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al usar herramienta: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean reponerHerramienta(int id, int cantidadAReponer) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Herramienta SET cantidad = cantidad + ? WHERE idHerramienta = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, cantidadAReponer);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reponer herramienta: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public Set<Herramienta> listarHerramientas() {
        Set<Herramienta> lista = new HashSet<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT idHerramienta, tipo, nombre, estado, cantidad, fechaAdqusicion FROM Herramienta";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Herramienta h = new Herramienta(
                        rs.getInt("idHerramienta"),
                        rs.getString("tipo"),
                        rs.getString("nombre"),
                        rs.getBoolean("estado"),
                        rs.getInt("cantidad"),
                        rs.getDate("fechaAdqusicion")
                );
                lista.add(h);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Herramientas: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public boolean editarHerramienta(Herramienta h) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Herramienta SET tipo=?, nombre=?, estado=?, cantidad=? WHERE idHerramienta=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, h.getTipo());
            ps.setString(2, h.getNombre());
            ps.setBoolean(3, h.isEstado());
            ps.setInt(4, h.getCantidad());
            ps.setInt(5, h.getIdHerramienta());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar Herramienta: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean eliminarHerramienta(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "DELETE FROM Herramienta WHERE idHerramienta = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar Herramienta: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }
}
