import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class TratamientoGestion {

    private static final TratamientoDAO dao = new TratamientoDAO();
    private static final Scanner leer = new Scanner(System.in);

    public static void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n========== MENÚ DE TRATAMIENTOS (JDBC) ==========");
            System.out.println("1. Registrar tratamiento");
            System.out.println("2. Listar tratamientos");
            System.out.println("3. Buscar tratamiento por ID");
            System.out.println("4. Cambiar estado");
            System.out.println("5. Actualizar duración");
            System.out.println("6. Listar tratamientos por DNI del paciente");
            System.out.println("7. Eliminar tratamiento");
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
                case 1 -> registrarTratamiento();
                case 2 -> listarTratamientos();
                case 3 -> buscarTratamientoPorIdMenu();
                case 4 -> cambiarEstado();
                case 5 -> actualizarDuracion();
                case 6 -> listarPorPacienteMenu();
                case 7 -> eliminarTratamiento();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> {
                    if (opcion != -1) System.out.println("Opción inválida.");
                }
            }
        } while (opcion != 0);
    }

    private static void registrarTratamiento() {
        System.out.println("=== Registrar Nuevo Tratamiento ===");
        try {
            System.out.print("Motivo del tratamiento: ");
            String motivo = leer.nextLine();

            System.out.print("Duración en sesiones: ");
            int duracion = leer.nextInt();
            leer.nextLine();

            System.out.print("DNI del paciente: ");
            int dniPaciente = leer.nextInt();
            leer.nextLine();

            System.out.print("ID del producto utilizado: ");
            int idProducto = leer.nextInt();
            leer.nextLine();

            Tratamiento nuevo = new Tratamiento(motivo, "Activo", duracion, dniPaciente, idProducto);

            if (dao.insertarTratamiento(nuevo)) {
                System.out.println("Tratamiento registrado con éxito. ID: " + nuevo.getIdTratamiento());
            } else {
                System.out.println("Error al registrar. Verifique si el DNI del Paciente y el ID de Producto existen.");
            }
        } catch (InputMismatchException e) {
            System.out.println("⚠ Error en formato numérico.");
            leer.nextLine();
        }
    }

    private static void listarTratamientos() {
        System.out.println("\n=== LISTA DE TRATAMIENTOS ===");
        List<Tratamiento> lista = dao.listarTratamientos();

        if (lista.isEmpty()) {
            System.out.println("No existen tratamientos registrados en la DB.");
            return;
        }
        for (Tratamiento t : lista) {
            System.out.println(t);
        }
    }

    private static void buscarTratamientoPorIdMenu() {
        System.out.print("Ingrese ID del tratamiento: ");
        int idBuscar = leer.nextInt();
        leer.nextLine();
        Tratamiento buscado = dao.buscarPorId(idBuscar);

        if (buscado != null) {
            System.out.println("Tratamiento encontrado:");
            System.out.println(buscado);
        } else {
            System.out.println("No se encontró el tratamiento.");
        }
    }

    private static void cambiarEstado() {
        System.out.print("Ingrese ID del tratamiento: ");
        int id = leer.nextInt();
        leer.nextLine();

        Tratamiento t = dao.buscarPorId(id);

        if (t == null) {
            System.out.println("No existe un tratamiento con ese ID.");
            return;
        }

        System.out.println("Estado actual: " + t.getEstado());
        System.out.println("Ingrese nuevo estado (Activo / En proceso / Finalizado):");
        String nuevoEstado = leer.nextLine();

        if (dao.cambiarEstado(id, nuevoEstado)) {
            System.out.println("Estado actualizado a '" + nuevoEstado + "' correctamente.");
        } else {
            System.out.println("Error al actualizar el estado en la DB.");
        }
    }

    private static void actualizarDuracion() {
        System.out.print("ID del tratamiento: ");
        int id = leer.nextInt();
        leer.nextLine();

        Tratamiento t = dao.buscarPorId(id);

        if (t == null) {
            System.out.println("Tratamiento no encontrado.");
            return;
        }

        System.out.print("Nueva duración en sesiones (Actual: " + t.getDuracionSesiones() + "): ");
        int nuevaDuracion = leer.nextInt();
        leer.nextLine();

        if (dao.actualizarDuracion(id, nuevaDuracion)) {
            System.out.println("Duración actualizada.");
        } else {
            System.out.println("Error al actualizar la duración en la DB.");
        }
    }

    private static void listarPorPacienteMenu() {
        System.out.print("DNI del paciente: ");
        int dni = leer.nextInt();
        leer.nextLine();

        System.out.println("\n=== TRATAMIENTOS DEL PACIENTE DNI: " + dni + " ===");

        List<Tratamiento> lista = dao.listarTratamientosPorPaciente(dni);

        if (lista.isEmpty()) {
            System.out.println("Este paciente no tiene tratamientos registrados.");
        } else {
            for (Tratamiento t : lista) {
                System.out.println(t);
            }
        }
    }

    private static void eliminarTratamiento() {
        System.out.print("Ingrese ID del tratamiento a eliminar: ");
        int id = leer.nextInt();
        leer.nextLine();

        if (dao.eliminarTratamiento(id)) {
            System.out.println("Tratamiento eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar el tratamiento.");
        }
    }
}