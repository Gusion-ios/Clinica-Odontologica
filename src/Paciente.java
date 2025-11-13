public class Paciente {

    private int DNIPaciente;
    private String nombres;
    private String apellidos;
    private int edad;
    private String genero;
    private int telefono;
    private int idAlergia;

    public Paciente(int DNIPaciente, String nombres,String apellidos, int edad, String genero, int telefono, int idAlergia) {
        this.DNIPaciente = DNIPaciente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.genero = genero;
        this.telefono = telefono;
        this. idAlergia = idAlergia;
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




}