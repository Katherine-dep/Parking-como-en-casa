package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionCredencialesAccesoJDBC {

    public static void main(String[] args) {
        // Se definen las variables de conexión adaptadas al entorno local XAMPP
        String url = "jdbc:mysql://localhost:3306/parking_como_en_casa";
        String usuario = "root";
        String password = ""; 
        String driver = "com.mysql.cj.jdbc.Driver"; 
        
        Connection conexion = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psSelect = null;
        ResultSet resultado = null;

        try {
            // 1. Se registra y se carga el driver de MySQL en el sistema
            Class.forName(driver);
            
            // 2. Se establece el puente seguro con la base de datos
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("=== CONEXION EXITOSA A XAMPP ===");

            // 1. CREATE: Se registran las credenciales de un usuario (el campo 'ultimo_acceso' inicia en NULL)
            // Nota: Se usa 'INSERT IGNORE' debido a las restricciones UNIQUE de id_usuario y correo
            String sqlInsert = 
                "INSERT IGNORE INTO credenciales_acceso (id, id_usuario, correo, clave, ultimo_acceso, cuenta_activa) " +
                "VALUES (NULL, ?, ?, ?, NULL, ?)";
            
            psInsert = conexion.prepareStatement(sqlInsert);
            
            // Se asignan los valores de prueba resguardando la seguridad
            psInsert.setInt(1, 1);                            // 1er "?": id_usuario
            psInsert.setString(2, "katherine@parqueo.com");   // 2do "?": correo electrónico de ingreso
            psInsert.setString(3, "claveSegura123");          // 3er "?": clave (en un futuro se almacenará cifrada)
            psInsert.setInt(4, 1);                            // 4to "?": cuenta_activa (1 = Sí, Activa)
            psInsert.executeUpdate();
            
            System.out.println("[CRUD CREDENCIALES] CREATE: Se crearon las credenciales de acceso con exito.");

            // 2. UPDATE: Se simula un inicio de sesion exitoso actualizando la columna 'ultimo_acceso' a la hora actual
            String sqlUpdate = 
                "UPDATE credenciales_acceso " +
                "SET ultimo_acceso = NOW() " +
                "WHERE id_usuario = ?";
            
            psUpdate = conexion.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, 1); // Filtro para actualizar al usuario ID 1
            psUpdate.executeUpdate();
            
            System.out.println("[CRUD CREDENCIALES] UPDATE: Se registro la marca de ultimo_acceso correctamente.");

            // 3. READ: Se consulta y se valida el estado de las cuentas en el sistema
            String sqlSelect = "SELECT * FROM credenciales_acceso";
            psSelect = conexion.prepareStatement(sqlSelect);
            resultado = psSelect.executeQuery();
            
            System.out.println("\n--- LISTADO DE CREDENCIALES EN EL SISTEMA ---");
            while (resultado.next()) {
                System.out.println(
                    "ID Cuenta: " + resultado.getInt("id") + 
                    " | Usuario ID: " + resultado.getInt("id_usuario") + 
                    " | Correo: " + resultado.getString("correo") + 
                    " | Ultimo Ingreso: " + resultado.getTimestamp("ultimo_acceso") + 
                    " | Estado Activa: " + (resultado.getInt("cuenta_activa") == 1 ? "SI" : "NO")
                );
            }
            System.out.println("----------------------------------------------\n");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionCredencialesAccesoJDBC.class.getName())
                .log(Level.SEVERE, "No se encontro el Driver de MySQL", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionCredencialesAccesoJDBC.class.getName())
                .log(Level.SEVERE, "Se detecto un error en el SQL ejecutado", ex);
        } finally {
            // Se cierran de forma obligatoria y segura todos los componentes y flujos abiertos
            try {
                if (resultado != null) resultado.close();
                if (psInsert != null) psInsert.close();
                if (psUpdate != null) psUpdate.close();
                if (psSelect != null) psSelect.close();
                if (conexion != null) conexion.close();
                System.out.println("=== CONEXIONES CERRADAS SEGURAS ===");
            } catch (SQLException ex) {
                Logger.getLogger(ConexionCredencialesAccesoJDBC.class.getName())
                    .log(Level.SEVERE, "Fallo al intentar cerrar los componentes", ex);
            }
        }
    }
}

