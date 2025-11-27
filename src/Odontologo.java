import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class Odontologo {

    private static Set<Odontologo> listaOdontologo = new HashSet<>();

    private int idOdontologo;
    private String nombre;
    private String apellido;
    private String especialidad;
    private int sedeMain;

    public Odontologo(int idOdontologo, String nombre, String apellido, String especialidad, int sedeMain) {
        this.idOdontologo = idOdontologo;
        this.nombre = nombre;
        this. apellido = apellido;
        this.especialidad = especialidad;
        this.sedeMain = sedeMain;
    }

    public int getIdOdontologo() {
        return idOdontologo;
    }
    public void setIdOdontologo(int idOdontologo) {
        this.idOdontologo = idOdontologo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    public int getSedeMain() {
        return sedeMain;
    }
    public void setSedeMain(int sedeMain) {
        this.sedeMain = sedeMain;
    }

    public void modificarDatos(){

        Scanner leer = new Scanner(System.in);

        System.out.println("Ingrese el ID del odontologo a modificar: ");
        int idPrueba = leer.nextInt();

        Odontologo encontrado = null;

        for (Odontologo o : listaOdontologo){
            if (o.getIdOdontologo() == idPrueba) {
                System.out.println("Odontólogo encontrado: " + encontrado.getNombre() + " " + encontrado.getApellido());

                System.out.println("Nuevo nombre (enter para mantener): ");
                String nuevoNombre = leer.nextLine();
                if (!nuevoNombre.isBlank()) encontrado.setNombre(nuevoNombre);

                System.out.println("Nuevo apellido (enter para mantener): ");
                String nuevoApellido = leer.nextLine();
                if (!nuevoApellido.isBlank()) encontrado.setApellido(nuevoApellido);

                System.out.println("Nueva especialidad (enter para mantener): ");
                String nuevaEspecialidad = leer.nextLine();
                if (!nuevaEspecialidad.isBlank()) encontrado.setEspecialidad(nuevaEspecialidad);

                System.out.println("Nueva sede principal (0 para mantener): ");
                int nuevaSede = leer.nextInt();
                if (nuevaSede != 0) encontrado.setSedeMain(nuevaSede);

                System.out.println("    Datos actualizados correctamente.");
            }else {
                System.out.println("Odontologo no registrado");
                return;
            }
        }
    }

    private static int generarIdAutomatico() {
        return listaOdontologo.size() + 1;
    }


    public static void RegistrarOdontologo() {

        Scanner leer = new Scanner(System.in);

        System.out.println("===== REGISTRO DE ODONTÓLOGO =====");

        System.out.print("Ingrese ID del odontólogo: ");
        int id = generarIdAutomatico();

        for (Odontologo o : listaOdontologo) {
            if (o.getIdOdontologo() == id) {
                System.out.println("Error: Ya existe un odontólogo con ese ID.");
                return;
            }
        }

        System.out.print("Ingrese nombre: ");
        String nombre = leer.nextLine();

        System.out.print("Ingrese apellido: ");
        String apellido = leer.nextLine();

        System.out.print("Ingrese especialidad: ");
        String especialidad = leer.nextLine();

        System.out.print("Ingrese la sede principal (ID de sede): ");
        int sede = leer.nextInt();

        Odontologo nuevo = new Odontologo(id, nombre, apellido, especialidad, sede);

        listaOdontologo.add(nuevo);

        System.out.println("Odontólogo registrado correctamente.");
    }

    public static void listarOdontologos() {
        if (listaOdontologo.isEmpty()) {
            System.out.println("No hay odontólogos registrados.");
            return;
        }
        for (Odontologo o : listaOdontologo) {
            System.out.println(o);
        }
    }

    public static Odontologo buscarPorId(int id) {
        for (Odontologo o : listaOdontologo) {
            if (o.getIdOdontologo() == id) {
                return o;
            }
        }
        return null;
    }

    public static void eliminarOdontologo() {

        Scanner leer = new Scanner(System.in);

        System.out.print("Ingrese ID del odontólogo a eliminar: ");
        int id = leer.nextInt();

        Odontologo encontrado = buscarPorId(id);

        if (encontrado == null) {
            System.out.println("No se encontró un odontólogo con ese ID.");
            return;
        }

        System.out.println("\nOdontólogo encontrado:");
        System.out.println(encontrado);

        System.out.println("\n¿Está seguro de eliminar a " + encontrado.getNombre() + " " + encontrado.getApellido() + "? (Si/No)");
        String respuesta = leer.nextLine();

        if (respuesta.equalsIgnoreCase("si")) {
            listaOdontologo.remove(encontrado);
            System.out.println("Odontólogo eliminado correctamente.");
        } else if (respuesta.equalsIgnoreCase("no")) {
            System.out.println("Operación cancelada.");
        } else {
            System.out.println("Respuesta inválida.");
        }
    }




}
