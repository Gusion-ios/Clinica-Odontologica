import java.util.Scanner;
import java.util.Set;
import java.util.InputMismatchException;

public class OdontologoGestion {

    private static final OdontologoDAO dao = new OdontologoDAO();
    private static final Scanner leer = new Scanner(System.in);

    public static void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n===== MENÚ DE ODONTÓLOGOS (JDBC) =====");
            System.out.println("1. Registrar odontólogo");
            System.out.println("2. Modificar odontólogo");
            System.out.println("3. Eliminar odontólogo");
            System.out.println("4. Buscar odontólogo por ID");
            System.out.println("5. Listar odontólogos");
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
                case 1 -> registrarOdontologo();
                case 2 -> modificarOdontologo();
                case 3 -> eliminarOdontologo();
                case 4 -> buscarOdontologoPorIdMenu();
                case 5 -> listarOdontologos();
                case 0 -> System.out.println("Regresando...");
                default -> {
                    if (opcion != -1) System.out.println("Opción inválida.");
                }
            }
        } while (opcion != 0);
    }

    private static void registrarOdontologo() {
        System.out.println("===== REGISTRO DE ODONTÓLOGO =====");
        try {
            System.out.print("Ingrese nombre: ");
            String nombre = leer.nextLine();
            System.out.print("Ingrese apellido: ");
            String apellido = leer.nextLine();

            if (dao.existeOdontologo(nombre, apellido)) {
                System.out.println("Error: Ya existe un odontólogo con ese nombre y apellido.");
                return;
            }

            System.out.print("Ingrese especialidad: ");
            String especialidad = leer.nextLine();
            System.out.print("Ingrese la sede principal (ID de sede): ");
            int sede = leer.nextInt();
            leer.nextLine();

            Odontologo nuevo = new Odontologo(nombre, apellido, especialidad, sede);

            if (dao.insertarOdontologo(nuevo)) {
                System.out.println("Odontólogo registrado. ID asignado: " + nuevo.getIdOdontologo());
            } else {
                System.out.println("Error al registrar. Verifique si el ID de Sede existe.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error en formato numérico.");
            leer.nextLine();
        }
    }

    private static void listarOdontologos() {
        System.out.println("\n=== LISTA DE ODONTÓLOGOS ===");
        Set<Odontologo> lista = dao.listarOdontologos();
        if (lista.isEmpty()) {
            System.out.println("No hay odontólogos registrados en la DB.");
            return;
        }
        for (Odontologo o : lista) {
            System.out.println(o);
        }
    }

    private static void buscarOdontologoPorIdMenu() {
        System.out.print("Ingrese el ID: ");
        int idBuscar = leer.nextInt();
        leer.nextLine();

        Odontologo encontrado = dao.buscarPorId(idBuscar);

        if (encontrado != null) {
            System.out.println("\nOdontólogo encontrado:");
            System.out.println(encontrado);
        } else {
            System.out.println("No se encontró un odontólogo con ese ID.");
        }
    }

    private static void modificarOdontologo() {
        System.out.print("Ingrese el ID del odontólogo a modificar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Odontologo o = dao.buscarPorId(id);

        if (o == null) {
            System.out.println("Odontólogo no registrado.");
            return;
        }

        System.out.println("\n=== ✏ Modificar Datos (Deje vacío para no cambiar) ===");

        System.out.print("Nuevo nombre (" + o.getNombre() + "): ");
        String nuevoNombre = leer.nextLine().trim();
        if (!nuevoNombre.isEmpty()) o.setNombre(nuevoNombre);

        System.out.print("Nuevo apellido (" + o.getApellido() + "): ");
        String nuevoApellido = leer.nextLine().trim();
        if (!nuevoApellido.isEmpty()) o.setApellido(nuevoApellido);

        System.out.print("Nueva especialidad (" + o.getEspecialidad() + "): ");
        String nuevaEspecialidad = leer.nextLine().trim();
        if (!nuevaEspecialidad.isEmpty()) o.setEspecialidad(nuevaEspecialidad);

        System.out.print("Nueva sede (" + o.getSedeMain() + "): ");
        try {
            String nuevaSedeTxt = leer.nextLine().trim();
            if (!nuevaSedeTxt.isEmpty()) {
                o.setSedeMain(Integer.parseInt(nuevaSedeTxt));
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor de sede inválido. Se mantuvo el valor anterior.");
        }

        if (dao.editarOdontologo(o)) {
            System.out.println("\nDatos actualizados correctamente.");
        } else {
            System.out.println("\nError al actualizar en la DB. Verifique el ID de Sede.");
        }
    }

    private static void eliminarOdontologo() {
        System.out.print("Ingrese ID del odontólogo a eliminar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Odontologo encontrado = dao.buscarPorId(id);

        if (encontrado == null) {
            System.out.println("No se encontró un odontólogo con ese ID.");
            return;
        }

        System.out.println("\n¿Está seguro de eliminar a " + encontrado.getNombre() + "? (Si/No)");
        String respuesta = leer.nextLine();

        if (respuesta.equalsIgnoreCase("si")) {
            if (dao.eliminarOdontologo(id)) {
                System.out.println("🗑 Odontólogo eliminado correctamente.");
            } else {
                System.out.println("Error al eliminar. Podría tener citas asociadas (FK).");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }
}