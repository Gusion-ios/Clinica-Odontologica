public class Alergia {

    private int idAlergia;
    private String nombreAlergia;
    private String causas;
    private String consecuencias;
    private String Recomendacion;

    public Alergia(int idAlergia, String nombreAlergia, String causas, String consecuencias, String recomendacion) {
        this.idAlergia = idAlergia;
        this.nombreAlergia = nombreAlergia;
        this.causas = causas;
        this.consecuencias = consecuencias;
        this.Recomendacion = recomendacion;
    }

    public int getIdAlergia() {
        return idAlergia;
    }
    public void setIdAlergia(int idAlergia) {
        this.idAlergia = idAlergia;
    }
    public String getNombreAlergia() {
        return nombreAlergia;
    }
    public void setNombreAlergia(String nombreAlergia) {
        this.nombreAlergia = nombreAlergia;
    }
    public String getCausas() {
        return causas;
    }
    public void setCausas(String causas) {
        this.causas = causas;
    }
    public String getConsecuencias() {
        return consecuencias;
    }
    public void setConsecuencias(String consecuencias) {
        this.consecuencias = consecuencias;
    }
    public String getRecomendacion() {
        return Recomendacion;
    }
    public void setRecomendacion(String recomendacion) {
        Recomendacion = recomendacion;
    }


}