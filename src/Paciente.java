import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class Paciente {

    private static Set<Paciente> listaPacientes = new HashSet<>();

    Scanner leer = new Scanner(System.in);

    private int DNIPaciente;
    private String nombres;
    private String apellidos;
    private int edad;
    private String genero;
    private int telefono;
    private int idAlergia;

    public Paciente(int DNIPaciente, String nombres, String apellidos, int edad, String genero, int telefono, int idAlergia) {
        this.DNIPaciente = DNIPaciente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.genero = genero;
        this.telefono = telefono;
        this.idAlergia = idAlergia;
    }

    public int getDNIPaciente() {
        return DNIPaciente;
    }
    public void setDNIPaciente(int DNIPaciente) {
        this.DNIPaciente = DNIPaciente;
    }
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public int getTelefono() {
        return telefono;
    }
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
    public int getIdAlergia() {
        return idAlergia;
    }
    public void setIdAlergia(int idAlergia) {
        this.idAlergia = idAlergia;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Paciente)) return false;

        Paciente p = (Paciente) obj;
        return this.DNIPaciente == p.DNIPaciente;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(DNIPaciente);
    }

    public static void registrarPaciente() {

        Scanner leer = new Scanner(System.in);

        System.out.println("Ingrese DNI:");
        int dni = leer.nextInt();

        System.out.println("Ingrese nombres:");
        String nom = leer.next();

        System.out.println("Ingrese apellidos:");
        String ape = leer.next();

        System.out.println("Ingrese edad:");
        int edad = leer.nextInt();

        System.out.println("Ingrese genero:");
        String genero = leer.next();

        System.out.println("Ingrese telefono:");
        int tel = leer.nextInt();

        System.out.println("Ingrese ID de alergia:");
        int idAlergia = leer.nextInt();

        Paciente nuevo = new Paciente(dni, nom, ape, edad, genero, tel, idAlergia);

        if (listaPacientes.add(nuevo)) {
            System.out.println("Paciente registrado correctamente");
        } else {
            System.out.println("ERROR: El DNI ya está registrado (no se puede duplicar)");
        }

    }

    public static void eliminarPaciente() {
        Scanner leer = new Scanner(System.in);

        System.out.println("Ingrese el DNI del paciente a eliminar:");
        int dniEliminar = leer.nextInt();

        Paciente pacienteAEliminar = null;

        for (Paciente p : listaPacientes) {
            if (p.getDNIPaciente() == dniEliminar) {
                pacienteAEliminar = p;
                break;
            }
        }

        if (pacienteAEliminar != null) {
            listaPacientes.remove(pacienteAEliminar);
            System.out.println("Paciente eliminado correctamente.");
        } else {
            System.out.println("No se encontró un paciente con ese DNI.");
        }
    }

    public static void modificarPaciente() {

        Scanner leer = new Scanner(System.in);

        System.out.println("Ingrese el DNI del paciente a modificar:");
        int dni = leer.nextInt();

        Paciente pacienteModificar = null;

        for (Paciente p : listaPacientes) {
            if (p.getDNIPaciente() == dni) {
                pacienteModificar = p;
                break;
            }
        }

        if (pacienteModificar == null) {
            System.out.println("No se encontró un paciente con ese DNI.");
            return;
        }

        System.out.println("¿Qué dato desea modificar?");
        System.out.println("1. Nombres");
        System.out.println("2. Apellidos");
        System.out.println("3. Edad");
        System.out.println("4. Género");
        System.out.println("5. Teléfono");
        System.out.println("6. ID de Alergia");
        System.out.print("Opción: ");
        int opcion = leer.nextInt();

        switch (opcion) {
            case 1:
                System.out.println("Nuevo nombre:");
                pacienteModificar.setNombres(leer.next());
                break;
            case 2:
                System.out.println("Nuevo apellido:");
                pacienteModificar.setApellidos(leer.next());
                break;
            case 3:
                System.out.println("Nueva edad:");
                pacienteModificar.setEdad(leer.nextInt());
                break;
            case 4:
                System.out.println("Nuevo género:");
                pacienteModificar.setGenero(leer.next());
                break;
            case 5:
                System.out.println("Nuevo teléfono:");
                pacienteModificar.setTelefono(leer.nextInt());
                break;
            case 6:
                System.out.println("Nuevo ID de alergia:");
                pacienteModificar.setIdAlergia(leer.nextInt());
                break;

            default:
                System.out.println("Opción inválida.");
                return;
        }
        System.out.println("Datos del paciente modificados correctamente.");
    }

    public static void buscarPaciente() {

        Scanner leer = new Scanner(System.in);

        System.out.println("Ingrese el DNI del paciente a buscar:");
        int dni = leer.nextInt();

        for (Paciente p : listaPacientes) {
            if (p.getDNIPaciente() == dni) {
                System.out.println("\n--- Paciente encontrado ---");
                System.out.println("DNI: " + p.getDNIPaciente());
                System.out.println("Nombre: " + p.getNombres());
                System.out.println("Apellido: " + p.getApellidos());
                System.out.println("Edad: " + p.getEdad());
                System.out.println("Género: " + p.getGenero());
                System.out.println("Teléfono: " + p.getTelefono());
                System.out.println("ID Alergia: " + p.getIdAlergia());
                return;
            }
        }

        System.out.println("No se encontró un paciente con ese DNI.");
    }

    public static void listarPacientes() {
        System.out.println("\n--- Lista de Pacientes ---");

        if (listaPacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }

        for (Paciente p : listaPacientes) {
            System.out.println("DNI: " + p.getDNIPaciente());
            System.out.println("Nombre: " + p.getNombres() + " " + p.getApellidos());
            System.out.println("Edad: " + p.getEdad());
            System.out.println("Género: " + p.getGenero());
            System.out.println("Teléfono: " + p.getTelefono());
            System.out.println("ID Alergia: " + p.getIdAlergia());
        }
    }

    public static void contarPacientes() {
        System.out.println("Total de pacientes registrados: " + listaPacientes.size());
    }








}