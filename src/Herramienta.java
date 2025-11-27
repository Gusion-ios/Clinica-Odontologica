import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

public class Herramienta {

    Scanner leer = new Scanner(System.in);

    private static Set<Herramienta> listaHerramienta = new HashSet<>();


    private int idHerramienta;
    private String tipo;
    private  String nombre;
    private boolean estado;
    private int cantidad;
    private Date fechaAdqusicion;

    public Herramienta(int idHerramienta, String tipo, String nombre, boolean estado, int cantidad, Date fechaAdqusicion) {
        this.idHerramienta = idHerramienta;
        this.tipo = tipo;
        this.nombre = nombre;
        this.estado = estado;
        this.cantidad = cantidad;
        this.fechaAdqusicion = fechaAdqusicion;
    }

    public int getIdHerramienta() {
        return idHerramienta;
    }
    public void setIdHerramienta(int idHerramienta) {
        this.idHerramienta = idHerramienta;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public boolean isEstado() {
        return estado;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public Date getFechaAdqusicion() {
        return fechaAdqusicion;
    }
    public void setFechaAdqusicion(Date fechaAdqusicion) {
        this.fechaAdqusicion = fechaAdqusicion;
    }

    private static int generarIdAutomatico() {
        return listaHerramienta.size() + 1;
    }

    private static boolean existeHerramienta(String nombre) {
        for (Herramienta h : listaHerramienta) {
            if (h.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    public static void registrarHerramienta() {
        try {
            Scanner leer = new Scanner(System.in);
            System.out.println("\n===== REGISTRAR HERRAMIENTA =====");

            int id = generarIdAutomatico();

            System.out.print("Tipo de herramienta: ");
            String tipo = leer.nextLine();

            System.out.print("Nombre: ");
            String nombre = leer.nextLine();

            if (existeHerramienta(nombre)) {
                System.out.println("Ya existe una herramienta con ese nombre.");
                return;
            }

            System.out.print("Cantidad: ");
            int cantidad = leer.nextInt();
            leer.nextLine();

            System.out.print("Estado (true=Funcional / false=No funcional): ");
            boolean estado = leer.nextBoolean();
            leer.nextLine();

            Date fecha = new Date(); // fecha actual de adquisición

            Herramienta nueva = new Herramienta(id, tipo, nombre, estado, cantidad, fecha);
            listaHerramienta.add(nueva);

            System.out.println("Herramienta registrada con éxito. ID: " + id);

        } catch (Exception e) {
            System.out.println("Error en el ingreso de datos.");
        }
    }

    public static Herramienta buscarPorId(int id) {
        for (Herramienta h : listaHerramienta) {
            if (h.getIdHerramienta() == id) {
                return h;
            }
        }
        return null;
    }

    public static Herramienta buscarPorNombre(String nombre) {
        for (Herramienta h : listaHerramienta) {
            if (h.getNombre().equalsIgnoreCase(nombre)) {
                return h;
            }
        }
        return null;
    }

    public static void editarHerramienta() {
        Scanner leer = new Scanner(System.in);

        System.out.print("Ingrese ID a editar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Herramienta h = buscarPorId(id);

        if (h == null) {
            System.out.println("Herramienta no encontrada.");
            return;
        }

        System.out.print("Nuevo tipo: ");
        h.setTipo(leer.nextLine());

        System.out.print("Nuevo nombre: ");
        h.setNombre(leer.nextLine());

        System.out.print("Nueva cantidad: ");
        h.setCantidad(leer.nextInt());
        leer.nextLine();

        System.out.print("Nuevo estado (true/false): ");
        h.setEstado(leer.nextBoolean());

        System.out.println("Herramienta editada correctamente.");
    }

    public static void eliminarHerramienta() {
        Scanner leer = new Scanner(System.in);

        System.out.print("ID a eliminar: ");
        int id = leer.nextInt();
        leer.nextLine();

        Herramienta h = buscarPorId(id);

        if (h == null) {
            System.out.println("No existe esa herramienta.");
            return;
        }

        System.out.println("¿Desea eliminar '" + h.getNombre() + "'? (Si/No)");
        String r = leer.nextLine();

        if (r.equalsIgnoreCase("si")) {
            listaHerramienta.remove(h);
            System.out.println("Herramienta eliminada.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    public static void listarHerramientas() {
        if (listaHerramienta.isEmpty()) {
            System.out.println("No hay herramientas registradas.");
            return;
        }

        for (Herramienta h : listaHerramienta) {
            System.out.println(h);
        }
    }

    public boolean disponible() {
        return this.cantidad > 0 && this.estado;
    }

    public void usar() {
        if (cantidad > 0) cantidad--;
    }

    public void reponer(int cant) {
        this.cantidad += cant;
    }

    @Override
    public String toString() {
        return "\n--- HERRAMIENTA ---" +
                "\nID: " + idHerramienta +
                "\nTipo: " + tipo +
                "\nNombre: " + nombre +
                "\nEstado: " + (estado ? "Funcional" : "No funcional") +
                "\nCantidad: " + cantidad +
                "\nFecha adquisición: " + fechaAdqusicion + "\n";
    }

}
