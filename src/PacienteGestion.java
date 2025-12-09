import java.util.Scanner;
import java.util.Set;
import java.util.InputMismatchException;

public class PacienteGestion {

    private static final PacienteDAO dao = new PacienteDAO();
    private static final Scanner leer = new Scanner(System.in);

    public static void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n===== MENÚ DE PACIENTES (JDBC) =====");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Eliminar paciente");
            System.out.println("3. Modificar paciente");
            System.out.println("4. Buscar paciente por DNI");
            System.out.println("5. Listar pacientes");
            System.out.println("6. Contar pacientes");
            System.out.println("0. Volver al menú principal");

            System.out.print("Seleccione una opción: ");
            try {
                opcion = leer.nextInt();
                leer.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Intente de nuevo.");
                leer.nextLine();
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> registrarPaciente();
                case 2 -> eliminarPaciente();
                case 3 -> modificarPacienteMenu();
                case 4 -> buscarPaciente();
                case 5 -> listarPacientes();
                case 6 -> contarPacientes();
                case 0 -> System.out.println("Regresando al menú principal...");
                default -> {
                    if (opcion != -1) System.out.println("Opción inválida, intente nuevamente.");
                }
            }
        } while (opcion != 0);
    }

    private static void registrarPaciente() {
        System.out.println("=== Registrar Paciente ===");
        try {
            System.out.print("Ingrese DNI: ");
            int dni = leer.nextInt();
            leer.nextLine();

            if (dao.buscarPorDNI(dni) != null) {
                System.out.println("Error: El DNI ya está registrado.");
                return;
            }

            System.out.print("Nombres: ");
            String nom = leer.nextLine();
            System.out.print("Apellidos: ");
            String ape = leer.nextLine();
            System.out.print("Edad: ");
            int edad = leer.nextInt();
            leer.nextLine();
            System.out.print("Género: ");
            String genero = leer.nextLine();
            System.out.print("Teléfono: ");
            int tel = leer.nextInt();
            leer.nextLine();
            System.out.print("ID de Alergia (0 si no aplica): ");
            int idAlergia = leer.nextInt();
            leer.nextLine();

            Paciente nuevo = new Paciente(dni, nom, ape, edad, genero, tel, idAlergia);

            if (dao.insertarPaciente(nuevo)) {
                System.out.println("Paciente registrado correctamente.");
            } else {
                System.out.println("Error al registrar en la DB. Verifique el ID de Alergia.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número. Operación cancelada.");
            leer.nextLine();
        }
    }

    private static void eliminarPaciente() {
        System.out.print("Ingrese el DNI del paciente a eliminar: ");
        int dniEliminar = leer.nextInt();
        leer.nextLine();

        if (dao.eliminarPaciente(dniEliminar)) {
            System.out.println("Paciente eliminado correctamente.");
        } else {
            System.out.println("Error: Paciente no encontrado o tiene registros asociados (citas/tratamientos).");
        }
    }

    private static void modificarPacienteMenu() {
        System.out.print("Ingrese el DNI del paciente a modificar: ");
        int dni = leer.nextInt();
        leer.nextLine();

        Paciente pacienteModificar = dao.buscarPorDNI(dni);

        if (pacienteModificar == null) {
            System.out.println("No se encontró un paciente con ese DNI.");
            return;
        }

        System.out.println("Paciente actual: " + pacienteModificar);

        try {
            System.out.print("Nuevo nombre (" + pacienteModificar.getNombres() + "): ");
            String nuevoNom = leer.nextLine();
            if (!nuevoNom.isEmpty()) pacienteModificar.setNombres(nuevoNom);

            System.out.print("Nuevo apellido (" + pacienteModificar.getApellidos() + "): ");
            String nuevoApe = leer.nextLine();
            if (!nuevoApe.isEmpty()) pacienteModificar.setApellidos(nuevoApe);

            System.out.print("Nueva edad (" + pacienteModificar.getEdad() + "): ");
            String nuevaEdadStr = leer.nextLine();
            if (!nuevaEdadStr.isEmpty()) pacienteModificar.setEdad(Integer.parseInt(nuevaEdadStr));

            System.out.print("Nuevo género (" + pacienteModificar.getGenero() + "): ");
            String nuevoGen = leer.nextLine();
            if (!nuevoGen.isEmpty()) pacienteModificar.setGenero(nuevoGen);

            System.out.print("Nuevo teléfono (" + pacienteModificar.getTelefono() + "): ");
            String nuevoTelStr = leer.nextLine();
            if (!nuevoTelStr.isEmpty()) pacienteModificar.setTelefono(Integer.parseInt(nuevoTelStr));

            System.out.print("Nuevo ID de Alergia (" + pacienteModificar.getIdAlergia() + "): ");
            String nuevoIdAlergiaStr = leer.nextLine();
            if (!nuevoIdAlergiaStr.isEmpty()) pacienteModificar.setIdAlergia(Integer.parseInt(nuevoIdAlergiaStr));

            if (dao.modificarPaciente(pacienteModificar)) {
                System.out.println("Datos del paciente modificados correctamente.");
            } else {
                System.out.println("Error al modificar en la DB. Verifique el ID de Alergia.");
            }

        } catch (Exception e) {
            System.out.println("Error en el formato de entrada. Operación cancelada.");
        }
    }

    private static void buscarPaciente() {
        System.out.print("Ingrese el DNI del paciente a buscar: ");
        int dni = leer.nextInt();
        leer.nextLine();

        Paciente p = dao.buscarPorDNI(dni);

        if (p != null) {
            System.out.println(p);
        } else {
            System.out.println("No se encontró un paciente con ese DNI.");
        }
    }

    private static void listarPacientes() {
        System.out.println("\n--- Lista de Pacientes ---");

        Set<Paciente> lista = dao.listarPacientes();

        if (lista.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }

        for (Paciente p : lista) {
            System.out.println(p);
        }
    }

    private static void contarPacientes() {
        int total = dao.contarPacientes();
        System.out.println("Total de pacientes registrados: " + total);
    }
}