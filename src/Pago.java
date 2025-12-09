import java.util.Date;

public class Pago {

    private int dniPaciente;

    private int idPago;
    private Date fecha;
    private boolean estadoPagado;
    private String metodo;

    public Pago(int idPago, int dniPaciente, Date fecha, boolean estadoPagado, String metodo) {
        this.idPago = idPago;
        this.dniPaciente = dniPaciente;
        this.fecha = fecha;
        this.estadoPagado = estadoPagado;
        this.metodo = metodo;
    }

    public Pago(int dniPaciente, Date fecha, boolean estadoPagado, String metodo) {
        this.dniPaciente = dniPaciente;
        this.fecha = fecha;
        this.estadoPagado = estadoPagado;
        this.metodo = metodo;
    }

    public int getDniPaciente() { return dniPaciente; }
    public void setDniPaciente(int dniPaciente) { this.dniPaciente = dniPaciente; }

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    public boolean isEstadoPagado() { return estadoPagado; }
    public void setEstadoPagado(boolean estadoPagado) { this.estadoPagado = estadoPagado; }

    @Override
    public String toString() {
        return "\n--- PAGO ---" +
                "\nID Pago: " + idPago +
                "\nFecha: " + fecha +
                "\nEstado: " + (estadoPagado ? "Pagado" : "Pendiente") +
                "\nMétodo: " + metodo +
                "\nDNI Paciente: " + dniPaciente + "\n";
    }
}