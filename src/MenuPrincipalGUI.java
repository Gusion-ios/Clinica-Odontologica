import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipalGUI extends JFrame implements ActionListener {

    public MenuPrincipalGUI() {
        setTitle("SISTEMA DE GESTIÓN ODONTOLÓGICA - Menú Principal");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(12, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Seleccione un Módulo de Gestión", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo);

        String[] gestiones = {
                "1. Gestión de Sedes",
                "2. Gestión de Clínicas",
                "3. Gestión de Odontólogos",
                "4. Gestión de Pacientes",
                "5. Gestión de Productos",
                "6. Gestión de Tratamientos",
                "7. Gestión de Pagos",
                "8. Gestión de Alergias",
                "9. Gestión de Herramientas",
                "10. Gestión de Salas",
                "11. GESTIÓN DE CITAS (Central)"
        };

        for (String nombre : gestiones) {
            JButton boton = new JButton(nombre);
            boton.setActionCommand(nombre);
            boton.addActionListener(this);
            panel.add(boton);
        }

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        switch (comando) {
            case "1. Gestión de Sedes":
               new SedeGUI().setVisible(true);
                break;
            case "2. Gestión de Clínicas":
                new ClinicaGUI().setVisible(true);
                break;
            case "3. Gestión de Odontólogos":
                new OdontologoGUI().setVisible(true);
                break;
            case "4. Gestión de Pacientes":
                new PacienteGUI().setVisible(true);
                break;
            case "5. Gestión de Productos":
                new ProductoGUI().setVisible(true);
                break;
            case "6. Gestión de Tratamientos":
                new TratamientoGUI().setVisible(true);
                break;
            case "7. Gestión de Pagos":
                new PagoGUI().setVisible(true);
                break;
            case "8. Gestión de Alergias":
                new AlergiaGUI().setVisible(true);
                break;
            case "9. Gestión de Herramientas":
                new HerramientaGUI().setVisible(true);
                break;
            case "10. Gestión de Salas":
                new SalaGUI().setVisible(true);
                break;
            case "11. GESTIÓN DE CITAS (Central)":
                new CitaGUI().setVisible(true);
                break;
        }
    }

}