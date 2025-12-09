    import java.util.Date;
    import java.util.Scanner;
    import java.util.Set;
    import java.util.InputMismatchException;

    public class PagoGestion {

        private static final PagoDAO dao = new PagoDAO();
        private static final PacienteDAO pacienteDAO = new PacienteDAO();

        private static final Scanner leer = new Scanner(System.in);

        public static void mostrarMenu() {
            int opcion;

            do {
                System.out.println("\n===== MENÚ DE PAGOS (JDBC) =====");
                System.out.println("1. Registrar pago");
                System.out.println("2. Listar todos los pagos");
                System.out.println("3. Buscar pago(s) por DNI del paciente");
                System.out.println("4. Actualizar estado de un pago");
                System.out.println("5. Eliminar pago");
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
                    case 1 -> registrarPago();
                    case 2 -> listarPagos();
                    case 3 -> buscarPagoPorDniMenu();
                    case 4 -> actualizarEstadoMenu();
                    case 5 -> eliminarPagoMenu();
                    case 0 -> System.out.println("Regresando...");
                    default -> {
                        if (opcion != -1) System.out.println("Opción inválida. Intente de nuevo.");
                    }
                }
            } while (opcion != 0);
        }

        private static void registrarPago() {
            System.out.println("=== Registrando Pago ===");
            try {
                System.out.print("Ingrese el DNI del paciente: ");
                int dni = leer.nextInt();
                leer.nextLine();

                Paciente p = pacienteDAO.buscarPorDNI(dni);
                if (p == null) {
                    System.out.println("Paciente no encontrado. No se puede registrar el pago.");
                    return;
                }

                Date fecha = new Date();

                System.out.println("Estado del pago:");
                System.out.println("1) Pagado");
                System.out.println("2) Pendiente");
                System.out.print("Opción: ");
                int opEstado = leer.nextInt();
                leer.nextLine();
                boolean pagado = (opEstado == 1);

                System.out.println("Método de pago:");
                System.out.println("1) Tarjeta");
                System.out.println("2) Efectivo");
                System.out.print("Opción: ");
                int metodoOp = leer.nextInt();
                leer.nextLine();

                String metodo = (metodoOp == 1) ? "Tarjeta" : "Efectivo";

                Pago nuevo = new Pago(dni, fecha, pagado, metodo);

                if (dao.insertarPago(nuevo)) {
                    System.out.println("Pago registrado con éxito. ID pago: " + nuevo.getIdPago());
                    System.out.println("Paciente: " + p.getNombres() + " " + p.getApellidos());
                } else {
                    System.out.println("Error al registrar el pago en la DB.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Error: Se esperaba un número. Operación cancelada.");
                leer.nextLine();
            }
        }

        private static void listarPagos() {
            System.out.println("\n=== LISTA DE PAGOS ===");
            Set<Pago> lista = dao.listarPagos();

            if (lista.isEmpty()) {
                System.out.println("No hay pagos registrados.");
                return;
            }

            for (Pago p : lista) {
                System.out.println(p);
            }
        }

        private static void buscarPagoPorDniMenu() {
            System.out.print("Ingrese DNI del paciente: ");
            int dni = leer.nextInt();
            leer.nextLine();

            Set<Pago> pagos = dao.buscarPagoPorDNI(dni);

            if (pagos.isEmpty()) {
                System.out.println("No existe pago(s) asociado a ese DNI.");
            } else {
                System.out.println("\n=== PAGOS ASOCIADOS AL DNI " + dni + " ===");
                for (Pago p : pagos) {
                    System.out.println(p);
                }
            }
        }

        private static void actualizarEstadoMenu() {
            System.out.print("Ingrese ID del pago a actualizar: ");
            int id = leer.nextInt();
            leer.nextLine();

            Pago p = dao.buscarPagoPorId(id);

            if (p == null) {
                System.out.println("No se encontró un pago con ese ID.");
                return;
            }

            System.out.println("Estado actual: " + (p.isEstadoPagado() ? "Pagado" : "Pendiente"));
            System.out.println("1) Marcar como pagado");
            System.out.println("2) Marcar como pendiente");
            System.out.print("Opción: ");
            int op = leer.nextInt();
            leer.nextLine();

            boolean nuevoEstado = (op == 1);

            if (dao.actualizarEstadoPago(id, nuevoEstado)) {
                System.out.println("Estado actualizado correctamente.");
            } else {
                System.out.println("Error al actualizar el estado en la DB.");
            }
        }

        private static void eliminarPagoMenu() {
            System.out.print("Ingrese ID del pago a eliminar: ");
            int id = leer.nextInt();
            leer.nextLine();

            if (dao.eliminarPago(id)) {
                System.out.println("Pago eliminado correctamente.");
            } else {
                System.out.println("Error al eliminar el pago.");
            }
        }
    }