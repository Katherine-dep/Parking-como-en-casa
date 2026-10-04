package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionTipoVehiculoJDBC {

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

            // 1. CREATE: Se inserta un nuevo tipo de vehículo 
            // Nota: Se usa 'INSERT IGNORE' debido a que la columna 'nombre_tipo' es UNIQUE en la base de datos
            String sqlInsert = 
                "INSERT IGNORE INTO tipo_vehiculo (id, nombre_tipo) " +
                "VALUES (NULL, ?)";
            
            psInsert = conexion.prepareStatement(sqlInsert);
            psInsert.setString(1, "Automovil"); // Se asigna el nombre del primer tipo base
            psInsert.executeUpdate();
            
            System.out.println("[CRUD TIPO VEHICULO] CREATE: Se proceso el registro del tipo 'Automovil' con exito.");

            // 2. UPDATE: Se modifica el nombre de un tipo de vehículo existente utilizando su ID como filtro
            // Asumimos que el registro insertado tomó el ID 1 por ser auto-incremental
            String sqlUpdate = 
                "UPDATE tipo_vehiculo " +
                "SET nombre_tipo = ? " +
                "WHERE id = ?";
            
            psUpdate = conexion.prepareStatement(sqlUpdate);
            psUpdate.setString(1, "camioneta"); // Se actualiza la descripción del tipo
            psUpdate.setInt(2, 1);                         // Filtro por el ID objetivo
            psUpdate.executeUpdate();
            
            System.out.println("[CRUD TIPO VEHICULO] UPDATE: Se actualizo el nombre del tipo de vehiculo correctamente.");

            // 3. READ: Se consulta y se lista el catálogo completo de tipos de vehículos
            String sqlSelect = "SELECT * FROM tipo_vehiculo";
            psSelect = conexion.prepareStatement(sqlSelect);
            resultado = psSelect.executeQuery();
            
            System.out.println("\n--- CATALOGO DE TIPOS DE VEHICULOS ---");
            while (resultado.next()) {
                System.out.println(
                    "ID Tipo: " + resultado.getInt("id") + 
                    " | Descripcion: " + resultado.getString("nombre_tipo")
                );
            }
            System.out.println("---------------------------------------\n");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionTipoVehiculoJDBC.class.getName())
                .log(Level.SEVERE, "No se encontro el Driver de MySQL", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionTipoVehiculoJDBC.class.getName())
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
                Logger.getLogger(ConexionTipoVehiculoJDBC.class.getName())
                    .log(Level.SEVERE, "Fallo al intentar cerrar los componentes", ex);
            }
        }
    }
}

