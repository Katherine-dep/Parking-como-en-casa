package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionClienteJDBC {

    public static void main(String[] args) {
        // Variables de conexion adaptadas al entorno local XAMPP
        String url = "jdbc:mysql://localhost:3306/parking_como_en_casa";
        String usuario = "root";
        String password = ""; 
        String driver = "com.mysql.cj.jdbc.Driver"; 
        
        Connection conexion = null;
        Statement sentencia = null;
        ResultSet resultado = null;

        try {
            // 1. Registrar y cargar el driver
            Class.forName(driver);
            
            // 2. Establecer el puente con la base de datos
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("=== CONEXION EXITOSA A XAMPP ===");
            
            sentencia = conexion.createStatement();

            // 1. CREATE: Insertar un nuevo cliente (Usamos IGNORE por el indice UNIQUE)
            String sqlInsertCliente = 
                "INSERT IGNORE INTO cliente (id, nombre_completo, documento, telefono, correo) " +
                "VALUES (NULL, 'Katherine Ramirez', '98765432', '3211234567', 'Kathe@correo.com')";
            sentencia.executeUpdate(sqlInsertCliente);
            
            System.out.println(
                "[CRUD CLIENTE] CREATE: Cliente 'Katherine Ramirez' procesado con exito."
            );

            // 2. UPDATE: Modificar el telefono del cliente usando su documento como filtro
            String sqlUpdateCliente = 
                "UPDATE cliente " +
                "SET telefono = '3229876543' " +
                "WHERE documento = '98765432'";
            sentencia.executeUpdate(sqlUpdateCliente);
            
            System.out.println(
                "[CRUD CLIENTE] UPDATE: Telefono del cliente actualizado correctamente."
            );

            // 3. READ: Consultar y listar los clientes de la tabla
            String sqlSelectCliente = "SELECT * FROM cliente";
            resultado = sentencia.executeQuery(sqlSelectCliente);
            
            System.out.println(
                "\n--- LISTADO DE CLIENTES EN PARKING COMO EN CASA ---"
            );
            while (resultado.next()) {
                System.out.println(
                    "ID: " + resultado.getInt("id") + 
                    " | Nombre: " + resultado.getString("nombre_completo") + 
                    " | Documento: " + resultado.getString("documento") +
                    " | Telefono: " + resultado.getString("telefono") +
                    " | Correo: " + resultado.getString("correo")
                );
            }
            System.out.println(
                "---------------------------------------------------\n"
            );

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EjemploConexionJDBC.class.getName())
                .log(Level.SEVERE, "Driver no encontrado", ex);
        } catch (SQLException ex) {
            Logger.getLogger(EjemploConexionJDBC.class.getName())
                .log(Level.SEVERE, "Error de SQL ejecutado", ex);
        } finally {
            // Cierre seguro de conexiones
            try {
                if (resultado != null) resultado.close();
                if (sentencia != null) sentencia.close();
                if (conexion != null) conexion.close();
                System.out.println("=== CONEXIONES CERRADAS SEGURAS ===");
            } catch (SQLException ex) {
                Logger.getLogger(EjemploConexionJDBC.class.getName())
                    .log(Level.SEVERE, "Error al cerrar componentes", ex);
            }
        }
    }
}
