public class Clinica {

    private int idClinica;
    private String nombre;
    private String RUC;
    private String telefono;
    private String direccion;

    public Clinica(int idClinica, String nombre, String RUC, String telefono, String direccion) {
        this.idClinica = idClinica;
        this.nombre = nombre;
        this.RUC = RUC;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public int getIdClinica() {
        return idClinica;
    }
    public void setIdClinica(int idClinica) {
        this.idClinica = idClinica;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getRUC() {
        return RUC;
    }
    public void setRUC(String RUC) {
        this.RUC = RUC;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }




}
