import java.util.Objects;

public class Tratamiento {

    private int idTratamiento;
    private String motivo;
    private String estado;
    private int duracionSesiones;
    private int dniPaciente;
    private int idProducto;

    public Tratamiento(int idTratamiento, String motivo, String estado, int duracionSesiones, int dniPaciente, int idProducto) {
        this.idTratamiento = idTratamiento;
        this.motivo = motivo;
        this.estado = estado;
        this.duracionSesiones = duracionSesiones;
        this.dniPaciente = dniPaciente;
        this.idProducto = idProducto;
    }

    public Tratamiento(String motivo, String estado, int duracionSesiones, int dniPaciente, int idProducto) {
        this.motivo = motivo;
        this.estado = estado;
        this.duracionSesiones = duracionSesiones;
        this.dniPaciente = dniPaciente;
        this.idProducto = idProducto;
    }

    public int getIdTratamiento() { return idTratamiento; }
    public void setIdTratamiento(int idTratamiento) { this.idTratamiento = idTratamiento; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getDuracionSesiones() { return duracionSesiones; }
    public void setDuracionSesiones(int duracionSesiones) { this.duracionSesiones = duracionSesiones; }
    public int getPaciente() { return dniPaciente; }
    public void setPaciente(int dniPaciente) { this.dniPaciente = dniPaciente; }
    public int getProducto() { return idProducto; }
    public void setProducto(int idProducto) { this.idProducto = idProducto; }

    @Override
    public String toString() {
        return "\n-------------------------------" +
                "\nID Tratamiento: " + idTratamiento +
                "\nMotivo: " + motivo +
                "\nEstado: " + estado +
                "\nDuración de Sesiones: " + duracionSesiones +
                "\nDNI Paciente: " + dniPaciente +
                "\nID Producto: " + idProducto +
                "\n-------------------------------";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tratamiento that = (Tratamiento) o;
        return idTratamiento == that.idTratamiento;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTratamiento);
    }
}