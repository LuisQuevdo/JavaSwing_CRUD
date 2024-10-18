package Modelo;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class PersonaDAO {
    conexion conectar = new conexion(); // Clase conexión
    Connection con; // Conexión a la base de datos
    PreparedStatement ps; // Para ejecutar sentencias SQL
    ResultSet rs; // Para almacenar resultados de consultas

    // Método para listar los datos de las personas.
    public List<Persona> listar() {
        List<Persona> datos = new ArrayList<>();
        String sql = "SELECT Id_Empleado, NombresEmpleado, Correo, Telefono FROM Persona.Empleados"; // Consulta correcta
        
        try {
            con = conectar.getConnection(); // Obtener conexión
            ps = con.prepareStatement(sql); // Preparar consulta
            rs = ps.executeQuery(); // Ejecutar consulta
            
            // Recorrer los resultados y agregar a la lista
            while (rs.next()) {
                Persona p = new Persona();
                p.setId(rs.getInt("Id_Empleado")); // Usar el nombre correcto de la columna
                p.setNombre(rs.getString("NombresEmpleado")); // Usar el nombre correcto de la columna
                p.setCorreo(rs.getString("Correo")); // Usar el nombre correcto de la columna
                p.setTelefono(rs.getString("Telefono")); // Usar el nombre correcto de la columna
                datos.add(p); // Agregar objeto Persona a la lista
            }
        } catch (SQLException e) {
            System.out.println("Error SQL al listar: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return datos;
    }

    // Método para agregar una nueva persona a la base de datos.
    public int Agregar(Persona p) {
    String sql = "INSERT INTO Persona.Empleados (DUI_Empleado, ISSS_Empleado, NombresEmpleado, ApellidosEmpleado, FechaNacEmpleado, Telefono, Correo, Id_Cargo, Id_Direccion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    try {
        con = conectar.getConnection(); // Obtener conexión
        ps = con.prepareStatement(sql); // Preparar consulta
        
        // Establecer valores predeterminados para los campos que no se manejan en la vista
        ps.setString(1, "00000000-0"); // DUI genérico
        ps.setInt(2, 123456789); // ISSS genérico
        ps.setString(3, p.getNombre()); // Nombre
        ps.setString(4, "Apellido Genérico"); // Apellido genérico
        ps.setDate(5, java.sql.Date.valueOf("1990-01-01")); // Fecha de nacimiento genérica
        ps.setString(6, p.getTelefono()); // Teléfono
        ps.setString(7, p.getCorreo()); // Correo
        ps.setInt(8, 1); // Id_Cargo genérico (asegúrate de tener un cargo con ID=1)
        ps.setInt(9, 1); // Id_Direccion genérico (asegúrate de tener una dirección con ID=1)
        
        ps.executeUpdate(); // Ejecutar la inserción
    } catch (SQLException e) {
        System.out.println("Error SQL al agregar: " + e.getMessage());
        return 0; // Retornar 0 si hay error
    } finally {
        try {
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    return 1; // Retornar 1 si se agrega correctamente
}

    public int Editar(Persona p) {
    String sql = "UPDATE Persona.Empleados SET NombresEmpleado=?, Correo=?, Telefono=? WHERE Id_Empleado=?";
    
    try {
        con = conectar.getConnection(); // Obtener conexión
        ps = con.prepareStatement(sql); // Preparar consulta
        
        // Establecer los valores para la actualización
        ps.setString(1, p.getNombre());   // Nombre
        ps.setString(2, p.getCorreo());   // Correo
        ps.setString(3, p.getTelefono()); // Teléfono
        ps.setInt(4, p.getId());          // ID del empleado a editar (Id_Empleado)
        
        ps.executeUpdate(); // Ejecutar la actualización
    } catch (SQLException e) {
        System.out.println("Error SQL al editar: " + e.getMessage());
        return 0; // Retornar 0 si hay error
    } finally {
        try {
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    return 1; // Retornar 1 si se actualiza correctamente
}

    public void Eliminar(int id) {
    String sql = "DELETE FROM Persona.Empleados WHERE Id_Empleado=?";
    
    try {
        con = conectar.getConnection(); // Obtener conexión
        ps = con.prepareStatement(sql); // Preparar consulta
        
        // Establecer el ID del empleado a eliminar
        ps.setInt(1, id); // Usar el Id_Empleado para eliminar
        
        ps.executeUpdate(); // Ejecutar la eliminación
    } catch (SQLException e) {
        System.out.println("Error SQL al eliminar: " + e.getMessage());
    } finally {
        try {
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
// Método para filtrar personas por nombre.
public List<Persona> filtrarPorNombre(String nombre) {
    List<Persona> datos = new ArrayList<>();
    String sql = "SELECT Id_Empleado, NombresEmpleado, Correo, Telefono FROM Persona.Empleados WHERE NombresEmpleado LIKE ?";
    
    try {
        con = conectar.getConnection(); // Obtener conexión
        ps = con.prepareStatement(sql); // Preparar la consulta
        ps.setString(1, "%" + nombre + "%"); // Agregar el nombre para la búsqueda con LIKE
        
        rs = ps.executeQuery(); // Ejecutar la consulta
        
        // Recorrer los resultados
        while (rs.next()) {
            Persona p = new Persona();
            p.setId(rs.getInt("Id_Empleado")); // Obtener el ID del empleado
            p.setNombre(rs.getString("NombresEmpleado")); // Obtener el nombre del empleado
            p.setCorreo(rs.getString("Correo")); // Obtener el correo del empleado
            p.setTelefono(rs.getString("Telefono")); // Obtener el teléfono del empleado
            datos.add(p); // Agregar el objeto Persona a la lista
        }
    } catch (SQLException e) {
        System.out.println("Error SQL al filtrar por nombre: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    return datos;
}

// Método para filtrar personas por correo.
public List<Persona> filtrarPorCorreo(String correo) {
    List<Persona> datos = new ArrayList<>();
    String sql = "SELECT Id_Empleado, NombresEmpleado, Correo, Telefono FROM Persona.Empleados WHERE Correo LIKE ?";
    
    try {
        con = conectar.getConnection(); // Obtener conexión
        ps = con.prepareStatement(sql); // Preparar la consulta
        ps.setString(1, "%" + correo + "%"); // Agregar el correo para la búsqueda con LIKE
        
        rs = ps.executeQuery(); // Ejecutar la consulta
        
        // Recorrer los resultados
        while (rs.next()) {
            Persona p = new Persona();
            p.setId(rs.getInt("Id_Empleado")); // Obtener el ID del empleado
            p.setNombre(rs.getString("NombresEmpleado")); // Obtener el nombre del empleado
            p.setCorreo(rs.getString("Correo")); // Obtener el correo del empleado
            p.setTelefono(rs.getString("Telefono")); // Obtener el teléfono del empleado
            datos.add(p); // Agregar el objeto Persona a la lista
        }
    } catch (SQLException e) {
        System.out.println("Error SQL al filtrar por correo: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    return datos;
}

// Método para filtrar personas por teléfono.
public List<Persona> filtrarPorTelefono(String telefono) {
    List<Persona> datos = new ArrayList<>();
    String sql = "SELECT Id_Empleado, NombresEmpleado, Correo, Telefono FROM Persona.Empleados WHERE Telefono LIKE ?";
    
    try {
        con = conectar.getConnection(); // Obtener conexión
        ps = con.prepareStatement(sql); // Preparar la consulta
        ps.setString(1, "%" + telefono + "%"); // Agregar el teléfono para la búsqueda con LIKE
        
        rs = ps.executeQuery(); // Ejecutar la consulta
        
        // Recorrer los resultados
        while (rs.next()) {
            Persona p = new Persona();
            p.setId(rs.getInt("Id_Empleado")); // Obtener el ID del empleado
            p.setNombre(rs.getString("NombresEmpleado")); // Obtener el nombre del empleado
            p.setCorreo(rs.getString("Correo")); // Obtener el correo del empleado
            p.setTelefono(rs.getString("Telefono")); // Obtener el teléfono del empleado
            datos.add(p); // Agregar el objeto Persona a la lista
        }
    } catch (SQLException e) {
        System.out.println("Error SQL al filtrar por teléfono: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    return datos;
}

}
