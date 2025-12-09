import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClinicaGUI extends JFrame {

    private final ClinicaDAO clinicaDAO = new ClinicaDAO();
    private JTable tablaClinicas;
    private JTextField txtNombre;
    private JTextField txtRUC;
    private JTextField txtTelefono;
    private JTextField txtDireccion;

    public ClinicaGUI() {
        setTitle("2. Gestión de Clínicas (JDBC)");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);

        add(new JScrollPane(crearTablaClinicas()), BorderLayout.CENTER);
        add(crearPanelAcciones(), BorderLayout.SOUTH);

        cargarDatosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));

        txtNombre = new JTextField(15);
        txtRUC = new JTextField(10);
        txtTelefono = new JTextField(10);
        txtDireccion = new JTextField(20);

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("RUC:"));
        panel.add(txtRUC);

        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);

        JButton btnRegistrar = new JButton("Registrar Clínica");
        btnRegistrar.addActionListener(e -> registrarNuevaClinica());
        panel.add(btnRegistrar);

        return panel;
    }

    private JTable crearTablaClinicas() {
        String[] columnas = {"ID", "Nombre", "RUC", "Teléfono", "Dirección"};
        tablaClinicas = new JTable(new DefaultTableModel(new Object[0][0], columnas));
        return tablaClinicas;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarDatosTabla());
        panel.add(btnActualizar);

        JButton btnEditar = new JButton("Editar Clínica");
        btnEditar.addActionListener(e -> editarClinicaSeleccionada());
        panel.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar Clínica");
        btnEliminar.addActionListener(e -> eliminarClinicaSeleccionada());
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatosTabla() {
        try {
            List<Clinica> clinicas = clinicaDAO.listarClinicas();
            String[] columnas = {"ID", "Nombre", "RUC", "Teléfono", "Dirección"};
            Object[][] datos = new Object[clinicas.size()][columnas.length];

            for (int i = 0; i < clinicas.size(); i++) {
                Clinica c = clinicas.get(i);
                datos[i][0] = c.getIdClinica();
                datos[i][1] = c.getNombre();
                datos[i][2] = c.getRUC();
                datos[i][3] = c.getTelefono();
                datos[i][4] = c.getDireccion();
            }

            tablaClinicas.setModel(new DefaultTableModel(datos, columnas));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevaClinica() {
        try {
            String nombre = txtNombre.getText();
            String ruc = txtRUC.getText();
            String telefono = txtTelefono.getText();
            String direccion = txtDireccion.getText();

            if (nombre.isEmpty() || ruc.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos deben estar llenos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Clinica nueva = new Clinica(nombre, ruc, telefono, direccion);

            if (clinicaDAO.insertarClinica(nueva)) {
                JOptionPane.showMessageDialog(this, "Clínica registrada con ID: " + nueva.getIdClinica(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();

                txtNombre.setText("");
                txtRUC.setText("");
                txtTelefono.setText("");
                txtDireccion.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la clínica. Revise la consola (ej. RUC duplicado).", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de ingreso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarClinicaSeleccionada() {
        int filaSeleccionada = tablaClinicas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una clínica para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idClinicaStr = tablaClinicas.getValueAt(filaSeleccionada, 0).toString();
            int idClinica = Integer.parseInt(idClinicaStr);
            Clinica clinicaAEditar = clinicaDAO.buscarPorId(idClinica);

            if (clinicaAEditar != null) {
                String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo Nombre:", clinicaAEditar.getNombre());
                String nuevoRUC = JOptionPane.showInputDialog(this, "Nuevo RUC:", clinicaAEditar.getRUC());
                String nuevoTelefonoStr = JOptionPane.showInputDialog(this, "Nuevo Teléfono:", clinicaAEditar.getTelefono());
                String nuevaDireccion = JOptionPane.showInputDialog(this, "Nueva Dirección:", clinicaAEditar.getDireccion());

                if (nuevoNombre != null && nuevoRUC != null && nuevoTelefonoStr != null && nuevaDireccion != null) {

                    clinicaAEditar.setNombre(nuevoNombre);
                    clinicaAEditar.setRUC(nuevoRUC);
                    clinicaAEditar.setTelefono(nuevoTelefonoStr);
                    clinicaAEditar.setDireccion(nuevaDireccion);

                    if (clinicaDAO.editarClinica(clinicaAEditar)) {
                        JOptionPane.showMessageDialog(this, "Clínica actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarDatosTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar la clínica en DB.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error: No se encontró el registro en la base de datos.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: El ID de la tabla o un dato de entrada no es numérico.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al editar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminarClinicaSeleccionada() {
        int filaSeleccionada = tablaClinicas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una clínica para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idClinica = (int) tablaClinicas.getValueAt(filaSeleccionada, 0);
        String nombre = (String) tablaClinicas.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la Clínica '" + nombre + "'? Esto eliminará todas sus Sedes asociadas.",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (clinicaDAO.eliminarClinica(idClinica)) {
                JOptionPane.showMessageDialog(this, "Clínica eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar la clínica. Podría tener Sedes asociadas (Integridad Referencial).",
                        "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}