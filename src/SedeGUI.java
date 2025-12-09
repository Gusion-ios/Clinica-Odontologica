import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SedeGUI extends JFrame {

    private final SedeDAO sedeDAO = new SedeDAO();
    private JTable tablaSedes;
    private JTextField txtCapacidad;
    private JTextField txtUbicacion;
    private JTextField txtIdClinica;
    private JButton btnRegistrar;
    private JButton btnActualizar;

    public SedeGUI() {
        setTitle("1. Gestión de Sedes (JDBC)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);

        add(new JScrollPane(crearTablaSedes()), BorderLayout.CENTER);

        add(crearPanelAcciones(), BorderLayout.SOUTH);

        cargarDatosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));

        txtCapacidad = new JTextField(5);
        txtUbicacion = new JTextField(15);
        txtIdClinica = new JTextField(5);

        panel.add(new JLabel("Capacidad:"));
        panel.add(txtCapacidad);
        panel.add(new JLabel("Ubicación:"));
        panel.add(txtUbicacion);

        panel.add(new JLabel("ID Clínica:"));
        panel.add(txtIdClinica);

        btnRegistrar = new JButton("Registrar Sede");
        btnRegistrar.addActionListener(e -> registrarNuevaSede());
        panel.add(btnRegistrar);

        return panel;
    }

    private JTable crearTablaSedes() {
        String[] columnas = {"ID", "Capacidad", "Ubicación", "Disponible", "ID Clínica"};
        tablaSedes = new JTable(new DefaultTableModel(new Object[0][0], columnas));
        return tablaSedes;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnActualizar = new JButton("Actualizar / Recargar Lista");
        btnActualizar.addActionListener(e -> cargarDatosTabla());
        panel.add(btnActualizar);

        JButton btnEditar = new JButton("Editar Sede");
        btnEditar.addActionListener(e -> editarSedeSeleccionada());
        panel.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar Sede");
        btnEliminar.addActionListener(e -> eliminarSedeSeleccionada());
        panel.add(btnEliminar);

        JButton btnCambiarDisp = new JButton("Cambiar Disponibilidad");
        btnCambiarDisp.addActionListener(e -> cambiarDisponibilidadSeleccionada());
        panel.add(btnCambiarDisp);

        return panel;
    }

    private void cargarDatosTabla() {
        try {
            List<Sede> sedes = sedeDAO.listarSedes();
            String[] columnas = {"ID", "Capacidad", "Ubicación", "Disponible", "ID Clínica"};
            Object[][] datos = new Object[sedes.size()][columnas.length];

            for (int i = 0; i < sedes.size(); i++) {
                Sede s = sedes.get(i);
                datos[i][0] = s.getIdSede();
                datos[i][1] = s.getCapacidad();
                datos[i][2] = s.getUbicacion();
                datos[i][3] = s.isDisponible() ? "Libre" : "Ocupada";
                datos[i][4] = s.getIdClinica();
            }

            tablaSedes.setModel(new DefaultTableModel(datos, columnas));
            JOptionPane.showMessageDialog(this, "Datos de sedes cargados correctamente.", "Carga Exitosa", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevaSede() {
        try {
            int capacidad = Integer.parseInt(txtCapacidad.getText());
            String ubicacion = txtUbicacion.getText();
            int idClinica = Integer.parseInt(txtIdClinica.getText());

            Sede nueva = new Sede(capacidad, true, ubicacion, idClinica);

            if (sedeDAO.insertarSede(nueva)) {
                JOptionPane.showMessageDialog(this, "Sede registrada con ID: " + nueva.getIdSede(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();

                txtCapacidad.setText("");
                txtUbicacion.setText("");
                txtIdClinica.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la sede. Revise la consola o verifique si la Clínica existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos en Capacidad e ID Clínica.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarSedeSeleccionada() {
        int filaSeleccionada = tablaSedes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una sede de la tabla para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idSede = (int) tablaSedes.getValueAt(filaSeleccionada, 0);

            Sede sedeAEditar = sedeDAO.buscarPorId(idSede);

            if (sedeAEditar != null) {
                String nuevaCapacidadStr = JOptionPane.showInputDialog(this, "Nueva Capacidad:", sedeAEditar.getCapacidad());
                String nuevaUbicacion = JOptionPane.showInputDialog(this, "Nueva Ubicación:", sedeAEditar.getUbicacion());
                String nuevoIdClinicaStr = JOptionPane.showInputDialog(this, "Nuevo ID Clínica:", sedeAEditar.getIdClinica());

                if (nuevaCapacidadStr != null && nuevaUbicacion != null && nuevoIdClinicaStr != null) {

                    sedeAEditar.setCapacidad(Integer.parseInt(nuevaCapacidadStr));
                    sedeAEditar.setUbicacion(nuevaUbicacion);
                    sedeAEditar.setIdClinica(Integer.parseInt(nuevoIdClinicaStr));

                    if (sedeDAO.editarSede(sedeAEditar)) {
                        JOptionPane.showMessageDialog(this, "Sede actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarDatosTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar la sede.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSedeSeleccionada() {
        int filaSeleccionada = tablaSedes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una sede de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idSede = (int) tablaSedes.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la Sede ID: " + idSede + "? Esto podría afectar otras tablas.",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (sedeDAO.eliminarSede(idSede)) {
                JOptionPane.showMessageDialog(this, "Sede eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar. Verifique que no tenga Salas asociadas (Integridad Referencial).",
                        "Error de FK", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cambiarDisponibilidadSeleccionada() {
        int filaSeleccionada = tablaSedes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una sede.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idSede = (int) tablaSedes.getValueAt(filaSeleccionada, 0);
        String estadoActualStr = (String) tablaSedes.getValueAt(filaSeleccionada, 3);
        boolean nuevoEstado = estadoActualStr.equals("Ocupada");

        if (sedeDAO.cambiarDisponibilidad(idSede, nuevoEstado)) {
            JOptionPane.showMessageDialog(this,
                    "Estado de la Sede " + idSede + " cambiado a " + (nuevoEstado ? "Libre" : "Ocupada"),
                    "Cambio de Estado", JOptionPane.INFORMATION_MESSAGE);
            cargarDatosTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al cambiar la disponibilidad.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}