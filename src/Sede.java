import java.util.Objects;
public class Sede {

    private int idSede;
    private int capacidad;
    private boolean disponible;
    private String ubicacion;
    private int idClinica;

    public Sede(int idSede, int capacidad, boolean disponible, String ubicacion, int idClinica) {
        this.idSede = idSede;
        this.capacidad = capacidad;
        this.disponible = disponible;
        this.ubicacion = ubicacion;
        this.idClinica = idClinica;
    }

    public Sede(int capacidad, boolean disponible, String ubicacion, int idClinica) {
        this.capacidad = capacidad;
        this.disponible = disponible;
        this.ubicacion = ubicacion;
        this.idClinica = idClinica;
    }

    public int getIdSede() { return idSede; }
    public void setIdSede(int idSede) { this.idSede = idSede; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public int getIdClinica() { return idClinica; }
    public void setIdClinica(int idClinica) { this.idClinica = idClinica; }

    public boolean tieneCapacidad(int cantidadPacientes) {
        return cantidadPacientes <= this.capacidad;
    }

    @Override
    public String toString() {
        return "\n-------------------------------" +
                "\nID Sede: " + idSede +
                "\nCapacidad: " + capacidad +
                "\nDisponible: " + (disponible ? "Sí" : "No") +
                "\nUbicación: " + ubicacion +
                "\nID Clínica: " + idClinica +
                "\n-------------------------------";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sede sede = (Sede) o;
        return idSede == sede.idSede;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSede);
    }
}