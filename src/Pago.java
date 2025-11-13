import java.util.Date;

public class Pago {

    private int idPago;
    private Date fecha;
    private int paciente;
    private boolean estadoPagado;
    private String metodo;
    private String comprobante;

    public Pago(int idPago, Date fecha, int paciente, boolean estadoPagado, String metodo, String comprobante) {
        this.idPago = idPago;
        this.fecha = fecha;
        this.paciente = paciente;
        this. estadoPagado = estadoPagado;
        this.metodo = metodo;
        this.comprobante = comprobante;
    }

    public int getIdPago() {
        return idPago;
    }
    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public int getPaciente() {
        return paciente;
    }
    public void setPaciente(int paciente) {
        this.paciente = paciente;
    }
    public boolean isEstadoPagado() {
        return estadoPagado;
    }
    public void setEstadoPagado(boolean estadoPagado) {
        this.estadoPagado = estadoPagado;
    }
    public String getMetodo() {
        return metodo;
    }
    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
    public String getComprobante() {
        return comprobante;
    }
    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }




}
