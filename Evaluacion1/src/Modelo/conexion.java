package Modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author usuario
 */
public class conexion {
    private String databaseName = "TiendaByteCode"; // Nombre de la base que se usará.
    private String url = "jdbc:sqlserver://LUISQUEVEDO;databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    // Método para obtener la conexión
    public Connection getConnection() {
        Connection conexion = null; // Inicializar la conexión
        try {
            // Cargar el driver JDBC de SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Establecer la conexión
            conexion = DriverManager.getConnection(url);
            if (conexion != null) {
                System.out.println("Conexión exitosa a la base de datos " + databaseName + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error de Clase no encontrada: " + e.getMessage());
        }
        return conexion; // Retornar la conexión
    }
}
