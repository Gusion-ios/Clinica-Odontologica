import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TratamientoGUI extends JFrame {

    private final TratamientoDAO tratamientoDAO = new TratamientoDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    private JTable tablaTratamientos;
    private JTextField txtMotivo;
    private JTextField txtDuracion;
    private JTextField txtDniPaciente;
    private JTextField txtIdProducto;
    private JComboBox<String> cmbEstado;

    public TratamientoGUI() {
        setTitle("6. Gestión de Tratamientos (JDBC)");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(crearTablaTratamientos()), BorderLayout.CENTER);
        add(crearPanelAcciones(), BorderLayout.SOUTH);

        cargarDatosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 10, 10));

        txtMotivo = new JTextField(20);
        txtDuracion = new JTextField(5);
        txtDniPaciente = new JTextField(10);
        txtIdProducto = new JTextField(5);
        cmbEstado = new JComboBox<>(new String[]{"Activo", "En Proceso", "Finalizado", "Cancelado"});

        panel.add(new JLabel("Motivo/Descripción:"));
        panel.add(txtMotivo);
        panel.add(new JLabel("Estado Inicial:"));
        panel.add(cmbEstado);

        panel.add(new JLabel("Duración (Sesiones):"));
        panel.add(txtDuracion);
        panel.add(new JLabel("DNI Paciente:"));
        panel.add(txtDniPaciente);

        panel.add(new JLabel("ID Producto/Material:"));
        panel.add(txtIdProducto);

        JButton btnRegistrar = new JButton("Registrar Tratamiento");
        btnRegistrar.addActionListener(e -> registrarNuevoTratamiento());
        panel.add(btnRegistrar);

        return panel;
    }

    private JTable crearTablaTratamientos() {
        String[] columnas = {"ID", "Motivo", "Estado", "Duración (Sesiones)", "DNI Paciente", "ID Producto"};
        tablaTratamientos = new JTable(new DefaultTableModel(new Object[0][0], columnas));
        return tablaTratamientos;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarDatosTabla());
        panel.add(btnActualizar);

        JButton btnEditarDuracion = new JButton("Actualizar Duración");
        btnEditarDuracion.addActionListener(e -> actualizarDuracionSeleccionada());
        panel.add(btnEditarDuracion);

        JButton btnCambiarEstado = new JButton("Cambiar Estado");
        btnCambiarEstado.addActionListener(e -> cambiarEstadoSeleccionado());
        panel.add(btnCambiarEstado);

        JButton btnEliminar = new JButton("Eliminar Tratamiento");
        btnEliminar.addActionListener(e -> eliminarTratamientoSeleccionado());
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatosTabla() {
        try {
            List<Tratamiento> tratamientos = tratamientoDAO.listarTratamientos();
            String[] columnas = {"ID", "Motivo", "Estado", "Duración (Sesiones)", "DNI Paciente", "ID Producto"};
            Object[][] datos = new Object[tratamientos.size()][columnas.length];

            for (int i = 0; i < tratamientos.size(); i++) {
                Tratamiento t = tratamientos.get(i);
                datos[i][0] = t.getIdTratamiento();
                datos[i][1] = t.getMotivo();
                datos[i][2] = t.getEstado();
                datos[i][3] = t.getDuracionSesiones();
                datos[i][4] = t.getPaciente();
                datos[i][5] = t.getProducto();
            }

            tablaTratamientos.setModel(new DefaultTableModel(datos, columnas));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevoTratamiento() {
        try {
            String motivo = txtMotivo.getText();
            String estado = (String) cmbEstado.getSelectedItem();
            int duracion = Integer.parseInt(txtDuracion.getText());
            int dniPaciente = Integer.parseInt(txtDniPaciente.getText());
            int idProducto = Integer.parseInt(txtIdProducto.getText());

            if (pacienteDAO.buscarPorDNI(dniPaciente) == null) {
                JOptionPane.showMessageDialog(this, "El DNI del paciente no existe.", "Error de FK", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (productoDAO.buscarPorId(idProducto) == null) {
                JOptionPane.showMessageDialog(this, "El ID del producto/material no existe.", "Error de FK", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Tratamiento nuevo = new Tratamiento(motivo, estado, duracion, dniPaciente, idProducto);

            if (tratamientoDAO.insertarTratamiento(nuevo)) {
                JOptionPane.showMessageDialog(this, "Tratamiento registrado con ID: " + nuevo.getIdTratamiento(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
                txtMotivo.setText(""); txtDuracion.setText("");
                txtDniPaciente.setText(""); txtIdProducto.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el tratamiento.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Duración, DNI y ID Producto deben ser valores numéricos válidos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarDuracionSeleccionada() {
        int filaSeleccionada = tablaTratamientos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un tratamiento para actualizar la duración.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idTratStr = tablaTratamientos.getValueAt(filaSeleccionada, 0).toString();
            int idTratamiento = Integer.parseInt(idTratStr);
            String duracionActual = tablaTratamientos.getValueAt(filaSeleccionada, 3).toString();

            String nuevaDuracionStr = JOptionPane.showInputDialog(this,
                    "Nueva Duración (Sesiones) para Tratamiento ID " + idTratamiento + " (Actual: " + duracionActual + "):",
                    duracionActual);

            if (nuevaDuracionStr != null) {
                int nuevaDuracion = Integer.parseInt(nuevaDuracionStr);

                if (tratamientoDAO.actualizarDuracion(idTratamiento, nuevaDuracion)) {
                    JOptionPane.showMessageDialog(this, "Duración actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatosTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar la duración.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido para la Duración.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoSeleccionado() {
        int filaSeleccionada = tablaTratamientos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un tratamiento para cambiar el estado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idTratStr = tablaTratamientos.getValueAt(filaSeleccionada, 0).toString();
        int idTratamiento = Integer.parseInt(idTratStr);
        String estadoActual = (String) tablaTratamientos.getValueAt(filaSeleccionada, 2);

        String[] opcionesEstado = {"Activo", "En Proceso", "Finalizado", "Cancelado"};

        String nuevoEstado = (String) JOptionPane.showInputDialog(this,
                "Seleccione el nuevo estado para el Tratamiento ID " + idTratamiento + " (Actual: " + estadoActual + "):",
                "Cambiar Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesEstado,
                estadoActual);

        if (nuevoEstado != null) {
            if (tratamientoDAO.cambiarEstado(idTratamiento, nuevoEstado)) {
                JOptionPane.showMessageDialog(this, "Estado cambiado a '" + nuevoEstado + "' correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al cambiar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarTratamientoSeleccionado() {
        int filaSeleccionada = tablaTratamientos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un tratamiento para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idTratStr = tablaTratamientos.getValueAt(filaSeleccionada, 0).toString();
        int idTratamiento = Integer.parseInt(idTratStr);
        String motivo = (String) tablaTratamientos.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el tratamiento '" + motivo + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (tratamientoDAO.eliminarTratamiento(idTratamiento)) {
                JOptionPane.showMessageDialog(this, "Tratamiento eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar. Podría estar asociado a una Cita (Integridad Referencial).",
                        "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}