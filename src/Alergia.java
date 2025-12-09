import java.util.Objects;

public class Alergia {

    private int idAlergia;
    private String nombreAlergia;
    private String causas;
    private String consecuencias;
    private String recomendacion;

    public Alergia(int idAlergia, String nombreAlergia, String causas, String consecuencias, String recomendacion) {
        this.idAlergia = idAlergia;
        this.nombreAlergia = nombreAlergia;
        this.causas = causas;
        this.consecuencias = consecuencias;
        this.recomendacion = recomendacion;
    }

    public Alergia(String nombreAlergia, String causas, String consecuencias, String recomendacion) {
        this.nombreAlergia = nombreAlergia;
        this.causas = causas;
        this.consecuencias = consecuencias;
        this.recomendacion = recomendacion;
    }

    public int getIdAlergia() { return idAlergia; }
    public void setIdAlergia(int idAlergia) { this.idAlergia = idAlergia; }
    public String getNombreAlergia() { return nombreAlergia; }
    public void setNombreAlergia(String nombreAlergia) { this.nombreAlergia = nombreAlergia; }
    public String getCausas() { return causas; }
    public void setCausas(String causas) { this.causas = causas; }
    public String getConsecuencias() { return consecuencias; }
    public void setConsecuencias(String consecuencias) { this.consecuencias = consecuencias; }
    public String getRecomendacion() { return recomendacion; }
    public void setRecomendacion(String recomendacion) { this.recomendacion = recomendacion; }

    @Override
    public String toString() {
        return "\n--- Alergia ---" +
                "\nID: " + idAlergia +
                "\nNombre: " + nombreAlergia +
                "\nCausas: " + causas +
                "\nConsecuencias: " + consecuencias +
                "\nRecomendación: " + recomendacion + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alergia alergia = (Alergia) o;
        return idAlergia == alergia.idAlergia;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAlergia);
    }
}