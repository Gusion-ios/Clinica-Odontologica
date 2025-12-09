import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class CitaGUI extends JFrame {

    private final CitaDAO citaDAO = new CitaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final OdontologoDAO odontologoDAO = new OdontologoDAO();
    private final TratamientoDAO tratamientoDAO = new TratamientoDAO();
    private final HerramientaDAO herramientaDAO = new HerramientaDAO();
    private final SalaDAO salaDAO = new SalaDAO();

    private JTable tablaCitas;
    private JTextField txtDniPaciente;
    private JTextField txtIdOdontologo;
    private JTextField txtIdTratamiento;
    private JTextField txtIdHerramienta;
    private JTextField txtIdSala;
    private JTextField txtFechaHora;

    private static final SimpleDateFormat FORMATO_FECHA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public CitaGUI() {
        setTitle("11. GESTIÓN DE CITAS (JDBC) - Módulo Central");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(crearTablaCitas()), BorderLayout.CENTER);
        add(crearPanelAcciones(), BorderLayout.SOUTH);

        cargarDatosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(3, 6, 10, 10));

        txtDniPaciente = new JTextField(10);
        txtIdOdontologo = new JTextField(5);
        txtIdTratamiento = new JTextField(5);
        txtIdHerramienta = new JTextField(5);
        txtIdSala = new JTextField(5);
        txtFechaHora = new JTextField(FORMATO_FECHA_HORA.format(new Date()));

        panel.add(new JLabel("DNI Paciente:"));
        panel.add(txtDniPaciente);
        panel.add(new JLabel("ID Odontólogo:"));
        panel.add(txtIdOdontologo);
        panel.add(new JLabel("ID Tratamiento:"));
        panel.add(txtIdTratamiento);

        panel.add(new JLabel("ID Herramienta:"));
        panel.add(txtIdHerramienta);
        panel.add(new JLabel("ID Sala:"));
        panel.add(txtIdSala);
        panel.add(new JLabel("Fecha y Hora (DD/MM/YYYY HH:MM):"));
        panel.add(txtFechaHora);

        JButton btnRegistrar = new JButton("Registrar Cita");
        btnRegistrar.addActionListener(e -> registrarNuevaCita());
        panel.add(btnRegistrar);

        JLabel lblInfo = new JLabel("<html><font color='blue'>Asegúrese de que el DNI, ID Odontólogo, Tratamiento, Herramienta y Sala existan.</font></html>");
        panel.add(lblInfo);

        return panel;
    }

    private JTable crearTablaCitas() {
        String[] columnas = {"ID Cita", "Fecha/Hora", "DNI Paciente", "ID Odontólogo", "ID Tratamiento", "ID Herramienta", "ID Sala"};
        tablaCitas = new JTable(new DefaultTableModel(new Object[0][0], columnas));
        return tablaCitas;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarDatosTabla());
        panel.add(btnActualizar);

        JButton btnEditarFecha = new JButton("Reagendar Cita");
        btnEditarFecha.addActionListener(e -> reagendarCitaSeleccionada());
        panel.add(btnEditarFecha);

        JButton btnEliminar = new JButton("Eliminar/Cancelar Cita");
        btnEliminar.addActionListener(e -> eliminarCitaSeleccionada());
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatosTabla() {
        try {
            Set<Cita> setCitas = citaDAO.listarCitas();
            List<Cita> citas = new ArrayList<>(setCitas);

            String[] columnas = {"ID Cita", "Fecha/Hora", "DNI Paciente", "ID Odontólogo", "ID Tratamiento", "ID Herramienta", "ID Sala"};
            Object[][] datos = new Object[citas.size()][columnas.length];

            for (int i = 0; i < citas.size(); i++) {
                Cita c = citas.get(i);
                datos[i][0] = c.getnCita();
                datos[i][1] = FORMATO_FECHA_HORA.format(c.getFecha());
                datos[i][2] = c.getPaciente().getDNIPaciente();
                datos[i][3] = c.getOdontologo().getIdOdontologo();
                datos[i][4] = c.getTratamiento().getIdTratamiento();
                datos[i][5] = c.getHerramientas().getIdHerramienta();
                datos[i][6] = c.getSala().getIdSala();
            }

            tablaCitas.setModel(new DefaultTableModel(datos, columnas));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevaCita() {
        try {
            int dniPaciente = Integer.parseInt(txtDniPaciente.getText());
            int idOdontologo = Integer.parseInt(txtIdOdontologo.getText());
            int idTratamiento = Integer.parseInt(txtIdTratamiento.getText());
            int idHerramienta = Integer.parseInt(txtIdHerramienta.getText());
            int idSala = Integer.parseInt(txtIdSala.getText());
            Date fecha = FORMATO_FECHA_HORA.parse(txtFechaHora.getText());

            Paciente paciente = pacienteDAO.buscarPorDNI(dniPaciente);
            Odontologo odontologo = odontologoDAO.buscarPorId(idOdontologo);
            Tratamiento tratamiento = tratamientoDAO.buscarPorId(idTratamiento);
            Herramienta herramienta = herramientaDAO.buscarPorId(idHerramienta);
            Sala sala = salaDAO.buscarPorId(idSala);

            if (paciente == null || odontologo == null || tratamiento == null || herramienta == null || sala == null) {
                JOptionPane.showMessageDialog(this, "Verifique las Claves Foráneas: Algún recurso (Paciente, Odontólogo, Sala, etc.) no existe.", "Error de FK", JOptionPane.ERROR_MESSAGE); return;
            }

            if (!citaDAO.verificarDisponibilidad(fecha, idSala, idOdontologo)) {
                JOptionPane.showMessageDialog(this, "La sala o el odontólogo ya están ocupados en esa fecha y hora.", "Conflicto de Horario", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Cita nueva = new Cita(fecha, paciente, herramienta, tratamiento, sala, odontologo);

            if (citaDAO.insertarCita(nueva)) {
                JOptionPane.showMessageDialog(this, "Cita registrada con éxito. ID: " + nueva.getnCita(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
                txtDniPaciente.setText(""); txtIdOdontologo.setText("");
                txtIdTratamiento.setText(""); txtIdHerramienta.setText("");
                txtIdSala.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la cita (Error de DB).", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Todos los IDs y el DNI deben ser números válidos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "El formato de fecha/hora debe ser DD/MM/YYYY HH:MM.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void reagendarCitaSeleccionada() {
        int filaSeleccionada = tablaCitas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una cita para reagendar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String idCitaStr = tablaCitas.getValueAt(filaSeleccionada, 0).toString();
            int idCita = Integer.parseInt(idCitaStr);
            Cita citaAEditar = citaDAO.buscarCitaPorId(idCita);

            if (citaAEditar != null) {
                String fechaActualStr = FORMATO_FECHA_HORA.format(citaAEditar.getFecha());
                String nuevaFechaStr = JOptionPane.showInputDialog(this,
                        "Nueva Fecha y Hora (DD/MM/YYYY HH:MM):", fechaActualStr);

                if (nuevaFechaStr != null) {
                    Date nuevaFecha = FORMATO_FECHA_HORA.parse(nuevaFechaStr);

                    int idSala = citaAEditar.getSala().getIdSala();
                    int idOdontologo = citaAEditar.getOdontologo().getIdOdontologo();

                    if (!citaDAO.verificarDisponibilidad(nuevaFecha, idSala, idOdontologo)) {
                        JOptionPane.showMessageDialog(this, "La nueva fecha/hora tiene conflicto con otro recurso (Sala u Odontólogo).", "Conflicto de Horario", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (citaDAO.actualizarFechaCita(idCita, nuevaFecha)) {
                        JOptionPane.showMessageDialog(this, "Cita reagendada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarDatosTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al reagendar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID de la cita.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "El formato de fecha/hora debe ser DD/MM/YYYY HH:MM.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarCitaSeleccionada() {
        int filaSeleccionada = tablaCitas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una cita para cancelar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idCitaStr = tablaCitas.getValueAt(filaSeleccionada, 0).toString();
        int idCita = Integer.parseInt(idCitaStr);
        String fecha = (String) tablaCitas.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cancelar la Cita ID " + idCita + " del " + fecha + "?",
                "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (citaDAO.cancelarCita(idCita)) {
                JOptionPane.showMessageDialog(this, "Cita cancelada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al cancelar la cita.",
                        "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}