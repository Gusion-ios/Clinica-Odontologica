import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AlergiaDAO {

    private void close(Connection cn) {
        try { if (cn != null) cn.close(); } catch (SQLException ignore) {}
    }

    public boolean insertarAlergia(Alergia a) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "INSERT INTO Alergia (nombreAlergia, causas, consecuencias, Recomendacion) VALUES (?, ?, ?, ?)";
        boolean exito = false;

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNombreAlergia());
            ps.setString(2, a.getCausas());
            ps.setString(3, a.getConsecuencias());
            ps.setString(4, a.getRecomendacion());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        a.setIdAlergia(rs.getInt(1));
                    }
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar Alergia: " + e.getMessage());
        } finally {
            close(cn);
        }
        return exito;
    }

    public Set<Alergia> listarAlergias() {
        Set<Alergia> lista = new HashSet<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT idAlergia, nombreAlergia, causas, consecuencias, Recomendacion FROM Alergia";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Alergia a = new Alergia(
                        rs.getInt("idAlergia"),
                        rs.getString("nombreAlergia"),
                        rs.getString("causas"),
                        rs.getString("consecuencias"),
                        rs.getString("Recomendacion")
                );
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Alergias: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public Alergia buscarPorId(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return null;

        String sql = "SELECT * FROM Alergia WHERE idAlergia = ?";
        Alergia a = null;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    a = new Alergia(
                            rs.getInt("idAlergia"),
                            rs.getString("nombreAlergia"),
                            rs.getString("causas"),
                            rs.getString("consecuencias"),
                            rs.getString("Recomendacion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Alergia: " + e.getMessage());
        } finally {
            close(cn);
        }
        return a;
    }

    public boolean eliminarAlergia(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "DELETE FROM Alergia WHERE idAlergia = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar Alergia: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean editarAlergia(Alergia a) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Alergia SET nombreAlergia=?, causas=?, consecuencias=?, Recomendacion=? WHERE idAlergia=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, a.getNombreAlergia());
            ps.setString(2, a.getCausas());
            ps.setString(3, a.getConsecuencias());
            ps.setString(4, a.getRecomendacion());
            ps.setInt(5, a.getIdAlergia());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar Alergia: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }
}