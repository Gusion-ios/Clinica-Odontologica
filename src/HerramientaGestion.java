import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.InputMismatchException;
import java.text.SimpleDateFormat;

public class HerramientaGestion {

    private static final HerramientaDAO dao = new HerramientaDAO();
    private static final Scanner leer = new Scanner(System.in);
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    public static void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n===== MENÚ DE HERRAMIENTAS (JDBC) =====");
            System.out.println("1. Registrar herramienta");
            System.out.println("2. Listar todas las herramientas");
            System.out.println("3. Buscar herramienta por ID");
            System.out.println("4. Editar herramienta");
            System.out.println("5. Eliminar herramienta");
            System.out.println("6. Usar herramienta (Reducir stock)");
            System.out.println("7. Reponer herramienta (Aumentar stock)");
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
                case 1 -> registrarHerramienta();
                case 2 -> listarHerramientas();
                case 3 -> buscarPorIdMenu();
                case 4 -> editarHerramienta();
                case 5 -> eliminarHerramienta();
                case 6 -> usarHerramientaMenu();
                case 7 -> reponerHerramientaMenu();
                case 0 -> System.out.println("Regresando...");
                default -> {
                    if (opcion != -1) System.out.println("Opción inválida.");
                }
            }
        } while (opcion != 0);
    }

    private static void registrarHerramienta() {
        try {
            System.out.println("\n===== REGISTRAR HERRAMIENTA =====");

            System.out.print("Tipo de herramienta: ");
            String tipo = leer.nextLine();

            System.out.print("Nombre: ");
            String nombre = leer.nextLine();

            System.out.print("Cantidad: ");
            int cantidad = leer.nextInt();
            leer.nextLine();

            System.out.print("Estado (true=Funcional / false=No funcional): ");
            boolean estado = leer.nextBoolean();
            leer.nextLine();

            Date fecha = new Date();

            Herramienta nueva = new Herramienta(tipo, nombre, estado, cantidad, fecha);

            if (dao.insertarHerramienta(nueva)) {
                System.out.println("✔ Herramienta registrada con éxito. ID: " + nueva.getIdHerramienta());
            } else {
                System.out.println("Fallo en el registro.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Error: Ingrese TRUE/FALSE o un número en el campo incorrecto.");
            leer.nextLine();
        }
    }

    private static void listarHerramientas() {
        System.out.println("\n=== LISTA DE HERRAMIENTAS ===");
        Set<Herramienta> lista = dao.listarHerramientas();

        if (lista.isEmpty()) {
            System.out.println("No hay herramientas registradas en la DB.");
            return;
        }

        for (Herramienta h : lista) {
            System.out.println(h);
        }
    }

    private static void buscarPorIdMenu() {
        try {
            System.out.print("Ingrese ID a buscar: ");
            int id = leer.nextInt();
            leer.nextLine();

            Herramienta h = dao.buscarPorId(id);

            if (h == null) {
                System.out.println("Herramienta no encontrada.");
            } else {
                System.out.println(h);
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número.");
            leer.nextLine();
        }
    }

    private static void editarHerramienta() {
        try {
            System.out.print("Ingrese ID a editar: ");
            int id = leer.nextInt();
            leer.nextLine();

            Herramienta h = dao.buscarPorId(id);

            if (h == null) {
                System.out.println("Herramienta no encontrada.");
                return;
            }

            System.out.println("\n--- Edición de: " + h.getNombre() + " ---");

            System.out.print("Nuevo tipo (" + h.getTipo() + "): ");
            String nuevoTipo = leer.nextLine();
            if (!nuevoTipo.isEmpty()) h.setTipo(nuevoTipo);

            System.out.print("Nuevo nombre (" + h.getNombre() + "): ");
            String nuevoNombre = leer.nextLine();
            if (!nuevoNombre.isEmpty()) h.setNombre(nuevoNombre);

            System.out.print("Nueva cantidad (" + h.getCantidad() + "): ");
            String nuevaCantidadStr = leer.nextLine();
            if (!nuevaCantidadStr.isEmpty()) h.setCantidad(Integer.parseInt(nuevaCantidadStr));

            System.out.print("Nuevo estado (true/false) (" + h.isEstado() + "): ");
            String nuevoEstadoStr = leer.nextLine();
            if (!nuevoEstadoStr.isEmpty()) h.setEstado(Boolean.parseBoolean(nuevoEstadoStr));

            if (dao.editarHerramienta(h)) {
                System.out.println("Herramienta editada correctamente.");
            } else {
                System.out.println("Error al editar en la DB.");
            }

        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Error: Ingrese un valor numérico o booleano correcto.");
            leer.nextLine();
        }
    }

    private static void eliminarHerramienta() {
        try {
            System.out.print("ID a eliminar: ");
            int id = leer.nextInt();
            leer.nextLine();

            Herramienta h = dao.buscarPorId(id);

            if (h == null) {
                System.out.println("No existe esa herramienta.");
                return;
            }

            System.out.println("¿Desea eliminar '" + h.getNombre() + "'? (Si/No)");
            String r = leer.nextLine();

            if (r.equalsIgnoreCase("si")) {
                if (dao.eliminarHerramienta(id)) {
                    System.out.println("Herramienta eliminada.");
                } else {
                    System.out.println("Error al eliminar. Podría estar asociada a una Cita (FK).");
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número.");
            leer.nextLine();
        }
    }

    private static void usarHerramientaMenu() {
        try {
            System.out.print("ID de la herramienta a usar: ");
            int id = leer.nextInt();
            leer.nextLine();

            if (dao.usarHerramienta(id)) {
                System.out.println("Uso registrado. Se redujo el stock en 1.");
            } else {
                System.out.println("No se pudo usar. No hay stock, está dañada o el ID no existe.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número.");
            leer.nextLine();
        }
    }

    private static void reponerHerramientaMenu() {
        try {
            System.out.print("ID de la herramienta: ");
            int id = leer.nextInt();

            System.out.print("Cantidad a reponer: ");
            int cant = leer.nextInt();
            leer.nextLine();

            if (dao.reponerHerramienta(id, cant)) {
                System.out.println("Stock actualizado. Se repusieron " + cant + " unidades.");
            } else {
                System.out.println("Error al reponer el stock. ID no encontrado.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número.");
            leer.nextLine();
        }
    }
}