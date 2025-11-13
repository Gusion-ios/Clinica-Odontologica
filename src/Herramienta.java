import java.util.Date;

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




}
