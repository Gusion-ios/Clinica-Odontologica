import java.util.Scanner;
import java.util.Set;
import java.util.InputMismatchException;

public class SalaGestion {

    private static final SalaDAO dao = new SalaDAO();
    private static final Scanner leer = new Scanner(System.in);

    public static void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n========== MENÚ DE SALAS (JDBC) ==========");
            System.out.println("1. Registrar sala");
            System.out.println("2. Listar salas");
            System.out.println("3. Buscar sala por ID");
            System.out.println("4. Editar sala");
            System.out.println("5. Cambiar estado (Libre/Ocupada)");
            System.out.println("6. Eliminar sala");
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
                case 1 -> registrarSala();
                case 2 -> listarSalas();
                case 3 -> buscarSalaPorIdMenu();
                case 4 -> editarSala();
                case 5 -> cambiarEstadoMenu();
                case 6 -> eliminarSala();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> {
                    if (opcion != -1) System.out.println("Opción inválida.");
                }
            }

        } while (opcion != 0);
    }

    private static void registrarSala() {
        System.out.println("=== REGISTRAR SALA ===");
        try {
            System.out.print("Tipo de sala (Consultorio, Reunión, etc.): ");
            String tipo = leer.nextLine();

            System.out.print("Capacidad: ");
            int capacidad = leer.nextInt();
            leer.nextLine();

            System.out.print("Ubicación: ");
            String ubicacion = leer.nextLine();

            System.out.print("Número de sede (ID de la sede): ");
            int sede = leer.nextInt();
            leer.nextLine();

            Sala nueva = new Sala(tipo, capacidad, true, ubicacion, sede);

            if (dao.insertarSala(nueva)) {
                System.out.println("Sala registrada con éxito. ID = " + nueva.getIdSala());
            } else {
                System.out.println("Error al registrar la sala. Verifique si el ID de Sede existe.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número. Operación cancelada.");
            leer.nextLine();
        }
    }

    private static void listarSalas() {
        System.out.println("\n=== LISTA DE SALAS ===");
        Set<Sala> lista = dao.listarSalas();

        if (lista.isEmpty()) {
            System.out.println("No hay salas registradas en la DB.");
            return;
        }

        for (Sala s : lista) {
            System.out.println(s);
        }
    }

    private static void buscarSalaPorIdMenu() {
        try {
            System.out.print("Ingrese ID de la sala: ");
            int idBuscar = leer.nextInt();
            leer.nextLine();
            Sala s = dao.buscarPorId(idBuscar);

            if (s != null) {
                System.out.println("Sala encontrada:");
                System.out.println(s);
            } else {
                System.out.println("No se encontró la sala.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número.");
            leer.nextLine();
        }
    }

    private static void editarSala() {
        try {
            System.out.print("Ingrese ID de la sala a editar: ");
            int id = leer.nextInt();
            leer.nextLine();

            Sala s = dao.buscarPorId(id);

            if (s == null) {
                System.out.println("Sala no encontrada.");
                return;
            }

            System.out.println("\n--- Edición de Sala ID: " + id + " ---");

            System.out.print("Nuevo tipo (" + s.getTipo() + "): ");
            String nuevoTipo = leer.nextLine();
            if (!nuevoTipo.isEmpty()) s.setTipo(nuevoTipo);

            System.out.print("Nueva capacidad (" + s.getCapacidad() + "): ");
            String nuevaCapacidadStr = leer.nextLine();
            if (!nuevaCapacidadStr.isEmpty()) s.setCapacidad(Integer.parseInt(nuevaCapacidadStr));

            System.out.print("Nueva ubicación (" + s.getUbicacion() + "): ");
            String nuevaUbicacion = leer.nextLine();
            if (!nuevaUbicacion.isEmpty()) s.setUbicacion(nuevaUbicacion);

            System.out.print("Nueva sede ID (" + s.getSede() + "): ");
            String nuevaSedeStr = leer.nextLine();
            if (!nuevaSedeStr.isEmpty()) s.setSede(Integer.parseInt(nuevaSedeStr));

            if (dao.editarSala(s)) {
                System.out.println("Sala actualizada correctamente.");
            } else {
                System.out.println("Error al actualizar en la DB.");
            }

        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Error: Ingrese un valor numérico correcto. Operación cancelada.");
            leer.nextLine();
        }
    }

    private static void cambiarEstadoMenu() {
        try {
            System.out.print("Ingrese ID de la sala: ");
            int id = leer.nextInt();
            leer.nextLine();

            Sala s = dao.buscarPorId(id);

            if (s == null) {
                System.out.println("Sala no encontrada.");
                return;
            }

            System.out.println("Estado actual: " + (s.isEstadoLibre() ? "Libre" : "Ocupada"));
            System.out.println("1) Marcar como Libre");
            System.out.println("2) Marcar como Ocupada");
            System.out.print("Seleccione opción: ");
            int op = leer.nextInt();
            leer.nextLine();

            boolean nuevoEstado = (op == 1);

            if (dao.cambiarEstado(id, nuevoEstado)) {
                System.out.println("Estado actualizado a " + (nuevoEstado ? "Libre" : "Ocupada"));
            } else {
                System.out.println("Error al actualizar el estado en la DB.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número. Operación cancelada.");
            leer.nextLine();
        }
    }

    private static void eliminarSala() {
        try {
            System.out.print("Ingrese ID de la sala a eliminar: ");
            int id = leer.nextInt();
            leer.nextLine();

            if (dao.eliminarSala(id)) {
                System.out.println("Sala eliminada.");
            } else {
                System.out.println("Error al eliminar. La sala podría estar asociada a una Cita (FK).");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número. Operación cancelada.");
            leer.nextLine();
        }
    }
}