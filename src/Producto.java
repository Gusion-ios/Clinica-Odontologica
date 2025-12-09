import java.util.Date;
import java.text.SimpleDateFormat;
public class Producto {

    private int idProducto;
    private String tipo;
    private String nombre;
    private int cantidad;
    private double precio;
    private Date fechaProduccion;
    private Date fechaVencimiento;

    public Producto(int idProducto, String tipo, String nombre, int cantidad, double precio, Date fechaProduccion, Date fechaVencimiento) {
        this.idProducto = idProducto;
        this.tipo = tipo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.fechaProduccion = fechaProduccion;
        this.fechaVencimiento = fechaVencimiento;
    }

    public Producto(String tipo, String nombre, int cantidad, double precio, Date fechaProduccion, Date fechaVencimiento) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.fechaProduccion = fechaProduccion;
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public Date getFechaProduccion() { return fechaProduccion; }
    public void setFechaProduccion(Date fechaProduccion) { this.fechaProduccion = fechaProduccion; }
    public Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    @Override
    public String toString() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        return "\n--- PRODUCTO ---" +
                "\nID: " + idProducto +
                "\nTipo: " + tipo +
                "\nNombre: " + nombre +
                "\nCantidad: " + cantidad +
                "\nPrecio: S/. " + precio +
                "\nFecha Producción: " + f.format(fechaProduccion) +
                "\nFecha Vencimiento: " + f.format(fechaVencimiento) + "\n";
    }

    public boolean estaVencido() {
        return fechaVencimiento.before(new Date());
    }

    public boolean stockBajo() {
        return cantidad < 5;
    }

    public long diasParaVencer() {
        long dif = fechaVencimiento.getTime() - new Date().getTime();
        return dif / (1000L * 60 * 60 * 24);
    }

}