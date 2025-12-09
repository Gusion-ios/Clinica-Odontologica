import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class OdontologoGUI extends JFrame {

    private final OdontologoDAO odontologoDAO = new OdontologoDAO();
    private final SedeDAO sedeDAO = new SedeDAO();

    private JTable tablaOdontologos;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEspecialidad;
    private JTextField txtSedeMain;

    public OdontologoGUI() {
        setTitle("3. Gestión de Odontólogos (JDBC)");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);

        add(new JScrollPane(crearTablaOdontologos()), BorderLayout.CENTER);

        add(crearPanelAcciones(), BorderLayout.SOUTH);

        cargarDatosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));

        txtNombre = new JTextField(15);
        txtApellido = new JTextField(15);
        txtEspecialidad = new JTextField(15);
        txtSedeMain = new JTextField(5);

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtApellido);

        panel.add(new JLabel("Especialidad:"));
        panel.add(txtEspecialidad);
        panel.add(new JLabel("ID Sede Principal:"));
        panel.add(txtSedeMain);

        JButton btnRegistrar = new JButton("Registrar Odontólogo");
        btnRegistrar.addActionListener(e -> registrarNuevoOdontologo());
        panel.add(btnRegistrar);

        return panel;
    }

    private JTable crearTablaOdontologos() {
        String[] columnas = {"ID", "Nombre", "Apellido", "Especialidad", "ID Sede Principal"};
        tablaOdontologos = new JTable(new DefaultTableModel(new Object[0][0], columnas));
        return tablaOdontologos;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarDatosTabla());
        panel.add(btnActualizar);

        JButton btnEditar = new JButton("Editar Odontólogo");
        btnEditar.addActionListener(e -> editarOdontologoSeleccionado());
        panel.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar Odontólogo");
        btnEliminar.addActionListener(e -> eliminarOdontologoSeleccionado());
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatosTabla() {
        try {
            Set<Odontologo> setOdontologos = odontologoDAO.listarOdontologos();

            List<Odontologo> odontologos = new java.util.ArrayList<>(setOdontologos);

            String[] columnas = {"ID", "Nombre", "Apellido", "Especialidad", "ID Sede Principal"};
            Object[][] datos = new Object[odontologos.size()][columnas.length];

            for (int i = 0; i < odontologos.size(); i++) {
                Odontologo o = odontologos.get(i);
                datos[i][0] = o.getIdOdontologo();
                datos[i][1] = o.getNombre();
                datos[i][2] = o.getApellido();
                datos[i][3] = o.getEspecialidad();
                datos[i][4] = o.getSedeMain();
            }

            tablaOdontologos.setModel(new DefaultTableModel(datos, columnas));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevoOdontologo() {
        try {
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String especialidad = txtEspecialidad.getText();
            int sedeMain = Integer.parseInt(txtSedeMain.getText());

            if (sedeDAO.buscarPorId(sedeMain) == null) {
                JOptionPane.showMessageDialog(this, "La Sede con ID " + sedeMain + " no existe. Verifique la tabla de Sedes.", "Error de FK", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Odontologo nuevo = new Odontologo(nombre, apellido, especialidad, sedeMain);

            if (odontologoDAO.insertarOdontologo(nuevo)) {
                JOptionPane.showMessageDialog(this, "Odontólogo registrado con ID: " + nuevo.getIdOdontologo(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();

                txtNombre.setText("");
                txtApellido.setText("");
                txtEspecialidad.setText("");
                txtSedeMain.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el Odontólogo.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID de Sede debe ser un valor numérico válido.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarOdontologoSeleccionado() {
        int filaSeleccionada = tablaOdontologos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un odontólogo para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idOdontologo = (int) tablaOdontologos.getValueAt(filaSeleccionada, 0);
            Odontologo odonAEditar = odontologoDAO.buscarPorId(idOdontologo);

            if (odonAEditar != null) {
                String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo Nombre:", odonAEditar.getNombre());
                String nuevoApellido = JOptionPane.showInputDialog(this, "Nuevo Apellido:", odonAEditar.getApellido());
                String nuevaEspecialidad = JOptionPane.showInputDialog(this, "Nueva Especialidad:", odonAEditar.getEspecialidad());
                String nuevaSedeMainStr = JOptionPane.showInputDialog(this, "Nuevo ID Sede Principal:", String.valueOf(odonAEditar.getSedeMain()));

                if (nuevoNombre != null && nuevoApellido != null && nuevaEspecialidad != null && nuevaSedeMainStr != null) {

                    int nuevaSedeMain = Integer.parseInt(nuevaSedeMainStr);

                    if (sedeDAO.buscarPorId(nuevaSedeMain) == null) {
                        JOptionPane.showMessageDialog(this, "El nuevo ID de Sede no existe.", "Error de FK", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    odonAEditar.setNombre(nuevoNombre);
                    odonAEditar.setApellido(nuevoApellido);
                    odonAEditar.setEspecialidad(nuevaEspecialidad);
                    odonAEditar.setSedeMain(nuevaSedeMain);

                    if (odontologoDAO.editarOdontologo(odonAEditar)) {
                        JOptionPane.showMessageDialog(this, "Odontólogo actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarDatosTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar el odontólogo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido para ID Sede.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarOdontologoSeleccionado() {
        int filaSeleccionada = tablaOdontologos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un odontólogo para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idOdontologo = (int) tablaOdontologos.getValueAt(filaSeleccionada, 0);
        String nombreCompleto = (String) tablaOdontologos.getValueAt(filaSeleccionada, 1) + " " + (String) tablaOdontologos.getValueAt(filaSeleccionada, 2);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar a " + nombreCompleto + "?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (odontologoDAO.eliminarOdontologo(idOdontologo)) {
                JOptionPane.showMessageDialog(this, "Odontólogo eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar. Podría estar asociado a una Cita (Integridad Referencial).",
                        "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}