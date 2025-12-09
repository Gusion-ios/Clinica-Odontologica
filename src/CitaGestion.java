import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.InputMismatchException;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class CitaGestion {

    private static final CitaDAO dao = new CitaDAO();

    private static final PacienteDAO pacienteDAO = new PacienteDAO();
    private static final OdontologoDAO odontologoDAO = new OdontologoDAO();
    private static final SalaDAO salaDAO = new SalaDAO();
    private static final TratamientoDAO tratamientoDAO = new TratamientoDAO();
    private static final HerramientaDAO herramientaDAO = new HerramientaDAO();

    private static final Scanner leer = new Scanner(System.in);
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n===== MENÚ DE GESTIÓN DE CITAS =====");
            System.out.println("1. Reservar nueva cita");
            System.out.println("2. Listar todas las citas");
            System.out.println("3. Buscar cita por ID");
            System.out.println("4. Editar fecha y hora de una cita");
            System.out.println("5. Cancelar/Eliminar cita");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = leer.nextInt();
                leer.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("⚠ Entrada inválida. Intente de nuevo.");
                leer.nextLine();
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> reservarCita();
                case 2 -> listarCitas();
                case 3 -> buscarCitaMenu();
                case 4 -> editarFechaCitaMenu();
                case 5 -> cancelarCitaMenu();
                case 0 -> System.out.println("Regresando...");
                default -> {
                    if (opcion != -1) System.out.println("Opción inválida. Intente de nuevo.");
                }
            }
        } while (opcion != 0);
    }

    private static void reservarCita() {
        System.out.println("\n=== RESERVA DE NUEVA CITA ===");
        try {
            System.out.print("Ingrese DNI del paciente: ");
            int dni = leer.nextInt();
            Paciente paciente = pacienteDAO.buscarPorDNI(dni);
            if (paciente == null) {
                System.out.println("Error: Paciente con DNI " + dni + " no encontrado.");
                leer.nextLine();
                return;
            }
            leer.nextLine();

            System.out.print("Ingrese fecha y hora (formato YYYY-MM-DD HH:MM): ");
            String fechaStr = leer.nextLine();
            Date fecha = FORMATO_FECHA.parse(fechaStr);

            System.out.print("Ingrese ID del odontólogo: ");
            int idOdontologo = leer.nextInt();
            Odontologo odontologo = odontologoDAO.buscarPorId(idOdontologo);
            if (odontologo == null) {
                System.out.println("Error: Odontólogo con ID " + idOdontologo + " no encontrado.");
                return;
            }

            System.out.print("Ingrese ID de la sala: ");
            int idSala = leer.nextInt();
            Sala sala = salaDAO.buscarPorId(idSala);
            if (sala == null) {
                System.out.println("Error: Sala con ID " + idSala + " no encontrada.");
                return;
            }

            System.out.print("Ingrese ID del tratamiento: ");
            int idTratamiento = leer.nextInt();
            Tratamiento tratamiento = tratamientoDAO.buscarPorId(idTratamiento);
            if (tratamiento == null) {
                System.out.println("Error: Tratamiento con ID " + idTratamiento + " no encontrado.");
                return;
            }

            System.out.print("Ingrese ID de la herramienta: ");
            int idHerramienta = leer.nextInt();
            Herramienta herramienta = herramientaDAO.buscarPorId(idHerramienta);
            if (herramienta == null) {
                System.out.println("Error: Herramienta con ID " + idHerramienta + " no encontrada.");
                return;
            }
            leer.nextLine();

            if (!dao.verificarDisponibilidad(fecha, idSala, idOdontologo)) {
                System.out.println("Error de Conflicto: La sala o el odontólogo ya están ocupados en esa fecha/hora.");
                return;
            }

            Cita nuevaCita = new Cita(fecha, paciente, herramienta, tratamiento, sala, odontologo);

            if (dao.insertarCita(nuevaCita)) {
                System.out.println("Cita reservada correctamente.");
                System.out.println("ID Cita asignado: " + nuevaCita.getnCita());
            } else {
                System.out.println("Error desconocido al intentar insertar la cita en la DB.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número. Intente de nuevo.");
            leer.nextLine();
        } catch (ParseException e) {
            System.out.println("Error: Formato de fecha y hora inválido. Use YYYY-MM-DD HH:MM.");
        }
    }

    private static void listarCitas() {
        System.out.println("\n=== LISTA DE CITAS ===");

        Set<Cita> lista = dao.listarCitas();

        if (lista.isEmpty()) {
            System.out.println("No hay citas registradas en la DB.");
        } else {
            for (Cita c : lista) {
                System.out.println(c);
            }
        }
    }

    private static void buscarCitaMenu() {
        System.out.print("Ingrese ID de la cita a buscar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Cita c = dao.buscarCitaPorId(id);

        if (c != null) {
            System.out.println("\n=== CITA ENCONTRADA ===");
            System.out.println(c);
        } else {
            System.out.println("Error: Cita no encontrada.");
        }
    }

    private static void editarFechaCitaMenu() {
        System.out.print("Ingrese ID de la cita a editar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Cita c = dao.buscarCitaPorId(id);

        if (c == null) {
            System.out.println("Error: Cita no encontrada.");
            return;
        }

        try {
            System.out.println("Cita ID " + id + ". Fecha actual: " + FORMATO_FECHA.format(c.getFecha()));
            System.out.print("Ingrese la NUEVA fecha y hora (YYYY-MM-DD HH:MM): ");
            String fechaStr = leer.nextLine();
            Date nuevaFecha = FORMATO_FECHA.parse(fechaStr);

            if (!dao.verificarDisponibilidad(nuevaFecha, c.getSala().getIdSala(), c.getOdontologo().getIdOdontologo())) {
                System.out.println("Error de Conflicto: La sala o el odontólogo ya están ocupados en esa nueva fecha/hora.");
                return;
            }

            if (dao.actualizarFechaCita(id, nuevaFecha)) {
                System.out.println("Fecha de cita actualizada correctamente.");
            } else {
                System.out.println("Error al actualizar la fecha en la DB.");
            }

        } catch (ParseException e) {
            System.out.println("Error: Formato de fecha y hora inválido. Use YYYY-MM-DD HH:MM.");
        }
    }

    private static void cancelarCitaMenu() {
        System.out.print("Ingrese ID de la cita a cancelar: ");
        int id = leer.nextInt();
        leer.nextLine();

        if (dao.cancelarCita(id)) {
            System.out.println("Cita cancelada correctamente.");
        } else {
            System.out.println("Error: Cita no encontrada o error en DB.");
        }
    }
}