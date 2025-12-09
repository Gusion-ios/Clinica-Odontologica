import java.util.Date;
import java.util.Objects;
import java.text.SimpleDateFormat;

public class Herramienta {

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

    public Herramienta(String tipo, String nombre, boolean estado, int cantidad, Date fechaAdqusicion) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.estado = estado;
        this.cantidad = cantidad;
        this.fechaAdqusicion = fechaAdqusicion;
    }

    public int getIdHerramienta() { return idHerramienta; }
    public void setIdHerramienta(int idHerramienta) { this.idHerramienta = idHerramienta; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public Date getFechaAdqusicion() { return fechaAdqusicion; }
    public void setFechaAdqusicion(Date fechaAdqusicion) { this.fechaAdqusicion = fechaAdqusicion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Herramienta that = (Herramienta) o;
        return idHerramienta == that.idHerramienta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHerramienta);
    }

    @Override
    public String toString() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        return "\n--- HERRAMIENTA ---" +
                "\nID: " + idHerramienta +
                "\nTipo: " + tipo +
                "\nNombre: " + nombre +
                "\nEstado: " + (estado ? "Funcional" : "No funcional") +
                "\nCantidad: " + cantidad +
                "\nFecha adquisición: " + f.format(fechaAdqusicion) + "\n";
    }
}