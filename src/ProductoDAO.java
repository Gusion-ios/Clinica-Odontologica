import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ProductoDAO {

    private void close(Connection cn) {
        try { if (cn != null) cn.close(); } catch (SQLException ignore) {}
    }

    public boolean existeProducto(String nombre) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "SELECT COUNT(*) FROM Producto WHERE nombre = ?";
        boolean existe = false;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    existe = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de Producto: " + e.getMessage());
        } finally {
            close(cn);
        }
        return existe;
    }

    public boolean insertarProducto(Producto p) {
        if (existeProducto(p.getNombre())) {
            System.err.println("Ya existe un producto con ese nombre.");
            return false;
        }

        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "INSERT INTO Producto (tipo, nombre, cantidad, precio, fechaProduccion, fechaVencimiento) VALUES (?, ?, ?, ?, ?, ?)";
        boolean exito = false;

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getTipo());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getCantidad());
            ps.setDouble(4, p.getPrecio());
            ps.setDate(5, new java.sql.Date(p.getFechaProduccion().getTime()));
            ps.setDate(6, new java.sql.Date(p.getFechaVencimiento().getTime()));

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setIdProducto(rs.getInt(1));
                    }
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar Producto: " + e.getMessage());
        } finally {
            close(cn);
        }
        return exito;
    }

    public Producto buscarPorId(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return null;

        String sql = "SELECT * FROM Producto WHERE idProducto = ?";
        Producto p = null;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Producto(
                            rs.getInt("idProducto"),
                            rs.getString("tipo"),
                            rs.getString("nombre"),
                            rs.getInt("cantidad"),
                            rs.getDouble("precio"),
                            rs.getDate("fechaProduccion"),
                            rs.getDate("fechaVencimiento")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Producto: " + e.getMessage());
        } finally {
            close(cn);
        }
        return p;
    }

    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        Connection cn = Conexion.conectar();
        if (cn == null) return lista;

        String sql = "SELECT * FROM Producto";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("tipo"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getDate("fechaProduccion"),
                        rs.getDate("fechaVencimiento")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Productos: " + e.getMessage());
        } finally {
            close(cn);
        }
        return lista;
    }

    public boolean editarProducto(Producto p) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Producto SET tipo=?, nombre=?, cantidad=?, precio=?, fechaProduccion=?, fechaVencimiento=? WHERE idProducto=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getTipo());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getCantidad());
            ps.setDouble(4, p.getPrecio());
            ps.setDate(5, new java.sql.Date(p.getFechaProduccion().getTime()));
            ps.setDate(6, new java.sql.Date(p.getFechaVencimiento().getTime()));
            ps.setInt(7, p.getIdProducto());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar Producto: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean eliminarProducto(int id) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "DELETE FROM Producto WHERE idProducto = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar Producto: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean reducirStock(int id, int cantidadAReducir) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Producto SET cantidad = cantidad - ? WHERE idProducto = ? AND cantidad >= ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, cantidadAReducir);
            ps.setInt(2, id);
            ps.setInt(3, cantidadAReducir);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reducir stock: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }

    public boolean incrementarStock(int id, int cantidadAumentar) {
        Connection cn = Conexion.conectar();
        if (cn == null) return false;

        String sql = "UPDATE Producto SET cantidad = cantidad + ? WHERE idProducto = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, cantidadAumentar);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al incrementar stock: " + e.getMessage());
            return false;
        } finally {
            close(cn);
        }
    }
}