package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionCajasJDBC {

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
            // 1. Registrar y cargar el driver
            Class.forName(driver);
            
            // 2. Establecer el puente seguro con la base de datos
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("=== CONEXION EXITOSA A XAMPP ===");
            
            // 1. CREATE: Apertura de una nueva caja (Montos finales inician en NULL)
            String sqlInsertCajas = 
                "INSERT INTO cajas (id, id_usuario, fecha_hora_apertura, fecha_hora_cierre, monto_inicial, monto_final_calculado, monto_final_real) " +
                "VALUES (NULL, ?, NOW(), NULL, ?, NULL, NULL)";
            
            psInsert = conexion.prepareStatement(sqlInsertCajas);
            psInsert.setInt(1, 1);           // id_usuario
            psInsert.setDouble(2, 50000.00);  // monto_inicial
            psInsert.executeUpdate();
            
            System.out.println(
                "[CRUD CAJAS] CREATE: Apertura de caja registrada con exito."
            );

            // 2. UPDATE: Cierre de caja (Simulamos que el sistema calcula $120,000 y el cajero entrega $120,000 reales)
            String sqlUpdateCajas = 
                "UPDATE cajas " +
                "SET fecha_hora_cierre = NOW(), monto_final_calculado = ?, monto_final_real = ? " +
                "WHERE id_usuario = ? AND fecha_hora_cierre IS NULL";
            
            psUpdate = conexion.prepareStatement(sqlUpdateCajas);
            psUpdate.setDouble(1, 120000.00); // monto_final_calculado
            psUpdate.setDouble(2, 120000.00); // monto_final_real
            psUpdate.setInt(3, 1);            // id_usuario para cerrar su caja activa
            psUpdate.executeUpdate();
            
            System.out.println(
                "[CRUD CAJAS] UPDATE: Cierre y arqueo de caja procesado correctamente."
            );

            // 3. READ: Consultar y listar el historial de estados de las cajas
            String sqlSelect = "SELECT * FROM cajas";
            psSelect = conexion.prepareStatement(sqlSelect);
            resultado = psSelect.executeQuery();
            
            System.out.println(
                "\n--- HISTORIAL DE CAJAS EN PARKING COMO EN CASA ---"
            );
            while (resultado.next()) {
                System.out.println(
                    "ID Caja: " + resultado.getInt("id") + 
                    " | Usuario ID: " + resultado.getInt("id_usuario") + 
                    " | Apertura: " + resultado.getTimestamp("fecha_hora_apertura") +
                    " | Cierre: " + resultado.getTimestamp("fecha_hora_cierre") +
                    " | M. Inicial: $" + resultado.getDouble("monto_inicial") +
                    " | M. Final Real: $" + resultado.getDouble("monto_final_real")
                );
            }
            System.out.println(
                "---------------------------------------------------\n"
            );

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionCajasJDBC.class.getName())
                .log(Level.SEVERE, "Driver no encontrado", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionCajasJDBC.class.getName())
                .log(Level.SEVERE, "Error de SQL ejecutado", ex);
        } finally {
            // Cierre seguro y obligatorio de recursos abiertos
            try {
                if (resultado != null) resultado.close();
                if (psInsert != null) psInsert.close();
                if (psUpdate != null) psUpdate.close();
                if (psSelect != null) psSelect.close();
                if (conexion != null) conexion.close();
                System.out.println("=== CONEXIONES CERRADAS SEGURAS ===");
            } catch (SQLException ex) {
                Logger.getLogger(ConexionCajasJDBC.class.getName())
                    .log(Level.SEVERE, "Error al cerrar componentes", ex);
            }
        }
    }
}
