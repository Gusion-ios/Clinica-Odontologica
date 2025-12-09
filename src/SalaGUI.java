import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class SalaGUI extends JFrame {

    private final SalaDAO salaDAO = new SalaDAO();
    private final SedeDAO sedeDAO = new SedeDAO();

    private JTable tablaSalas;
    private JTextField txtTipo;
    private JTextField txtCapacidad;
    private JTextField txtUbicacion;
    private JTextField txtIdSede;
    private JComboBox<String> cmbEstadoLibre;

    public SalaGUI() {
        setTitle("10. Gestión de Salas (JDBC)");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(crearTablaSalas()), BorderLayout.CENTER);
        add(crearPanelAcciones(), BorderLayout.SOUTH);

        cargarDatosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 10, 10));

        txtTipo = new JTextField(15);
        txtCapacidad = new JTextField(5);
        txtUbicacion = new JTextField(20);
        txtIdSede = new JTextField(5);
        cmbEstadoLibre = new JComboBox<>(new String[]{"Libre (True)", "Ocupada (False)"});

        panel.add(new JLabel("Tipo de Sala:"));
        panel.add(txtTipo);
        panel.add(new JLabel("Capacidad:"));
        panel.add(txtCapacidad);

        panel.add(new JLabel("Ubicación/Piso:"));
        panel.add(txtUbicacion);
        panel.add(new JLabel("ID Sede (FK):"));
        panel.add(txtIdSede);

        panel.add(new JLabel("Estado Inicial:"));
        panel.add(cmbEstadoLibre);

        JButton btnRegistrar = new JButton("Registrar Sala");
        btnRegistrar.addActionListener(e -> registrarNuevaSala());
        panel.add(btnRegistrar);

        return panel;
    }

    private JTable crearTablaSalas() {
        String[] columnas = {"ID", "Tipo", "Capacidad", "Estado", "Ubicación", "ID Sede"};
        tablaSalas = new JTable(new DefaultTableModel(new Object[0][0], columnas));
        return tablaSalas;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarDatosTabla());
        panel.add(btnActualizar);

        JButton btnEditar = new JButton("Editar Sala");
        btnEditar.addActionListener(e -> editarSalaSeleccionada());
        panel.add(btnEditar);

        JButton btnCambiarEstado = new JButton("Cambiar Estado (Libre/Ocupada)");
        btnCambiarEstado.addActionListener(e -> cambiarEstadoSeleccionado());
        panel.add(btnCambiarEstado);

        JButton btnEliminar = new JButton("Eliminar Sala");
        btnEliminar.addActionListener(e -> eliminarSalaSeleccionada());
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatosTabla() {
        try {
            Set<Sala> setSalas = salaDAO.listarSalas();
            List<Sala> salas = new ArrayList<>(setSalas);

            String[] columnas = {"ID", "Tipo", "Capacidad", "Estado", "Ubicación", "ID Sede"};
            Object[][] datos = new Object[salas.size()][columnas.length];

            for (int i = 0; i < salas.size(); i++) {
                Sala s = salas.get(i);
                datos[i][0] = s.getIdSala();
                datos[i][1] = s.getTipo();
                datos[i][2] = s.getCapacidad();
                datos[i][3] = s.isEstadoLibre() ? "Libre" : "Ocupada";
                datos[i][4] = s.getUbicacion();
                datos[i][5] = s.getSede();
            }

            tablaSalas.setModel(new DefaultTableModel(datos, columnas));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevaSala() {
        try {
            String tipo = txtTipo.getText();
            int capacidad = Integer.parseInt(txtCapacidad.getText());
            String ubicacion = txtUbicacion.getText();
            int idSede = Integer.parseInt(txtIdSede.getText());
            boolean estadoLibre = cmbEstadoLibre.getSelectedItem().toString().contains("Libre");

            if (sedeDAO.buscarPorId(idSede) == null) {
                JOptionPane.showMessageDialog(this, "La Sede con ID " + idSede + " no existe.", "Error de FK", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Sala nueva = new Sala(tipo, capacidad, estadoLibre, ubicacion, idSede);

            if (salaDAO.insertarSala(nueva)) {
                JOptionPane.showMessageDialog(this, "Sala registrada con ID: " + nueva.getIdSala(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
                txtTipo.setText(""); txtCapacidad.setText("");
                txtUbicacion.setText(""); txtIdSede.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la sala.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacidad e ID Sede deben ser valores numéricos válidos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarSalaSeleccionada() {
        int filaSeleccionada = tablaSalas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una sala para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idSalaStr = tablaSalas.getValueAt(filaSeleccionada, 0).toString();
            int idSala = Integer.parseInt(idSalaStr);
            Sala salaAEditar = salaDAO.buscarPorId(idSala);

            if (salaAEditar != null) {
                String nuevoTipo = JOptionPane.showInputDialog(this, "Nuevo Tipo:", salaAEditar.getTipo());
                String nuevaCapacidadStr = JOptionPane.showInputDialog(this, "Nueva Capacidad:", String.valueOf(salaAEditar.getCapacidad()));
                String nuevaUbicacion = JOptionPane.showInputDialog(this, "Nueva Ubicación:", salaAEditar.getUbicacion());
                String nuevoIdSedeStr = JOptionPane.showInputDialog(this, "Nuevo ID Sede:", String.valueOf(salaAEditar.getSede()));

                if (nuevoTipo != null && nuevaCapacidadStr != null && nuevaUbicacion != null && nuevoIdSedeStr != null) {

                    int nuevaCapacidad = Integer.parseInt(nuevaCapacidadStr);
                    int nuevoIdSede = Integer.parseInt(nuevoIdSedeStr);

                    if (sedeDAO.buscarPorId(nuevoIdSede) == null) {
                        JOptionPane.showMessageDialog(this, "El nuevo ID de Sede no existe.", "Error de FK", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    salaAEditar.setTipo(nuevoTipo);
                    salaAEditar.setCapacidad(nuevaCapacidad);
                    salaAEditar.setUbicacion(nuevaUbicacion);
                    salaAEditar.setSede(nuevoIdSede);

                    if (salaDAO.editarSala(salaAEditar)) {
                        JOptionPane.showMessageDialog(this, "Sala actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarDatosTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar la sala.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacidad e ID Sede deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoSeleccionado() {
        int filaSeleccionada = tablaSalas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una sala.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idSalaStr = tablaSalas.getValueAt(filaSeleccionada, 0).toString();
            int idSala = Integer.parseInt(idSalaStr);
            String estadoActualStr = (String) tablaSalas.getValueAt(filaSeleccionada, 3);

            boolean estadoActual = estadoActualStr.equals("Libre");
            boolean nuevoEstado = !estadoActual;

            String nuevoEstadoStr = nuevoEstado ? "Libre" : "Ocupada";

            if (salaDAO.cambiarEstado(idSala, nuevoEstado)) {
                JOptionPane.showMessageDialog(this,
                        "Estado de la Sala " + idSala + " cambiado a " + nuevoEstadoStr,
                        "Cambio de Estado", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al cambiar el estado de la sala.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID de sala.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSalaSeleccionada() {
        int filaSeleccionada = tablaSalas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una sala para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idSalaStr = tablaSalas.getValueAt(filaSeleccionada, 0).toString();
        int idSala = Integer.parseInt(idSalaStr);
        String tipo = (String) tablaSalas.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la Sala '" + tipo + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (salaDAO.eliminarSala(idSala)) {
                JOptionPane.showMessageDialog(this, "Sala eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar. Podría estar asociada a una Cita (Integridad Referencial).",
                        "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}