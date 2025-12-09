import java.util.List;
import java.util.Scanner;

public class SedeGestion {

    private static final SedeDAO dao = new SedeDAO();
    private static final Scanner leer = new Scanner(System.in);

    public static void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n========= MENÚ DE SEDES (JDBC) =========");
            System.out.println("1. Registrar sede");
            System.out.println("2. Listar todas las sedes");
            System.out.println("3. Editar sede");
            System.out.println("4. Cambiar disponibilidad");
            System.out.println("5. Listar sedes por clínica");
            System.out.println("6. Eliminar sede");
            System.out.println("7. Estadísticas del sistema");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = leer.nextInt();
                leer.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrada inválida. Intente de nuevo.");
                leer.nextLine();
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> registrarSede();
                case 2 -> listarSedes();
                case 3 -> editarSede();
                case 4 -> cambiarDisponibilidad();
                case 5 -> listarPorClinicaMenu();
                case 6 -> eliminarSede();
                case 7 -> estadisticas();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    private static void registrarSede() {
        System.out.println("=== Registrar Nueva Sede ===");
        try {
            System.out.print("Ingrese capacidad: ");
            int capacidad = leer.nextInt();
            leer.nextLine();

            System.out.print("Ubicación: ");
            String ubicacion = leer.nextLine();

            System.out.print("ID de la clínica a la que pertenece: ");
            int idClinica = leer.nextInt();
            leer.nextLine();

            Sede nueva = new Sede(capacidad, true, ubicacion, idClinica);

            if (dao.insertarSede(nueva)) {
                System.out.println("Sede registrada con éxito. ID: " + nueva.getIdSede());
            } else {
                System.out.println("Error al registrar la sede. Verifique si la Clínica existe.");
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Entrada de formato incorrecto.");
            leer.nextLine();
        }
    }

    private static void listarSedes() {
        System.out.println("\n=== LISTA DE SEDES ===");
        List<Sede> lista = dao.listarSedes();

        if (lista.isEmpty()) {
            System.out.println("No hay sedes registradas en la DB.");
        } else {
            for (Sede s : lista) {
                System.out.println(s);
            }
        }
    }

    private static void editarSede() {
        System.out.print("Ingrese ID de la sede a editar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Sede s = dao.buscarPorId(id);

        if (s == null) {
            System.out.println("Sede no encontrada.");
            return;
        }

        System.out.println("Sede actual: " + s);

        try {
            System.out.print("Nueva capacidad (" + s.getCapacidad() + "): ");
            s.setCapacidad(leer.nextInt());
            leer.nextLine();

            System.out.print("Nueva ubicación (" + s.getUbicacion() + "): ");
            s.setUbicacion(leer.nextLine());

            System.out.print("Nuevo ID de clínica (" + s.getIdClinica() + "): ");
            s.setIdClinica(leer.nextInt());
            leer.nextLine();

            if (dao.editarSede(s)) {
                System.out.println("Sede actualizada correctamente.");
            } else {
                System.out.println("Error al actualizar en la DB.");
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("⚠ Entrada de formato incorrecto. Operación cancelada.");
            leer.nextLine();
        }
    }

    private static void cambiarDisponibilidad() {
        System.out.print("ID de sede: ");
        int id = leer.nextInt();
        leer.nextLine();

        Sede s = dao.buscarPorId(id);
        if (s == null) {
            System.out.println("Sede no encontrada.");
            return;
        }

        System.out.println("Estado actual: " + (s.isDisponible() ? "Disponible" : "No disponible"));
        System.out.println("Ingrese nuevo estado (1 = Disponible / 0 = No disponible): ");
        int estado = leer.nextInt();
        boolean nuevoEstado = (estado == 1);

        if (dao.cambiarDisponibilidad(id, nuevoEstado)) {
            System.out.println("Estado actualizado a " + (nuevoEstado ? "Disponible" : "No disponible"));
        } else {
            System.out.println("Error al cambiar el estado.");
        }
    }

    private static void eliminarSede() {
        System.out.print("ID de sede a eliminar: ");
        int id = leer.nextInt();
        leer.nextLine();

        if (dao.eliminarSede(id)) {
            System.out.println("Sede eliminada correctamente.");
        } else {
            System.out.println("Error al eliminar la sede. Podría tener Odontólogos o Salas asociadas (FK).");
        }
    }

    private static void listarPorClinicaMenu() {
        System.out.print("Ingrese ID de la clínica: ");
        int idClinica = leer.nextInt();
        leer.nextLine();

        List<Sede> lista = dao.listarPorClinica(idClinica);

        if (lista.isEmpty()) {
            System.out.println("No hay sedes registradas para esa clínica.");
        } else {
            for (Sede s : lista) {
                System.out.println(s);
            }
        }
    }

    private static void estadisticas() {
        System.out.println("\n===== ESTADÍSTICAS DEL SISTEMA (DB) =====");
        int total = dao.contarSedes();
        int disponibles = dao.contarDisponibles();
        int capacidadTotal = dao.sumarCapacidadTotal();

        System.out.println("Total de sedes: " + total);
        System.out.println("Sedes disponibles: " + disponibles);
        System.out.println("Sedes no disponibles: " + (total - disponibles));
        System.out.println("Capacidad total entre sedes: " + capacidadTotal);
        System.out.println("=========================================");
    }
}