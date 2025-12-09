import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.stream.Collectors;

public class ProductoGestion {

    private static final ProductoDAO dao = new ProductoDAO();
    private static final Scanner leer = new Scanner(System.in);
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    public static void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n===== MENÚ DE PRODUCTOS (JDBC) =====");
            System.out.println("1. Registrar producto");
            System.out.println("2. Listar todos los productos");
            System.out.println("3. Buscar producto por ID");
            System.out.println("4. Editar producto");
            System.out.println("5. Eliminar producto");
            System.out.println("6. Operaciones de Inventario (Stock)");
            System.out.println("7. Reportes (Vencimiento/Stock Bajo)");
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
                case 1 -> registrarProducto();
                case 2 -> listarProductos();
                case 3 -> buscarProductoPorIdMenu();
                case 4 -> editarProducto();
                case 5 -> eliminarProducto();
                case 6 -> menuInventario();
                case 7 -> menuReportes();
                case 0 -> System.out.println("Regresando...");
                default -> {
                    if (opcion != -1) System.out.println("Opción inválida.");
                }
            }
        } while (opcion != 0);
    }

    private static void registrarProducto() {
        try {
            System.out.println("\n===== REGISTRAR PRODUCTO =====");
            System.out.print("Ingrese tipo de producto: ");
            String tipo = leer.nextLine();

            System.out.print("Ingrese nombre del producto: ");
            String nombre = leer.nextLine();

            if (dao.existeProducto(nombre)) {
                System.out.println("Ya existe un producto con ese nombre.");
                return;
            }

            System.out.print("Ingrese cantidad: ");
            int cantidad = leer.nextInt();
            System.out.print("Ingrese precio: ");
            double precio = leer.nextDouble();
            leer.nextLine();

            System.out.print("Ingrese fecha de producción (dd/MM/yyyy): ");
            Date fp = formatoFecha.parse(leer.nextLine());

            System.out.print("Ingrese fecha de vencimiento (dd/MM/yyyy): ");
            Date fv = formatoFecha.parse(leer.nextLine());

            Producto nuevo = new Producto(tipo, nombre, cantidad, precio, fp, fv);

            if (dao.insertarProducto(nuevo)) {
                System.out.println("Producto registrado correctamente. ID: " + nuevo.getIdProducto());
            } else {
                System.out.println("Error al registrar en la DB.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número o decimal.");
            leer.nextLine();
        } catch (ParseException e) {
            System.out.println("Error: Formato de fecha incorrecto (debe ser dd/MM/yyyy).");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listarProductos() {
        System.out.println("\n--- LISTA DE PRODUCTOS ---");
        List<Producto> lista = dao.listarProductos();

        if (lista.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    private static void buscarProductoPorIdMenu() {
        System.out.print("Ingrese ID a buscar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Producto p = dao.buscarPorId(id);

        if (p == null) {
            System.out.println("No se encontró el producto.");
        } else {
            System.out.println(p);
        }
    }

    private static void editarProducto() {
        System.out.println("Implementar edición aquí...");
    }

    private static void eliminarProducto() {
        System.out.print("Ingrese ID a eliminar: ");
        int id = leer.nextInt();
        leer.nextLine();

        if (dao.eliminarProducto(id)) {
            System.out.println("Producto eliminado.");
        } else {
            System.out.println("Error al eliminar. Podría tener tratamientos asociados (FK).");
        }
    }


    private static void menuInventario() {
        System.out.println("Implementar menú de Inventario aquí...");
    }

    private static void menuReportes() {
        System.out.println("Implementar menú de Reportes aquí...");

        List<Producto> listaCompleta = dao.listarProductos();

        List<Producto> stockBajo = listaCompleta.stream()
                .filter(Producto::stockBajo)
                .collect(Collectors.toList());

        System.out.println("\n--- REPORTE: STOCK BAJO ---");
        if (stockBajo.isEmpty()) {
            System.out.println("Todos los productos tienen stock suficiente.");
        } else {
            stockBajo.forEach(p -> System.out.println(p));
        }
    }
}