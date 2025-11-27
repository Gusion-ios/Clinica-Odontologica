public class Tratamiento {

    private int idTratamiento;
    private String motivo;
    private String estado;
    private int duracionSesiones;
    private int paciente;
    private int producto;

    public Tratamiento(int idTratamiento, String motivo, String estado, int duracionSesiones, int paciente, int producto) {
        this.idTratamiento = idTratamiento;
        this. motivo = motivo;
        this.estado = estado;
        this.duracionSesiones = duracionSesiones;
        this.paciente = paciente;
        this. producto = producto;
    }

    public int getIdTratamiento() {
        return idTratamiento;
    }
    public void setIdTratamiento(int idTratamiento) {
        this.idTratamiento = idTratamiento;
    }
    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public int getDuracionSesiones() {
        return duracionSesiones;
    }
    public void setDuracionSesiones(int duracionSesiones) {
        this.duracionSesiones = duracionSesiones;
    }
    public int getPaciente() {
        return paciente;
    }
    public void setPaciente(int paciente) {
        this.paciente = paciente;
    }
    public int getProducto() {
        return producto;
    }
    public void setProducto(int producto) {
        this.producto = producto;
    }







}
