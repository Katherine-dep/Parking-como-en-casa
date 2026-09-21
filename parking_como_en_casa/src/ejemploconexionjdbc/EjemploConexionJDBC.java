package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EjemploConexionJDBC {

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

            //CREATE: Insertar rol base y usuario unico

            String sqlInsertRol = 
                "INSERT IGNORE INTO rol (id, nombre) " +
                "VALUES (1, 'Administrador')";
            sentencia.executeUpdate(sqlInsertRol);
            
            String sqlInsertUsuario = 
                "INSERT INTO usuario " +
                "(id, id_rol, nombre_completo, documento, fecha_registro) " +
                "VALUES (NULL, 1, 'Katherine Ramirez', '10203040', NOW())";
            sentencia.executeUpdate(sqlInsertUsuario);
            
            System.out.println(
                "[CRUD] CREATE: Usuario 'Katherine Ramirez' " +
                "registrado con exito."
            );

            //UPDATE: Modificar el nombre completo usando el doc
 
            String sqlUpdate = 
                "UPDATE usuario " +
                "SET nombre_completo = 'Katherine Ramirez Blandon' " +
                "WHERE documento = '10203040'";
            sentencia.executeUpdate(sqlUpdate);
            
            System.out.println(
                "[CRUD] UPDATE: Nombre de usuario " +
                "actualizado correctamente."
            );

            //READ: Consultar y listar los datos finales
         
            String sqlSelect = "SELECT * FROM usuario";
            resultado = sentencia.executeQuery(sqlSelect);
            
            System.out.println(
                "\n--- LISTADO DE USUARIOS EN LA BASE DE DATOS ---"
            );
            while (resultado.next()) {
                System.out.println(
                    "ID: " + resultado.getInt("id") + 
                    " | Rol: " + resultado.getInt("id_rol") +
                    " | Nombre: " + resultado.getString("nombre_completo") + 
                    " | Documento: " + resultado.getString("documento")
                );
            }
            System.out.println(
                "-----------------------------------------------\n"
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
