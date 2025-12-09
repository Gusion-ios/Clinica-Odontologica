import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PacienteGUI extends JFrame {

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final AlergiaDAO alergiaDAO = new AlergiaDAO();

    private JTable tablaPacientes;
    private JTextField txtDni;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtEdad;
    private JComboBox<String> cmbGenero;
    private JTextField txtTelefono;
    private JTextField txtIdAlergia;

    public PacienteGUI() {
        setTitle("4. Gestión de Pacientes (JDBC)");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);

        add(new JScrollPane(crearTablaPacientes()), BorderLayout.CENTER);

        add(crearPanelAcciones(), BorderLayout.SOUTH);

        cargarDatosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 10, 10));

        txtDni = new JTextField(8);
        txtNombres = new JTextField(15);
        txtApellidos = new JTextField(15);
        txtEdad = new JTextField(3);
        cmbGenero = new JComboBox<>(new String[]{"M", "F"});
        txtTelefono = new JTextField(10);
        txtIdAlergia = new JTextField(5);

        panel.add(new JLabel("DNI (PK):"));
        panel.add(txtDni);
        panel.add(new JLabel("Nombres:"));
        panel.add(txtNombres);

        panel.add(new JLabel("Apellidos:"));
        panel.add(txtApellidos);
        panel.add(new JLabel("Edad:"));
        panel.add(txtEdad);

        panel.add(new JLabel("Género:"));
        panel.add(cmbGenero);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("ID Alergia (0 o vacío):"));
        panel.add(txtIdAlergia);

        JButton btnRegistrar = new JButton("Registrar Paciente");
        btnRegistrar.addActionListener(e -> registrarNuevoPaciente());
        panel.add(btnRegistrar);

        return panel;
    }

    private JTable crearTablaPacientes() {
        String[] columnas = {"DNI", "Nombres", "Apellidos", "Edad", "Género", "Teléfono", "ID Alergia"};
        tablaPacientes = new JTable(new DefaultTableModel(new Object[0][0], columnas));
        return tablaPacientes;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarDatosTabla());
        panel.add(btnActualizar);

        JButton btnEditar = new JButton("Editar Paciente");
        btnEditar.addActionListener(e -> editarPacienteSeleccionado());
        panel.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar Paciente");
        btnEliminar.addActionListener(e -> eliminarPacienteSeleccionado());
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatosTabla() {
        try {
            List<Paciente> pacientes = pacienteDAO.listarPacientes() instanceof List ?
                    (List<Paciente>) pacienteDAO.listarPacientes() :
                    new java.util.ArrayList<>(pacienteDAO.listarPacientes());

            String[] columnas = {"DNI", "Nombres", "Apellidos", "Edad", "Género", "Teléfono", "ID Alergia"};
            Object[][] datos = new Object[pacientes.size()][columnas.length];

            for (int i = 0; i < pacientes.size(); i++) {
                Paciente p = pacientes.get(i);
                datos[i][0] = p.getDNIPaciente();
                datos[i][1] = p.getNombres();
                datos[i][2] = p.getApellidos();
                datos[i][3] = p.getEdad();
                datos[i][4] = p.getGenero();
                datos[i][5] = p.getTelefono();
                datos[i][6] = p.getIdAlergia() != 0 ? p.getIdAlergia() : "N/A";
            }

            tablaPacientes.setModel(new DefaultTableModel(datos, columnas));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevoPaciente() {
        try {
            int dni = Integer.parseInt(txtDni.getText());
            String nombres = txtNombres.getText();
            String apellidos = txtApellidos.getText();
            int edad = Integer.parseInt(txtEdad.getText());
            String genero = (String) cmbGenero.getSelectedItem();
            int telefono = Integer.parseInt(txtTelefono.getText());

            int idAlergia = 0;
            String idAlergiaStr = txtIdAlergia.getText().trim();
            if (!idAlergiaStr.isEmpty()) {
                idAlergia = Integer.parseInt(idAlergiaStr);

                if (alergiaDAO.buscarPorId(idAlergia) == null) {
                    JOptionPane.showMessageDialog(this, "La Alergia con ID " + idAlergia + " no existe.", "Error de FK", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Paciente nuevo = new Paciente(dni, nombres, apellidos, edad, genero, telefono, idAlergia);

            if (pacienteDAO.insertarPaciente(nuevo)) {
                JOptionPane.showMessageDialog(this, "Paciente registrado con DNI: " + dni, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();

                txtDni.setText(""); txtNombres.setText(""); txtApellidos.setText("");
                txtEdad.setText(""); txtTelefono.setText(""); txtIdAlergia.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el Paciente (DNI duplicado o error de DB).", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "DNI, Edad, Teléfono e ID Alergia deben ser valores numéricos válidos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarPacienteSeleccionado() {
        int filaSeleccionada = tablaPacientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String dniPacienteStr = tablaPacientes.getValueAt(filaSeleccionada, 0).toString();
            int dniPaciente = Integer.parseInt(dniPacienteStr);
            Paciente pacienteAEditar = pacienteDAO.buscarPorDNI(dniPaciente);

            if (pacienteAEditar != null) {
                String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo Nombre:", pacienteAEditar.getNombres());
                String nuevoApellido = JOptionPane.showInputDialog(this, "Nuevo Apellido:", pacienteAEditar.getApellidos());
                String nuevaEdadStr = JOptionPane.showInputDialog(this, "Nueva Edad:", String.valueOf(pacienteAEditar.getEdad()));
                String nuevoTelefonoStr = JOptionPane.showInputDialog(this, "Nuevo Teléfono:", String.valueOf(pacienteAEditar.getTelefono()));

                String idAlergiaActual = pacienteAEditar.getIdAlergia() != 0 ? String.valueOf(pacienteAEditar.getIdAlergia()) : "";
                String nuevoIdAlergiaStr = JOptionPane.showInputDialog(this, "Nuevo ID Alergia (0 o vacío):", idAlergiaActual);


                if (nuevoNombre != null && nuevoApellido != null && nuevaEdadStr != null && nuevoTelefonoStr != null) {

                    int nuevoIdAlergia = 0;
                    if (nuevoIdAlergiaStr != null && !nuevoIdAlergiaStr.trim().isEmpty()) {
                        nuevoIdAlergia = Integer.parseInt(nuevoIdAlergiaStr);
                        if (alergiaDAO.buscarPorId(nuevoIdAlergia) == null) {
                            JOptionPane.showMessageDialog(this, "El nuevo ID de Alergia no existe.", "Error de FK", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    pacienteAEditar.setNombres(nuevoNombre);
                    pacienteAEditar.setApellidos(nuevoApellido);
                    pacienteAEditar.setEdad(Integer.parseInt(nuevaEdadStr));
                    pacienteAEditar.setTelefono(Integer.parseInt(nuevoTelefonoStr));
                    pacienteAEditar.setIdAlergia(nuevoIdAlergia);

                    if (pacienteDAO.modificarPaciente(pacienteAEditar)) {
                        JOptionPane.showMessageDialog(this, "Paciente actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarDatosTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese números válidos para Edad, Teléfono e ID Alergia.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPacienteSeleccionado() {
        int filaSeleccionada = tablaPacientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dniPacienteStr = tablaPacientes.getValueAt(filaSeleccionada, 0).toString();
        int dniPaciente = Integer.parseInt(dniPacienteStr);
        String nombreCompleto = (String) tablaPacientes.getValueAt(filaSeleccionada, 1) + " " + (String) tablaPacientes.getValueAt(filaSeleccionada, 2);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al paciente " + nombreCompleto + "?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (pacienteDAO.eliminarPaciente(dniPaciente)) {
                JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar. Podría tener Citas, Pagos o Tratamientos asociados (Integridad Referencial).",
                        "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}