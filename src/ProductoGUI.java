import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class ProductoGUI extends JFrame {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private JTable tablaProductos;
    private JTextField txtTipo;
    private JTextField txtNombre;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JTextField txtFechaProd;
    private JTextField txtFechaVenc;

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    public ProductoGUI() {
        setTitle("5. Gestión de Productos (JDBC)");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(crearTablaProductos()), BorderLayout.CENTER);
        add(crearPanelAcciones(), BorderLayout.SOUTH);

        cargarDatosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 10, 10));

        txtTipo = new JTextField(10);
        txtNombre = new JTextField(15);
        txtCantidad = new JTextField(5);
        txtPrecio = new JTextField(5);
        txtFechaProd = new JTextField("01/01/2025");
        txtFechaVenc = new JTextField("01/01/2026");

        panel.add(new JLabel("Tipo:"));
        panel.add(txtTipo);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);

        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);
        panel.add(new JLabel("Precio:"));
        panel.add(txtPrecio);

        panel.add(new JLabel("F. Producción (DD/MM/YYYY):"));
        panel.add(txtFechaProd);
        panel.add(new JLabel("F. Vencimiento (DD/MM/YYYY):"));
        panel.add(txtFechaVenc);

        JButton btnRegistrar = new JButton("Registrar Producto");
        btnRegistrar.addActionListener(e -> registrarNuevoProducto());
        panel.add(btnRegistrar);

        return panel;
    }

    private JTable crearTablaProductos() {
        String[] columnas = {"ID", "Tipo", "Nombre", "Cantidad", "Precio", "F. Producción", "F. Vencimiento"};
        tablaProductos = new JTable(new DefaultTableModel(new Object[0][0], columnas));
        return tablaProductos;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarDatosTabla());
        panel.add(btnActualizar);

        JButton btnEditar = new JButton("Editar Producto");
        btnEditar.addActionListener(e -> editarProductoSeleccionado());
        panel.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar Producto");
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatosTabla() {
        try {
            List<Producto> productos = productoDAO.listarProductos();
            String[] columnas = {"ID", "Tipo", "Nombre", "Cantidad", "Precio", "F. Producción", "F. Vencimiento"};
            Object[][] datos = new Object[productos.size()][columnas.length];

            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                datos[i][0] = p.getIdProducto();
                datos[i][1] = p.getTipo();
                datos[i][2] = p.getNombre();
                datos[i][3] = p.getCantidad();
                datos[i][4] = p.getPrecio();
                datos[i][5] = FORMATO_FECHA.format(p.getFechaProduccion());
                datos[i][6] = FORMATO_FECHA.format(p.getFechaVencimiento());
            }

            tablaProductos.setModel(new DefaultTableModel(datos, columnas));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevoProducto() {
        try {
            String tipo = txtTipo.getText();
            String nombre = txtNombre.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            double precio = Double.parseDouble(txtPrecio.getText());

            Date fechaProd = FORMATO_FECHA.parse(txtFechaProd.getText());
            Date fechaVenc = FORMATO_FECHA.parse(txtFechaVenc.getText());

            Producto nuevo = new Producto(tipo, nombre, cantidad, precio, fechaProd, fechaVenc);

            if (productoDAO.insertarProducto(nuevo)) {
                JOptionPane.showMessageDialog(this, "Producto registrado con ID: " + nuevo.getIdProducto(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
                txtTipo.setText(""); txtNombre.setText(""); txtCantidad.setText("");
                txtPrecio.setText(""); txtFechaProd.setText("01/01/2025"); txtFechaVenc.setText("01/01/2026");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad y Precio deben ser valores numéricos válidos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "El formato de fecha debe ser DD/MM/YYYY.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idProdStr = tablaProductos.getValueAt(filaSeleccionada, 0).toString();
            int idProducto = Integer.parseInt(idProdStr);
            Producto prodAEditar = productoDAO.buscarPorId(idProducto);

            if (prodAEditar != null) {
                String nuevoTipo = JOptionPane.showInputDialog(this, "Nuevo Tipo:", prodAEditar.getTipo());
                String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo Nombre:", prodAEditar.getNombre());
                String nuevaCantidadStr = JOptionPane.showInputDialog(this, "Nueva Cantidad:", String.valueOf(prodAEditar.getCantidad()));
                String nuevoPrecioStr = JOptionPane.showInputDialog(this, "Nuevo Precio:", String.valueOf(prodAEditar.getPrecio()));

                if (nuevoTipo != null && nuevoNombre != null && nuevaCantidadStr != null && nuevoPrecioStr != null) {

                    prodAEditar.setTipo(nuevoTipo);
                    prodAEditar.setNombre(nuevoNombre);
                    prodAEditar.setCantidad(Integer.parseInt(nuevaCantidadStr));
                    prodAEditar.setPrecio(Double.parseDouble(nuevoPrecioStr));

                    if (productoDAO.editarProducto(prodAEditar)) {
                        JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarDatosTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad y Precio deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al editar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idProdStr = tablaProductos.getValueAt(filaSeleccionada, 0).toString();
        int idProducto = Integer.parseInt(idProdStr);
        String nombre = (String) tablaProductos.getValueAt(filaSeleccionada, 2);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el producto '" + nombre + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (productoDAO.eliminarProducto(idProducto)) {
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar. Podría estar asociado a un Tratamiento (Integridad Referencial).",
                        "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}