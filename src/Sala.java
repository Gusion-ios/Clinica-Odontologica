import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class Sala {

    private static Set<Sala> listaSala = new HashSet<>();

    Scanner leer = new Scanner(System.in);

    private int idSala;
    private String tipo;
    private int capacidad;
    private boolean estadoLibre;
    private String ubicacion;
    private int sede;

    public Sala(int idSala, String tipo, int capacidad, boolean estadoLibre, String ubicacion, int sede) {
        this.idSala = idSala;
        this.tipo = tipo;
        this. capacidad = capacidad;
        this. estadoLibre = estadoLibre;
        this.ubicacion = ubicacion;
        this.sede = sede;
    }

    public int getIdSala() {
        return idSala;
    }
    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    public boolean isEstadoLibre() {
        return estadoLibre;
    }
    public void setEstadoLibre(boolean estadoLibre) {
        this.estadoLibre = estadoLibre;
    }
    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    public int getSede() {
        return sede;
    }
    public void setSede(int sede) {
        this.sede = sede;
    }

    private static int generarIdAutomatico() {
        return listaSala.size() + 1;
    }

    public static void registrarSala() {
        Scanner leer = new Scanner(System.in);

        int id = generarIdAutomatico();

        System.out.println("=== REGISTRAR SALA ===");

        System.out.print("Tipo de sala (Reunión, Estudio, Conferencia): ");
        String tipo = leer.nextLine();

        System.out.print("Capacidad: ");
        int capacidad = leer.nextInt();
        leer.nextLine();

        System.out.print("Ubicación: ");
        String ubicacion = leer.nextLine();

        System.out.print("Número de sede: ");
        int sede = leer.nextInt();
        leer.nextLine();

        Sala nueva = new Sala(id, tipo, capacidad, true, ubicacion, sede);
        listaSala.add(nueva);

        System.out.println("Sala registrada con éxito. ID = " + id);
    }

    public static Sala buscarPorId(int id) {
        for (Sala s : listaSala) {
            if (s.getIdSala() == id) {
                return s;
            }
        }
        return null;
    }

    public static void listarSalas() {
        if (listaSala.isEmpty()) {
            System.out.println("No hay salas registradas.");
            return;
        }

        for (Sala s : listaSala) {
            System.out.println(s);
        }
    }

    public static void editarSala() {
        Scanner leer = new Scanner(System.in);

        System.out.print("Ingrese ID de la sala a editar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Sala s = buscarPorId(id);

        if (s == null) {
            System.out.println("Sala no encontrada.");
            return;
        }

        System.out.print("Nuevo tipo: ");
        s.setTipo(leer.nextLine());

        System.out.print("Nueva capacidad: ");
        s.setCapacidad(leer.nextInt());
        leer.nextLine();

        System.out.print("Nueva ubicación: ");
        s.setUbicacion(leer.nextLine());

        System.out.print("Nueva sede: ");
        s.setSede(leer.nextInt());

        System.out.println("Sala actualizada correctamente.");
    }

    public static void eliminarSala() {
        Scanner leer = new Scanner(System.in);

        System.out.print("Ingrese ID de la sala a eliminar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Sala s = buscarPorId(id);

        if (s == null) {
            System.out.println("Sala no encontrada.");
            return;
        }

        System.out.println("¿Eliminar sala '" + s.getTipo() + "'? (Si/No)");
        String r = leer.nextLine();

        if (r.equalsIgnoreCase("si")) {
            listaSala.remove(s);
            System.out.println("Sala eliminada.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    public void cambiarEstado(boolean nuevoEstado) {
        this.estadoLibre = nuevoEstado;
    }

    @Override
    public String toString() {
        return "\n--- SALA ---" +
                "\nID: " + idSala +
                "\nTipo: " + tipo +
                "\nCapacidad: " + capacidad +
                "\nEstado: " + (estadoLibre ? "Libre" : "Ocupada") +
                "\nUbicación: " + ubicacion +
                "\nSede: " + sede + "\n";
    }

}
