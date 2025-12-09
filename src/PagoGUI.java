import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.text.SimpleDateFormat;

public class PagoGUI extends JFrame {

    private final PagoDAO pagoDAO = new PagoDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    private JTable tablaPagos;
    private JComboBox<String> cmbMetodoPago;
    private JComboBox<String> cmbEstadoInicial;
    private JTextField txtDniPaciente;

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public PagoGUI() {
        setTitle("7. Gestión de Pagos (JDBC)");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(crearTablaPagos()), BorderLayout.CENTER);
        add(crearPanelAcciones(), BorderLayout.SOUTH);

        cargarDatosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));

        txtDniPaciente = new JTextField(10);
        cmbMetodoPago = new JComboBox<>(new String[]{"Tarjeta", "Efectivo", "Transferencia"});
        cmbEstadoInicial = new JComboBox<>(new String[]{"Pagado", "Pendiente"});

        panel.add(new JLabel("DNI Paciente (FK):"));
        panel.add(txtDniPaciente);
        panel.add(new JLabel("Método de Pago:"));
        panel.add(cmbMetodoPago);

        panel.add(new JLabel("Estado Inicial:"));
        panel.add(cmbEstadoInicial);

        JButton btnRegistrar = new JButton("Registrar Pago Ahora");
        btnRegistrar.addActionListener(e -> registrarNuevoPago());
        panel.add(btnRegistrar);

        return panel;
    }

    private JTable crearTablaPagos() {
        String[] columnas = {"ID Pago", "DNI Paciente", "Fecha", "Estado", "Método"};
        tablaPagos = new JTable(new DefaultTableModel(new Object[0][0], columnas));
        return tablaPagos;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarDatosTabla());
        panel.add(btnActualizar);

        JButton btnActualizarEstado = new JButton("Actualizar Estado");
        btnActualizarEstado.addActionListener(e -> actualizarEstadoSeleccionado());
        panel.add(btnActualizarEstado);

        JButton btnEliminar = new JButton("Eliminar Pago");
        btnEliminar.addActionListener(e -> eliminarPagoSeleccionado());
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatosTabla() {
        try {
            Set<Pago> setPagos = pagoDAO.listarPagos();
            List<Pago> pagos = new java.util.ArrayList<>(setPagos);

            String[] columnas = {"ID Pago", "DNI Paciente", "Fecha", "Estado", "Método"};
            Object[][] datos = new Object[pagos.size()][columnas.length];

            for (int i = 0; i < pagos.size(); i++) {
                Pago p = pagos.get(i);
                datos[i][0] = p.getIdPago();
                datos[i][1] = p.getDniPaciente();
                datos[i][2] = FORMATO_FECHA.format(p.getFecha());
                datos[i][3] = p.isEstadoPagado() ? "Pagado" : "Pendiente";
                datos[i][4] = p.getMetodo();
            }

            tablaPagos.setModel(new DefaultTableModel(datos, columnas));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevoPago() {
        try {
            int dniPaciente = Integer.parseInt(txtDniPaciente.getText());
            String metodoPago = (String) cmbMetodoPago.getSelectedItem();
            boolean estadoPagado = cmbEstadoInicial.getSelectedItem().equals("Pagado");

            if (pacienteDAO.buscarPorDNI(dniPaciente) == null) {
                JOptionPane.showMessageDialog(this, "El DNI del paciente no existe.", "Error de FK", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date fecha = new Date();
            Pago nuevo = new Pago(dniPaciente, fecha, estadoPagado, metodoPago);

            if (pagoDAO.insertarPago(nuevo)) {
                JOptionPane.showMessageDialog(this, "Pago registrado con ID: " + nuevo.getIdPago(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
                txtDniPaciente.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el pago.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El DNI Paciente debe ser un valor numérico válido.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarEstadoSeleccionado() {
        int filaSeleccionada = tablaPagos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pago.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idPagoStr = tablaPagos.getValueAt(filaSeleccionada, 0).toString();
            int idPago = Integer.parseInt(idPagoStr);
            String estadoActual = (String) tablaPagos.getValueAt(filaSeleccionada, 3);

            String[] opcionesEstado = {"Pagado", "Pendiente"};

            String nuevoEstadoStr = (String) JOptionPane.showInputDialog(this,
                    "Seleccione el nuevo estado para el Pago ID " + idPago + " (Actual: " + estadoActual + "):",
                    "Actualizar Estado",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesEstado,
                    estadoActual);

            if (nuevoEstadoStr != null) {
                boolean nuevoEstado = nuevoEstadoStr.equals("Pagado");

                if (pagoDAO.actualizarEstadoPago(idPago, nuevoEstado)) {
                    JOptionPane.showMessageDialog(this, "Estado actualizado a '" + nuevoEstadoStr + "' correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatosTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el estado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID de pago.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPagoSeleccionado() {
        int filaSeleccionada = tablaPagos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pago para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idPagoStr = tablaPagos.getValueAt(filaSeleccionada, 0).toString();
        int idPago = Integer.parseInt(idPagoStr);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el Pago ID: " + idPago + "?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (pagoDAO.eliminarPago(idPago)) {
                JOptionPane.showMessageDialog(this, "Pago eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el pago. Podría tener dependencias.", "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}