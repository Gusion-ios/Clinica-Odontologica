import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class AlergiaGUI extends JFrame {

    private final AlergiaDAO alergiaDAO = new AlergiaDAO();
    private JTable tablaAlergias;
    private JTextField txtNombre;
    private JTextField txtCausas;
    private JTextField txtConsecuencias;
    private JTextField txtRecomendacion;

    public AlergiaGUI() {
        setTitle("8. Gestión de Alergias (JDBC)");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(crearTablaAlergias()), BorderLayout.CENTER);
        add(crearPanelAcciones(), BorderLayout.SOUTH);

        cargarDatosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        txtNombre = new JTextField(20);
        txtCausas = new JTextField(20);
        txtConsecuencias = new JTextField(20);
        txtRecomendacion = new JTextField(30);

        panel.add(new JLabel("Nombre Alergia:"));
        panel.add(txtNombre);

        panel.add(new JLabel("Causas:"));
        panel.add(txtCausas);

        panel.add(new JLabel("Consecuencias:"));
        panel.add(txtConsecuencias);

        panel.add(new JLabel("Recomendación:"));
        panel.add(txtRecomendacion);

        JButton btnRegistrar = new JButton("Registrar Alergia");
        btnRegistrar.addActionListener(e -> registrarNuevaAlergia());
        panel.add(btnRegistrar);

        return panel;
    }

    private JTable crearTablaAlergias() {
        String[] columnas = {"ID", "Nombre", "Causas", "Consecuencias", "Recomendación"};
        tablaAlergias = new JTable(new DefaultTableModel(new Object[0][0], columnas));
        return tablaAlergias;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarDatosTabla());
        panel.add(btnActualizar);

        JButton btnEditar = new JButton("Editar Alergia");
        btnEditar.addActionListener(e -> editarAlergiaSeleccionada());
        panel.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar Alergia");
        btnEliminar.addActionListener(e -> eliminarAlergiaSeleccionada());
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatosTabla() {
        try {
            Set<Alergia> setAlergias = alergiaDAO.listarAlergias();
            List<Alergia> alergias = new ArrayList<>(setAlergias);

            String[] columnas = {"ID", "Nombre", "Causas", "Consecuencias", "Recomendación"};
            Object[][] datos = new Object[alergias.size()][columnas.length];

            for (int i = 0; i < alergias.size(); i++) {
                Alergia a = alergias.get(i);
                datos[i][0] = a.getIdAlergia();
                datos[i][1] = a.getNombreAlergia();
                datos[i][2] = a.getCausas();
                datos[i][3] = a.getConsecuencias();
                datos[i][4] = a.getRecomendacion();
            }

            tablaAlergias.setModel(new DefaultTableModel(datos, columnas));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevaAlergia() {
        try {
            String nombre = txtNombre.getText();
            String causas = txtCausas.getText();
            String consecuencias = txtConsecuencias.getText();
            String recomendacion = txtRecomendacion.getText();

            if (nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre de la alergia es obligatorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Alergia nueva = new Alergia(nombre, causas, consecuencias, recomendacion);

            if (alergiaDAO.insertarAlergia(nueva)) {
                JOptionPane.showMessageDialog(this, "Alergia registrada con ID: " + nueva.getIdAlergia(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
                txtNombre.setText(""); txtCausas.setText("");
                txtConsecuencias.setText(""); txtRecomendacion.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la alergia.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de ingreso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarAlergiaSeleccionada() {
        int filaSeleccionada = tablaAlergias.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una alergia para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idAlergiaStr = tablaAlergias.getValueAt(filaSeleccionada, 0).toString();
            int idAlergia = Integer.parseInt(idAlergiaStr);
            Alergia alergiaAEditar = alergiaDAO.buscarPorId(idAlergia);

            if (alergiaAEditar != null) {
                String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo Nombre:", alergiaAEditar.getNombreAlergia());
                String nuevasCausas = JOptionPane.showInputDialog(this, "Nuevas Causas:", alergiaAEditar.getCausas());
                String nuevasConsecuencias = JOptionPane.showInputDialog(this, "Nuevas Consecuencias:", alergiaAEditar.getConsecuencias());
                String nuevaRecomendacion = JOptionPane.showInputDialog(this, "Nueva Recomendación:", alergiaAEditar.getRecomendacion());

                if (nuevoNombre != null && nuevasCausas != null) {

                    alergiaAEditar.setNombreAlergia(nuevoNombre);
                    alergiaAEditar.setCausas(nuevasCausas);
                    alergiaAEditar.setConsecuencias(nuevasConsecuencias);
                    alergiaAEditar.setRecomendacion(nuevaRecomendacion);

                    if (alergiaDAO.editarAlergia(alergiaAEditar)) {
                        JOptionPane.showMessageDialog(this, "Alergia actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarDatosTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar la alergia.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID de alergia.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al editar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarAlergiaSeleccionada() {
        int filaSeleccionada = tablaAlergias.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una alergia para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idAlergiaStr = tablaAlergias.getValueAt(filaSeleccionada, 0).toString();
        int idAlergia = Integer.parseInt(idAlergiaStr);
        String nombre = (String) tablaAlergias.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la alergia '" + nombre + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (alergiaDAO.eliminarAlergia(idAlergia)) {
                JOptionPane.showMessageDialog(this, "Alergia eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar. La alergia podría estar asociada a un Paciente (Integridad Referencial).",
                        "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}