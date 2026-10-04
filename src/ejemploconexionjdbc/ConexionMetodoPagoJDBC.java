package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionMetodoPagoJDBC {

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

            // 1. CREATE: Se inserta un nuevo método de pago utilizando parámetros seguros
            // Nota: Se usa 'INSERT IGNORE' debido a que la columna 'nombre_metodo' es UNIQUE
            String sqlInsert = 
                "INSERT IGNORE INTO metodo_pago (id, nombre_metodo) " +
                "VALUES (NULL, ?)";
            
            psInsert = conexion.prepareStatement(sqlInsert);
            psInsert.setString(1, "Efectivo"); // Se asigna el primer método base
            psInsert.executeUpdate();
            
            System.out.println("[CRUD METODO PAGO] CREATE: Se proceso el registro de 'Efectivo' con exito.");

            // 2. UPDATE: Se modifica la descripción de un método de pago utilizando su ID como filtro
            String sqlUpdate = 
                "UPDATE metodo_pago " +
                "SET nombre_metodo = ? " +
                "WHERE id = ?";
            
            psUpdate = conexion.prepareStatement(sqlUpdate);
            psUpdate.setString(1, "Efectivo COP"); // Se actualiza la descripción del método
            psUpdate.setInt(2, 1);                  // Filtro por el ID objetivo
            psUpdate.executeUpdate();
            
            System.out.println("[CRUD METODO PAGO] UPDATE: Se actualizo el nombre del metodo de pago correctamente.");

            // 3. READ: Se consulta y se lista el catálogo completo de métodos de pago
            String sqlSelect = "SELECT * FROM metodo_pago";
            psSelect = conexion.prepareStatement(sqlSelect);
            resultado = psSelect.executeQuery();
            
            System.out.println("\n--- CATALOGO DE METODOS DE PAGO ---");
            while (resultado.next()) {
                System.out.println(
                    "ID Método: " + resultado.getInt("id") + 
                    " | Descripción: " + resultado.getString("nombre_metodo")
                );
            }
            System.out.println("------------------------------------\n");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionMetodoPagoJDBC.class.getName())
                .log(Level.SEVERE, "No se encontro el Driver de MySQL", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionMetodoPagoJDBC.class.getName())
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
                Logger.getLogger(ConexionMetodoPagoJDBC.class.getName())
                    .log(Level.SEVERE, "Fallo al intentar cerrar los componentes", ex);
            }
        }
    }
}
