import java.util.Date;
import java.util.Objects;
import java.text.SimpleDateFormat;

public class Cita {

    private int nCita;
    private Date fecha;
    private Paciente paciente;
    private Herramienta herramientas;
    private Tratamiento tratamiento;
    private Sala sala;
    private Odontologo odontologo;

    public Cita(Date fecha, Paciente paciente, Herramienta herramientas, Tratamiento tratamiento, Sala sala, Odontologo odontologo) {
        this.nCita = nCita;
        this.fecha = fecha;
        this.paciente = paciente;
        this.herramientas = herramientas;
        this.tratamiento = tratamiento;
        this.sala = sala;
        this.odontologo = odontologo;
    }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public int getnCita() { return nCita; }
    public void setnCita(int nCita) { this.nCita = nCita; }
    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }
    public Odontologo getOdontologo() { return odontologo; }
    public void setOdontologo(Odontologo odontologo) { this.odontologo = odontologo; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Tratamiento getTratamiento() { return tratamiento; }
    public void setTratamiento(Tratamiento tratamiento) { this.tratamiento = tratamiento; }
    public Herramienta getHerramientas() { return herramientas; }
    public void setHerramientas(Herramienta herramientas) { this.herramientas = herramientas; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cita)) return false;
        Cita cita = (Cita) o;
        return nCita == cita.nCita;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nCita);
    }

    @Override
    public String toString() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        return "\n------------------------------" +
                "\nID Cita: " + nCita +
                "\nFecha: " + formato.format(fecha) +
                "\nPaciente: " + paciente.getNombres() + " " + paciente.getApellidos() +
                "\nDNI Paciente: " + paciente.getDNIPaciente() +
                "\nTratamiento: " + tratamiento.getIdTratamiento() +
                "\nHerramienta: " + herramientas.getNombre() +
                "\nSala: " + sala.getIdSala() +
                "\nOdontólogo: " + odontologo.getNombre() +
                "\n------------------------------";
    }
}