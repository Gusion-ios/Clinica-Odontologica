import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:sqlserver://IMAGHINEXT;databaseName=clinica_odontologica;encrypt=false;";
    private static final String USER = "sa";
    private static final String PASS = "Xnxxxnxx123@";

    public static Connection conectar() {
        Connection cn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            cn = DriverManager.getConnection(URL, USER, PASS);

        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró la clase del driver JDBC.");
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
        }
        return cn;
    }

    public static void main(String[] args) {
        System.out.println("Intentando conectar a SQL Server...");
        Connection testConn = conectar();

        if (testConn != null) {
            System.out.println("🎉 ¡Conexión exitosa!");

            try {
                testConn.close();
                System.out.println("Conexión de prueba cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        } else {
            System.out.println("Falló la conexión. Verifique la URL, credenciales y permisos.");
        }
    }

}