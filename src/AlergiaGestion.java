import java.util.Scanner;
import java.util.Set;
import java.util.InputMismatchException;

public class AlergiaGestion {

    private static final AlergiaDAO dao = new AlergiaDAO();
    private static final Scanner leer = new Scanner(System.in);

    public static void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n========= MENÚ DE ALERGIAS (JDBC) =========");
            System.out.println("1. Registrar alergia");
            System.out.println("2. Listar alergias");
            System.out.println("3. Buscar alergia por ID");
            System.out.println("4. Editar alergia");
            System.out.println("5. Eliminar alergia");
            System.out.println("0. Volver al menú principal");
            System.out.println("====================================");
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
                case 1 -> registrarAlergia();
                case 2 -> listarAlergias();
                case 3 -> buscarPorIdMenu();
                case 4 -> editarAlergia();
                case 5 -> eliminarAlergia();
                case 0 -> System.out.println("Regresando...");
                default -> {
                    if (opcion != -1) System.out.println("Opción no válida.");
                }
            }
        } while (opcion != 0);
    }

    private static void registrarAlergia() {
        System.out.println("\n===== REGISTRAR ALERGIA =====");

        System.out.print("Ingrese nombre de la alergia: ");
        String nombre = leer.nextLine();

        System.out.print("Ingrese las causas: ");
        String causas = leer.nextLine();

        System.out.print("Ingrese las consecuencias: ");
        String consecuencias = leer.nextLine();

        System.out.print("Ingrese recomendaciones: ");
        String rec = leer.nextLine();

        Alergia nueva = new Alergia(nombre, causas, consecuencias, rec);

        if (dao.insertarAlergia(nueva)) {
            System.out.println("Alergia registrada con éxito. ID asignado: " + nueva.getIdAlergia());
        } else {
            System.out.println("Error al registrar la alergia en la DB.");
        }
    }

    private static void listarAlergias() {
        System.out.println("\n===== LISTA DE ALERGIAS =====");
        Set<Alergia> lista = dao.listarAlergias();

        if (lista.isEmpty()) {
            System.out.println("No hay alergias registradas en la DB.");
            return;
        }

        for (Alergia a : lista) {
            System.out.println(a);
        }
    }

    private static void buscarPorIdMenu() {
        try {
            System.out.print("Ingrese el ID a buscar: ");
            int id = leer.nextInt();
            leer.nextLine();

            Alergia encontrada = dao.buscarPorId(id);

            if (encontrada == null) {
                System.out.println("No se encontró ninguna alergia con ese ID.");
            } else {
                System.out.println(encontrada);
            }
        } catch (InputMismatchException e) {
            System.out.println("⚠ Error: Se esperaba un número.");
            leer.nextLine();
        }
    }

    private static void eliminarAlergia() {
        try {
            System.out.print("Ingrese ID de la alergia a eliminar: ");
            int id = leer.nextInt();
            leer.nextLine();

            Alergia encontrada = dao.buscarPorId(id);

            if (encontrada == null) {
                System.out.println("No existe una alergia con ese ID.");
                return;
            }

            System.out.println("¿Está seguro que desea eliminar '" + encontrada.getNombreAlergia() + "'? (Si/No)");
            String r = leer.nextLine();

            if (r.equalsIgnoreCase("si")) {
                if (dao.eliminarAlergia(id)) {
                    System.out.println("Alergia eliminada correctamente.");
                } else {
                    System.out.println("Error al eliminar. Podría haber Pacientes asociados (FK).");
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número.");
            leer.nextLine();
        }
    }

    private static void editarAlergia() {
        try {
            System.out.print("Ingrese ID de la alergia a editar: ");
            int id = leer.nextInt();
            leer.nextLine();

            Alergia encontrada = dao.buscarPorId(id);

            if (encontrada == null) {
                System.out.println("No existe una alergia con ese ID.");
                return;
            }

            System.out.println("\n--- Alergia actual: " + encontrada.getNombreAlergia() + " ---");

            System.out.print("Nuevo nombre (" + encontrada.getNombreAlergia() + "): ");
            String nuevoNombre = leer.nextLine();
            if (!nuevoNombre.isEmpty()) encontrada.setNombreAlergia(nuevoNombre);

            System.out.print("Nuevas causas (" + encontrada.getCausas() + "): ");
            String nuevasCausas = leer.nextLine();
            if (!nuevasCausas.isEmpty()) encontrada.setCausas(nuevasCausas);

            System.out.print("Nuevas consecuencias (" + encontrada.getConsecuencias() + "): ");
            String nuevasConsecuencias = leer.nextLine();
            if (!nuevasConsecuencias.isEmpty()) encontrada.setConsecuencias(nuevasConsecuencias);

            System.out.print("Nueva recomendación (" + encontrada.getRecomendacion() + "): ");
            String nuevaRec = leer.nextLine();
            if (!nuevaRec.isEmpty()) encontrada.setRecomendacion(nuevaRec);

            if (dao.editarAlergia(encontrada)) {
                System.out.println("Alergia actualizada correctamente.");
            } else {
                System.out.println("Error al actualizar en la DB.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número.");
            leer.nextLine();
        }
    }
}