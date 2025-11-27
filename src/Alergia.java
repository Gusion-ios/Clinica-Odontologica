import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class Alergia {

    private static Scanner leer = new Scanner(System.in);
    private static Set<Alergia> listaAlergia = new HashSet<>();

    private int idAlergia;
    private String nombreAlergia;
    private String causas;
    private String consecuencias;
    private String Recomendacion;

    public Alergia(int idAlergia, String nombreAlergia, String causas, String consecuencias, String recomendacion) {
        this.idAlergia = idAlergia;
        this.nombreAlergia = nombreAlergia;
        this.causas = causas;
        this.consecuencias = consecuencias;
        this.Recomendacion = recomendacion;
    }

    public int getIdAlergia() { return idAlergia; }
    public void setIdAlergia(int idAlergia) { this.idAlergia = idAlergia; }

    public String getNombreAlergia() { return nombreAlergia; }
    public void setNombreAlergia(String nombreAlergia) { this.nombreAlergia = nombreAlergia; }

    public String getCausas() { return causas; }
    public void setCausas(String causas) { this.causas = causas; }

    public String getConsecuencias() { return consecuencias; }
    public void setConsecuencias(String consecuencias) { this.consecuencias = consecuencias; }

    public String getRecomendacion() { return Recomendacion; }
    public void setRecomendacion(String recomendacion) { Recomendacion = recomendacion; }

    private static int generarIdAutomatico() {
        return listaAlergia.size() + 1;
    }

    private static boolean existeAlergia(String nombre) {
        for (Alergia a : listaAlergia) {
            if (a.getNombreAlergia().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    public static Alergia buscarPorId(int id) {
        for (Alergia a : listaAlergia) {
            if (a.getIdAlergia() == id) {
                return a;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "\n--- Alergia ---" +
                "\nID: " + idAlergia +
                "\nNombre: " + nombreAlergia +
                "\nCausas: " + causas +
                "\nConsecuencias: " + consecuencias +
                "\nRecomendación: " + Recomendacion + "\n";
    }

    public static void RegistrarAlergia() {

        System.out.println("\n===== REGISTRAR ALERGIA =====");

        int nuevoId = generarIdAutomatico();

        System.out.print("Ingrese nombre de la alergia: ");
        String nombre = leer.nextLine();

        if (existeAlergia(nombre)) {
            System.out.println("Ya existe una alergia registrada con ese nombre.");
            return;
        }

        System.out.print("Ingrese las causas: ");
        String causas = leer.nextLine();

        System.out.print("Ingrese las consecuencias: ");
        String consecuencias = leer.nextLine();

        System.out.print("Ingrese recomendaciones: ");
        String rec = leer.nextLine();

        Alergia nueva = new Alergia(nuevoId, nombre, causas, consecuencias, rec);
        listaAlergia.add(nueva);

        System.out.println("Alergia registrada con éxito. ID asignado: " + nuevoId);
    }



    public static void buscarPorIdMenu() {
        System.out.print("Ingrese el ID a buscar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Alergia encontrada = buscarPorId(id);

        if (encontrada == null) {
            System.out.println("No se encontró ninguna alergia con ese ID.");
        } else {
            System.out.println(encontrada);
        }
    }



    public static void eliminarAlergia() {

        System.out.print("Ingrese ID de la alergia a eliminar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Alergia encontrada = buscarPorId(id);

        if (encontrada == null) {
            System.out.println("No existe una alergia con ese ID.");
            return;
        }

        System.out.println("¿Está seguro que desea eliminar '" + encontrada.getNombreAlergia() + "'? (Si/No)");
        String r = leer.nextLine();

        if (r.equalsIgnoreCase("si")) {
            listaAlergia.remove(encontrada);
            System.out.println("Alergia eliminada correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }



    public static void editarAlergia() {

        System.out.print("Ingrese ID de la alergia a editar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Alergia encontrada = buscarPorId(id);

        if (encontrada == null) {
            System.out.println("No existe una alergia con ese ID.");
            return;
        }

        System.out.print("Nuevo nombre: ");
        encontrada.setNombreAlergia(leer.nextLine());

        System.out.print("Nuevas causas: ");
        encontrada.setCausas(leer.nextLine());

        System.out.print("Nuevas consecuencias: ");
        encontrada.setConsecuencias(leer.nextLine());

        System.out.print("Nueva recomendación: ");
        encontrada.setRecomendacion(leer.nextLine());

        System.out.println("Alergia actualizada correctamente.");
    }

}
