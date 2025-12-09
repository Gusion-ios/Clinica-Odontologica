import javax.swing.*;
import java.util.Scanner;

public class Main {

    private static final String ADMIN_USER = "Valerio";
    private static final String ADMIN_PASS = "1234";

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);

        System.out.println("=====================================");
        System.out.println("     SISTEMA DE GESTIÓN ODONTOLÓGICA");
        System.out.println("=====================================\n");

        boolean accesoConcedido = false;

        while (!accesoConcedido) {
            System.out.print("Ingrese nombre de usuario: ");
            String user = leer.nextLine();

            System.out.print("Ingrese contraseña: ");
            String pass = leer.nextLine();

            if (user.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
                accesoConcedido = true;
                System.out.println("\n Acceso concedido. Bienvenido " + ADMIN_USER + "!\n");

                SwingUtilities.invokeLater(() -> {
                    MenuPrincipalGUI menu = new MenuPrincipalGUI();
                    menu.setVisible(true);
                });
            } else {
                System.out.println("Usuario o contraseña incorrectos. Intente nuevamente.\n");
            }
        }

        int opcion;

        do {
            System.out.println("=========== MENÚ PRINCIPAL ===========");
            System.out.println("1. Gestión de Sedes");
            System.out.println("2. Gestión de Clínicas");
            System.out.println("3. Gestión de Odontólogos");
            System.out.println("4. Gestión de Pacientes");
            System.out.println("5. Gestión de Productos");
            System.out.println("6. Gestión de Tratamientos");
            System.out.println("0. Salir");
            System.out.println("======================================");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = leer.nextInt();
                leer.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("⚠ Opción inválida. Intente de nuevo.");
                leer.nextLine();
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> SedeGestion.mostrarMenu();
                case 2 -> ClinicaGestion.mostrarMenu();
                case 3 -> OdontologoGestion.mostrarMenu();
                case 4 -> PacienteGestion.mostrarMenu();
                case 5 -> ProductoGestion.mostrarMenu();
                case 6 -> TratamientoGestion.mostrarMenu();
                case 7 -> PagoGestion.mostrarMenu();
                case 8 -> AlergiaGestion.mostrarMenu();
                case 9 -> HerramientaGestion.mostrarMenu();
                case 10 -> SalaGestion.mostrarMenu();
                case 11 -> CitaGestion.mostrarMenu();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> {
                    if (opcion != -1) {
                        System.out.println("Opción no válida.");
                    }
                }
            }

        } while (opcion != 0);

        leer.close();
    }

}