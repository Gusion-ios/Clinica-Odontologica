public class Sede {

    private int idSede;
    private int capacidad;
    private boolean disponible;
    private String ubicacion;
    private int clinica;

    public Sede(int idSede, int capacidad, boolean disponible, String ubicacion, int clinica) {
        this.idSede = idSede;
        this.capacidad = capacidad;
        this.disponible = disponible;
        this.ubicacion = ubicacion;
        this.clinica = clinica;
    }

    public int getIdSede() {
        return idSede;
    }
    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    public boolean isDisponible() {
        return disponible;
    }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    public int getClinica() {
        return clinica;
    }
    public void setClinica(int clinica) {
        this.clinica = clinica;
    }






}
