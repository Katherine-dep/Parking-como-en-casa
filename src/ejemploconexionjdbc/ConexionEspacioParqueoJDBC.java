package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionEspacioParqueoJDBC {

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

            // 1. CREATE: Se inserta un nuevo espacio de parqueo utilizando parámetros seguros
            // Nota: Se usa 'INSERT IGNORE' debido a que la columna 'codigo_espacio' es UNIQUE
            String sqlInsert = 
                "INSERT IGNORE INTO espacio_parqueo (id, codigo_espacio, estado_actual) " +
                "VALUES (1, ?, ?)"; // Formamos el ID 1
            
            psInsert = conexion.prepareStatement(sqlInsert);
            psInsert.setString(1, "Celda 1");     // Se asigna el código del espacio
            psInsert.setString(2, "Disponible");  // Se define su estado inicial
            psInsert.executeUpdate();
            
            System.out.println("[CRUD ESPACIO PARQUEO] CREATE: Se proceso el registro del espacio 'Celda 1' con exito.");

            // 2. UPDATE: Se modifica el estado actual del espacio (ej. cuando entra un vehículo) utilizando su ID como filtro
            String sqlUpdate = 
                "UPDATE espacio_parqueo " +
                "SET estado_actual = ? " +
                "WHERE id = ?";
            
            psUpdate = conexion.prepareStatement(sqlUpdate);
            psUpdate.setString(1, "Ocupado"); // Nuevo estado del espacio
            psUpdate.setInt(2, 1);             // Filtro aplicado sobre la celda ID 1
            psUpdate.executeUpdate();
            
            System.out.println("[CRUD ESPACIO PARQUEO] UPDATE: Se actualizo el estado del espacio de parqueo correctamente.");

            // 3. READ: Se consulta y se lista el mapa total de celdas en el establecimiento
            String sqlSelect = "SELECT * FROM espacio_parqueo";
            psSelect = conexion.prepareStatement(sqlSelect);
            resultado = psSelect.executeQuery();
            
            System.out.println("\n--- MAPA DE ESPACIOS DE PARQUEO ---");
            while (resultado.next()) {
                System.out.println(
                    "ID Espacio: " + resultado.getInt("id") + 
                    " | Codigo: " + resultado.getString("codigo_espacio") +
                    " | Estado: " + resultado.getString("estado_actual")
                );
            }
            System.out.println("------------------------------------\n");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionEspacioParqueoJDBC.class.getName())
                .log(Level.SEVERE, "No se encontro el Driver de MySQL", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionEspacioParqueoJDBC.class.getName())
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
                Logger.getLogger(ConexionEspacioParqueoJDBC.class.getName())
                    .log(Level.SEVERE, "Fallo al intentar cerrar los componentes", ex);
            }
        }
    }
}
