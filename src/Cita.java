import java.util.Date;

public class Cita {

    private int nCita;
    private Date fecha;
    private int DNIPaciente;
    private int herramientas;
    private int tratamiento;
    private int sala;
    private int medico;

    public Cita(int nCita, Date fecha, int DNIPaciente, int herramientas, int tratamiento, int sala, int medico) {
        this.nCita = nCita;
        this.fecha = fecha;
        this.DNIPaciente = DNIPaciente;
        this.herramientas = herramientas;
        this.tratamiento = tratamiento;
        this.sala = sala;
        this.medico = medico;
    }

    public int getnCita() {
        return nCita;
    }
    public void setnCita(int nCita) {
        this.nCita = nCita;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public int getDNIPaciente() {
        return DNIPaciente;
    }
    public void setDNIPaciente(int DNIPaciente) {
        this.DNIPaciente = DNIPaciente;
    }
    public int getHerramientas() {
        return herramientas;
    }
    public void setHerramientas(int herramientas) {
        this.herramientas = herramientas;
    }
    public int getTratamiento() {
        return tratamiento;
    }
    public void setTratamiento(int tratamiento) {
        this.tratamiento = tratamiento;
    }
    public int getSala() {
        return sala;
    }
    public void setSala(int sala) {
        this.sala = sala;
    }
    public int getMedico() {
        return medico;
    }
    public void setMedico(int medico) {
        this.medico = medico;
    }



}