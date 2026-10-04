package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionRegistrosParqueoJDBC {

    public static void main(String[] args) {
        // Variables de conexion adaptadas al entorno local XAMPP 
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
            // 1. Se registra y se carga el driver 
            Class.forName(driver);
            
            // 2. Se establece el puente seguro con la base de datos 
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("=== CONEXION EXITOSA A XAMPP ===");

            // 1. CREATE: Se registra el ingreso de un vehículo (se deja la salida y el tiempo inicializados en NULL)
            String sqlInsert = 
                "INSERT INTO registros_parqueo (id, id_placa, id_usuario, id_suscripcion, id_espacio_parqueo, fecha_hora_ingreso, fecha_hora_salida, tiempo_permanencia) " +
                "VALUES (NULL, ?, ?, NULL, ?, NOW(), NULL, NULL)";
            
            psInsert = conexion.prepareStatement(sqlInsert);
            
            // Se asignan los valores dinámicos en las cajas de seguridad correspondientes
            psInsert.setString(1, "ABC123"); // Se asocia la placa 
            psInsert.setInt(2, 1);          // Se asocia el usuario administrador 
            psInsert.setInt(3, 1);          // Se asigna el espacio o celda número 1 del parqueadero
            psInsert.executeUpdate();
            
            System.out.println("[CRUD PARQUEO] CREATE: Se registro el ingreso del vehiculo correctamente.");

            // 2. UPDATE: Se registra la salida del vehículo (se captura la hora actual y se guarda un tiempo simulado de 120 minutos)
            String sqlUpdate = 
                "UPDATE registros_parqueo " +
                "SET fecha_hora_salida = NOW(), tiempo_permanencia = ? " +
                "WHERE id_placa = ? AND fecha_hora_salida IS NULL";
            
            psUpdate = conexion.prepareStatement(sqlUpdate);
            
            // Se actualizan los datos del vehículo que está abandonando la celda
            psUpdate.setInt(1, 120);         // Se calcula el tiempo de permanencia (Ejemplo: 120 minutos)
            psUpdate.setString(2, "ABC123"); // Se busca la placa específica que va a salir del establecimiento
            psUpdate.executeUpdate();
            
            System.out.println("[CRUD PARQUEO] UPDATE: Se proceso la salida del vehiculo con exito.");

            // 3. READ: Se consulta y se extrae toda la bitácora de movimientos para listarla en consola
            String sqlSelect = "SELECT * FROM registros_parqueo";
            psSelect = conexion.prepareStatement(sqlSelect);
            resultado = psSelect.executeQuery();
            
            System.out.println("\n--- BITACORA DE INGRESOS Y SALIDAS ---");
            while (resultado.next()) {
                System.out.println(
                    "Registro ID: " + resultado.getInt("id") + 
                    " | Placa: " + resultado.getString("id_placa") + 
                    " | Espacio: " + resultado.getInt("id_espacio_parqueo") +
                    " | Ingreso: " + resultado.getTimestamp("fecha_hora_ingreso") +
                    " | Permanencia: " + resultado.getInt("tiempo_permanencia") + " min"
                );
            }
            System.out.println("---------------------------------------\n");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionRegistrosParqueoJDBC.class.getName())
                .log(Level.SEVERE, "No se encontro el Driver de MySQL", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionRegistrosParqueoJDBC.class.getName())
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
                Logger.getLogger(ConexionRegistrosParqueoJDBC.class.getName())
                    .log(Level.SEVERE, "Fallo al intentar cerrar los componentes", ex);
            }
        }
    }
}
