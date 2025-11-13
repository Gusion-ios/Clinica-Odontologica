import java.util.Date;

public class Inventario {

    private int idInventario;
    private int stock;
    private Date fechaRegistro;

    public Inventario(int idInventario, int stock,Date fechaRegistro) {
        this.idInventario = idInventario;
        this.stock = stock;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdInventario() {
        return idInventario;
    }
    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public Date getFechaRegistro() {
        return fechaRegistro;
    }
    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }



}
