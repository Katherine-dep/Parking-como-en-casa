package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionSuscripcionesMensualesJDBC {

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
            
            // 2. Se establece el puente seguro con la base de datos del parqueadero
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("=== CONEXION EXITOSA A XAMPP ===");
            
            // 1. CREATE: Se registra una nueva suscripción mensual utilizando marcadores de posición "?"
            // Nota: CURDATE() es una función de MySQL que guarda la fecha actual (Año-Mes-Día) sin la hora, ideal para campos 'date'
            // Nota 2: DATE_ADD(CURDATE(), INTERVAL 1 MONTH) calcula de forma automática la fecha exacta de vencimiento en un mes
            String sqlInsert = 
                "INSERT INTO suscripciones_mensuales (id, id_cliente, id_placa, fecha_inicio, fecha_fin, valor_mensualidad, estado_suscripcion) " +
                "VALUES (NULL, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), ?, ?)";
            
            psInsert = conexion.prepareStatement(sqlInsert);
            
            // Se asignan los valores dinámicos respetando la integridad de las llaves foráneas
            psInsert.setInt(1, 1);                  // 1er "?": id_cliente
            psInsert.setString(2, "ABC123");       // 2do "?": id_placa 
            psInsert.setDouble(3, 150000.00);      // 3er "?": valor_mensualidad 
            psInsert.setString(4, "Activa");       // 4to "?": estado_suscripcion
            psInsert.executeUpdate();
            
            System.out.println("[CRUD SUSCRIPCIONES] CREATE: Se registro la suscripcion mensual con exito.");

            // 2. UPDATE: Se modifica el estado de la suscripción (ej: cuando pasa el tiempo y se vence) usando el ID como filtro
            String sqlUpdate = 
                "UPDATE suscripciones_mensuales " +
                "SET estado_suscripcion = ? " +
                "WHERE id = ?";
            
            psUpdate = conexion.prepareStatement(sqlUpdate);
            psUpdate.setString(1, "Vencida"); // Nuevo estado asignado
            psUpdate.setInt(2, 1);            // Filtro aplicado sobre el ID objetivo
            psUpdate.executeUpdate();
            
            System.out.println("[CRUD SUSCRIPCIONES] UPDATE: Se actualizo el estado de la suscripcion correctamente.");

            // 3. READ: Se consulta y se lista la bitácora total de afiliados mensuales
            String sqlSelect = "SELECT * FROM suscripciones_mensuales";
            psSelect = conexion.prepareStatement(sqlSelect);
            resultado = psSelect.executeQuery();
            
            System.out.println("\n--- LISTADO DE AFILIADOS MENSUALES ---");
            while (resultado.next()) {
                System.out.println(
                    "Suscripcion ID: " + resultado.getInt("id") + 
                    " | Cliente ID: " + resultado.getInt("id_cliente") + 
                    " | Placa: " + resultado.getString("id_placa") + 
                    " | Vence: " + resultado.getDate("fecha_fin") + 
                    " | Valor: $" + resultado.getDouble("valor_mensualidad") + 
                    " | Estado: " + resultado.getString("estado_suscripcion")
                );
            }
            System.out.println("--------------------------------------\n");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionSuscripcionesMensualesJDBC.class.getName())
                .log(Level.SEVERE, "No se encontro el Driver de MySQL", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionSuscripcionesMensualesJDBC.class.getName())
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
                Logger.getLogger(ConexionSuscripcionesMensualesJDBC.class.getName())
                    .log(Level.SEVERE, "Fallo al intentar cerrar los componentes", ex);
            }
        }
    }
}

