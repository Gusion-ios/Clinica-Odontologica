public class Odontologo {

    private int idOdontologo;
    private String nombre;
    private String apellido;
    private String especialidad;
    private int sedeMain;

    public Odontologo(int idOdontologo, String nombre, String apellido, String especialidad, int sedeMain) {
        this.idOdontologo = idOdontologo;
        this.nombre = nombre;
        this. apellido = apellido;
        this.especialidad = especialidad;
        this.sedeMain = sedeMain;
    }

    public int getIdOdontologo() {
        return idOdontologo;
    }
    public void setIdOdontologo(int idOdontologo) {
        this.idOdontologo = idOdontologo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    public int getSedeMain() {
        return sedeMain;
    }
    public void setSedeMain(int sedeMain) {
        this.sedeMain = sedeMain;
    }


}
