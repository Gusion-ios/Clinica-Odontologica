import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HerramientaGUI extends JFrame {

    private final HerramientaDAO herramientaDAO = new HerramientaDAO();
    private JTable tablaHerramientas;
    private JTextField txtTipo;
    private JTextField txtNombre;
    private JTextField txtCantidad;
    private JComboBox<String> cmbEstado;

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    public HerramientaGUI() {
        setTitle("9. Gestión de Herramientas (JDBC)");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(crearTablaHerramientas()), BorderLayout.CENTER);
        add(crearPanelAcciones(), BorderLayout.SOUTH);

        cargarDatosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));

        txtTipo = new JTextField(15);
        txtNombre = new JTextField(20);
        txtCantidad = new JTextField(5);
        cmbEstado = new JComboBox<>(new String[]{"Funcional (True)", "No Funcional (False)"});

        panel.add(new JLabel("Tipo:"));
        panel.add(txtTipo);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);

        panel.add(new JLabel("Cantidad Inicial:"));
        panel.add(txtCantidad);
        panel.add(new JLabel("Estado Inicial:"));
        panel.add(cmbEstado);

        JButton btnRegistrar = new JButton("Registrar Herramienta");
        btnRegistrar.addActionListener(e -> registrarNuevaHerramienta());
        panel.add(btnRegistrar);

        return panel;
    }

    private JTable crearTablaHerramientas() {
        String[] columnas = {"ID", "Tipo", "Nombre", "Cantidad", "Estado", "F. Adquisición"};
        tablaHerramientas = new JTable(new DefaultTableModel(new Object[0][0], columnas));
        return tablaHerramientas;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarDatosTabla());
        panel.add(btnActualizar);

        JButton btnUsar = new JButton("Usar Herramienta (Stock -1)");
        btnUsar.addActionListener(e -> usarHerramientaSeleccionada());
        panel.add(btnUsar);

        JButton btnReponer = new JButton("Reponer Stock (+ Cantidad)");
        btnReponer.addActionListener(e -> reponerHerramientaSeleccionada());
        panel.add(btnReponer);

        JButton btnEditar = new JButton("Editar Info General");
        btnEditar.addActionListener(e -> editarHerramientaSeleccionada());
        panel.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar Herramienta");
        btnEliminar.addActionListener(e -> eliminarHerramientaSeleccionada());
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatosTabla() {
        try {
            Set<Herramienta> setHerramientas = herramientaDAO.listarHerramientas();
            List<Herramienta> herramientas = new ArrayList<>(setHerramientas);

            String[] columnas = {"ID", "Tipo", "Nombre", "Cantidad", "Estado", "F. Adquisición"};
            Object[][] datos = new Object[herramientas.size()][columnas.length];

            for (int i = 0; i < herramientas.size(); i++) {
                Herramienta h = herramientas.get(i);
                datos[i][0] = h.getIdHerramienta();
                datos[i][1] = h.getTipo();
                datos[i][2] = h.getNombre();
                datos[i][3] = h.getCantidad();
                datos[i][4] = h.isEstado() ? "Funcional" : "No Funcional";
                datos[i][5] = FORMATO_FECHA.format(h.getFechaAdqusicion());
            }

            tablaHerramientas.setModel(new DefaultTableModel(datos, columnas));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevaHerramienta() {
        try {
            String tipo = txtTipo.getText();
            String nombre = txtNombre.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            boolean estado = cmbEstado.getSelectedItem().toString().contains("Funcional");

            if (herramientaDAO.existeHerramienta(nombre)) {
                JOptionPane.showMessageDialog(this, "Ya existe una herramienta con ese nombre.", "Error de Duplicidad", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date fechaAdquisicion = new Date();
            Herramienta nueva = new Herramienta(tipo, nombre, estado, cantidad, fechaAdquisicion);

            if (herramientaDAO.insertarHerramienta(nueva)) {
                JOptionPane.showMessageDialog(this, "Herramienta registrada con ID: " + nueva.getIdHerramienta(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
                txtTipo.setText(""); txtNombre.setText("");
                txtCantidad.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la herramienta.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad Inicial debe ser un valor numérico válido.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void usarHerramientaSeleccionada() {
        int filaSeleccionada = tablaHerramientas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una herramienta para usar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idHerramientaStr = tablaHerramientas.getValueAt(filaSeleccionada, 0).toString();
            int idHerramienta = Integer.parseInt(idHerramientaStr);

            if (herramientaDAO.usarHerramienta(idHerramienta)) {
                JOptionPane.showMessageDialog(this, "Uso registrado. Stock reducido en 1.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo usar. No hay stock, está dañada o el ID no existe.", "Error de Stock/Estado", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reponerHerramientaSeleccionada() {
        int filaSeleccionada = tablaHerramientas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una herramienta para reponer stock.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idHerramientaStr = tablaHerramientas.getValueAt(filaSeleccionada, 0).toString();
            int idHerramienta = Integer.parseInt(idHerramientaStr);
            String nombre = (String) tablaHerramientas.getValueAt(filaSeleccionada, 2);

            String cantidadStr = JOptionPane.showInputDialog(this,
                    "Cantidad a reponer para '" + nombre + "':",
                    "1");

            if (cantidadStr != null) {
                int cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad a reponer debe ser positiva.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (herramientaDAO.reponerHerramienta(idHerramienta, cantidad)) {
                    JOptionPane.showMessageDialog(this, "Stock actualizado. Reposición de " + cantidad + " unidades.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatosTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al reponer stock. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarHerramientaSeleccionada() {
        int filaSeleccionada = tablaHerramientas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una herramienta para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idHerramientaStr = tablaHerramientas.getValueAt(filaSeleccionada, 0).toString();
            int idHerramienta = Integer.parseInt(idHerramientaStr);
            Herramienta herrAEditar = herramientaDAO.buscarPorId(idHerramienta);

            if (herrAEditar != null) {
                String estadoActualStr = herrAEditar.isEstado() ? "Funcional" : "No Funcional";

                String nuevoTipo = JOptionPane.showInputDialog(this, "Nuevo Tipo:", herrAEditar.getTipo());
                String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo Nombre:", herrAEditar.getNombre());

                String nuevoEstadoStr = (String) JOptionPane.showInputDialog(this,
                        "Seleccione el nuevo estado:",
                        "Editar Estado",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Funcional", "No Funcional"},
                        estadoActualStr);

                if (nuevoTipo != null && nuevoNombre != null && nuevoEstadoStr != null) {

                    herrAEditar.setTipo(nuevoTipo);
                    herrAEditar.setNombre(nuevoNombre);
                    herrAEditar.setEstado(nuevoEstadoStr.equals("Funcional"));

                    if (herramientaDAO.editarHerramienta(herrAEditar)) {
                        JOptionPane.showMessageDialog(this, "Información general actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarDatosTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar la herramienta.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID de herramienta.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarHerramientaSeleccionada() {
        int filaSeleccionada = tablaHerramientas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una herramienta para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idHerramientaStr = tablaHerramientas.getValueAt(filaSeleccionada, 0).toString();
        int idHerramienta = Integer.parseInt(idHerramientaStr);
        String nombre = (String) tablaHerramientas.getValueAt(filaSeleccionada, 2);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la herramienta '" + nombre + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (herramientaDAO.eliminarHerramienta(idHerramienta)) {
                JOptionPane.showMessageDialog(this, "Herramienta eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar. Podría estar asociada a una Cita (Integridad Referencial).",
                        "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}