import java.util.List;
import java.util.Scanner;

public class ClinicaGestion {

    private static final ClinicaDAO dao = new ClinicaDAO();
    private static final Scanner leer = new Scanner(System.in);

    public static void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n========= MENÚ DE CLÍNICAS (JDBC) =========");
            System.out.println("1. Registrar Clínica");
            System.out.println("2. Listar Clínicas");
            System.out.println("3. Buscar Clínica por ID");
            System.out.println("4. Editar Clínica");
            System.out.println("5. Eliminar Clínica");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = leer.nextInt();
                leer.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("⚠ Entrada inválida. Intente de nuevo.");
                leer.nextLine();
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> registrarClinica();
                case 2 -> listarClinicas();
                case 3 -> buscarClinicaPorIdMenu();
                case 4 -> editarClinica();
                case 5 -> eliminarClinica();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> {
                    if (opcion != -1) System.out.println("Opción no válida.");
                }
            }

        } while (opcion != 0);
    }

    private static void registrarClinica() {
        System.out.println("=== Registrar Nueva Clínica ===");
        try {
            System.out.print("Nombre: ");
            String nombre = leer.nextLine();
            System.out.print("RUC (11 dígitos): ");
            String ruc = leer.nextLine();
            System.out.print("Teléfono: ");
            String telefono = leer.nextLine();
            System.out.print("Dirección: ");
            String direccion = leer.nextLine();

            Clinica nueva = new Clinica(nombre, ruc, telefono, direccion);

            if (!nueva.esRucValido()) {
                System.out.println("RUC inválido. Debe tener 11 caracteres.");
                return;
            }

            if (dao.insertarClinica(nueva)) {
                System.out.println("Clínica registrada con éxito. ID: " + nueva.getIdClinica());
            } else {
                System.out.println("Error al registrar la clínica. RUC duplicado o error de DB.");
            }
        } catch (Exception e) {
            System.out.println("Error en el ingreso de datos: " + e.getMessage());
        }
    }

    private static void listarClinicas() {
        System.out.println("\n=== LISTA DE CLÍNICAS ===");
        List<Clinica> lista = dao.listarClinicas();

        if (lista.isEmpty()) {
            System.out.println("No hay clínicas registradas en la DB.");
        } else {
            for (Clinica c : lista) {
                System.out.println(c);
            }
        }
    }

    private static void buscarClinicaPorIdMenu() {
        System.out.print("Ingrese ID de la clínica a buscar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Clinica c = dao.buscarPorId(id);

        if (c != null) {
            System.out.println("Clínica encontrada: " + c);
        } else {
            System.out.println("Clínica no encontrada.");
        }
    }

    private static void editarClinica() {
        System.out.print("Ingrese ID de la clínica a editar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Clinica c = dao.buscarPorId(id);

        if (c == null) {
            System.out.println("Clínica no encontrada.");
            return;
        }

        System.out.println("Clínica actual: " + c);

        System.out.print("Nuevo nombre (" + c.getNombre() + "): ");
        c.setNombre(leer.nextLine());
        System.out.print("Nuevo RUC (" + c.getRUC() + "): ");
        c.setRUC(leer.nextLine());
        System.out.print("Nuevo Teléfono (" + c.getTelefono() + "): ");
        c.setTelefono(leer.nextLine());
        System.out.print("Nueva Dirección (" + c.getDireccion() + "): ");
        c.setDireccion(leer.nextLine());

        if (dao.editarClinica(c)) {
            System.out.println("Clínica editada correctamente.");
        } else {
            System.out.println("Error al editar la clínica en la DB.");
        }
    }

    private static void eliminarClinica() {
        System.out.print("Ingrese ID de la clínica a eliminar: ");
        int id = leer.nextInt();
        leer.nextLine();

        if (dao.eliminarClinica(id)) {
            System.out.println("Clínica eliminada correctamente.");
        } else {
            System.out.println("Error al eliminar la clínica. Podría tener Sedes asociadas (FK).");
        }
    }
}