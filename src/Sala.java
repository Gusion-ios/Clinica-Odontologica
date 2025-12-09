import java.util.Objects;

public class Sala {

    private int idSala;
    private String tipo;
    private int capacidad;
    private boolean estadoLibre;
    private String ubicacion;
    private int sede;

    public Sala(int idSala, String tipo, int capacidad, boolean estadoLibre, String ubicacion, int sede) {
        this.idSala = idSala;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.estadoLibre = estadoLibre;
        this.ubicacion = ubicacion;
        this.sede = sede;
    }

    public Sala(String tipo, int capacidad, boolean estadoLibre, String ubicacion, int sede) {
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.estadoLibre = estadoLibre;
        this.ubicacion = ubicacion;
        this.sede = sede;
    }

    public int getIdSala() { return idSala; }
    public void setIdSala(int idSala) { this.idSala = idSala; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public boolean isEstadoLibre() { return estadoLibre; }
    public void setEstadoLibre(boolean estadoLibre) { this.estadoLibre = estadoLibre; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public int getSede() { return sede; }
    public void setSede(int sede) { this.sede = sede; }

    public void cambiarEstado(boolean nuevoEstado) {
        this.estadoLibre = nuevoEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sala sala = (Sala) o;
        return idSala == sala.idSala;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSala);
    }

    @Override
    public String toString() {
        return "\n--- SALA ---" +
                "\nID: " + idSala +
                "\nTipo: " + tipo +
                "\nCapacidad: " + capacidad +
                "\nEstado: " + (estadoLibre ? "Libre" : "Ocupada") +
                "\nUbicación: " + ubicacion +
                "\nSede ID: " + sede + "\n";
    }
}