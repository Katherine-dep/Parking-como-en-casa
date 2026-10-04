package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionTarifasJDBC {

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

            // 1. CREATE: Se registra una nueva tarifa utilizando marcadores de posición "?"
            String sqlInsert = 
                "INSERT INTO tarifas (id, id_tipo_vehiculo, modalidad, valor_unidad, tarifa_vigente) " +
                "VALUES (NULL, ?, ?, ?, ?)";
            
            psInsert = conexion.prepareStatement(sqlInsert);
            
            // Se asignan los valores resguardando los tipos de datos correctos
            psInsert.setInt(1, 1);             // 1er "?": id_tipo_vehiculo (Camioneta)
            psInsert.setString(2, "Hora");     // 2do "?": modalidad
            psInsert.setDouble(3, 4500.00);    // 3er "?": valor_unidad (Ejemplo: $4,500 la hora)
            psInsert.setInt(4, 1);             // 4to "?": tarifa_vigente (1 = Activa)
            psInsert.executeUpdate();
            
            System.out.println("[CRUD TARIFAS] CREATE: Se proceso el registro de la tarifa por hora con exito.");

            // 2. UPDATE: Se modifica el valor de la unidad utilizando el ID de la tarifa como filtro
            String sqlUpdate = 
                "UPDATE tarifas " +
                "SET valor_unidad = ? " +
                "WHERE id = ?";
            
            psUpdate = conexion.prepareStatement(sqlUpdate);
            psUpdate.setDouble(1, 4800.00); // Nuevo valor asignado (Incremento a $4,800)
            psUpdate.setInt(2, 1);          // Filtro por el ID objetivo
            psUpdate.executeUpdate();
            
            System.out.println("[CRUD TARIFAS] UPDATE: Se actualizo el valor de la tarifa correctamente.");

            // 3. READ: Se consulta y se lista el esquema de tarifas configurado en el sistema
            String sqlSelect = "SELECT * FROM tarifas";
            psSelect = conexion.prepareStatement(sqlSelect);
            resultado = psSelect.executeQuery();
            
            System.out.println("\n--- LISTADO DE TARIFAS DE PARQUEADERO ---");
            while (resultado.next()) {
                System.out.println(
                    "ID Tarifa: " + resultado.getInt("id") + 
                    " | Tipo Vehiculo ID: " + resultado.getInt("id_tipo_vehiculo") + 
                    " | Modalidad: " + resultado.getString("modalidad") + 
                    " | Valor Unidad: $" + resultado.getDouble("valor_unidad") + 
                    " | Vigente: " + (resultado.getInt("tarifa_vigente") == 1 ? "SI" : "NO")
                );
            }
            System.out.println("----------------------------------------\n");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionTarifasJDBC.class.getName())
                .log(Level.SEVERE, "No se encontro el Driver de MySQL", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionTarifasJDBC.class.getName())
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
                Logger.getLogger(ConexionTarifasJDBC.class.getName())
                    .log(Level.SEVERE, "Fallo al intentar cerrar los componentes", ex);
            }
        }
    }
}

