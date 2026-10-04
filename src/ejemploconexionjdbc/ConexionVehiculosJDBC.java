package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionVehiculosJDBC {

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

            // 1. CREATE: Se registra un nuevo vehículo utilizando marcadores de posición "?"
            //Se usa 'INSERT IGNORE' para evitar caídas si la placa 'ABC123' ya existe 
            String sqlInsert = 
                "INSERT IGNORE INTO vehiculos (placa, id_tipo_vehiculo, marca, modelo, color) " +
                "VALUES (?, ?, ?, ?, ?)";
            
            psInsert = conexion.prepareStatement(sqlInsert);
            
            // Se asignan los valores dinámicos resguardando la seguridad del sistema
            psInsert.setString(1, "ABC123");       // placa (Llave primaria)
            psInsert.setInt(2, 1);                  // id_tipo_vehiculo
            psInsert.setString(3, "Chevrolet");     // marca
            psInsert.setString(4, "Onix 2023");     // modelo
            psInsert.setString(5, "Gris Plata");    // color
            psInsert.executeUpdate();
            
            System.out.println("[CRUD VEHICULOS] CREATE: Se proceso el registro del vehiculo 'ABC123' con exito.");

            // 2. UPDATE: Se modifican los datos del vehículo (ej. cambio de color o modelo) usando la placa como filtro
            String sqlUpdate = 
                "UPDATE vehiculos " +
                "SET color = ?, modelo = ? " +
                "WHERE placa = ?";
            
            psUpdate = conexion.prepareStatement(sqlUpdate);
            
            // Se escriben los nuevos datos de actualización
            psUpdate.setString(1, "Negro Perlado"); // Nuevo color
            psUpdate.setString(2, "Onix Turbo 2024"); // Modelo corregido
            psUpdate.setString(3, "ABC123");       // Placa del carro objetivo
            psUpdate.executeUpdate();
            
            System.out.println("[CRUD VEHICULOS] UPDATE: Se actualizaron los datos del vehiculo correctamente.");

            // 3. READ: Se consulta y se extrae el inventario de vehículos registrados
            String sqlSelect = "SELECT * FROM vehiculos";
            psSelect = conexion.prepareStatement(sqlSelect);
            resultado = psSelect.executeQuery();
            
            System.out.println("\n--- LISTADO DE VEHICULOS EN PARKING COMO EN CASA ---");
            while (resultado.next()) {
                System.out.println(
                    "Placa: " + resultado.getString("placa") + 
                    " | Tipo ID: " + resultado.getInt("id_tipo_vehiculo") + 
                    " | Marca: " + resultado.getString("marca") + 
                    " | Modelo: " + resultado.getString("modelo") + 
                    " | Color: " + resultado.getString("color")
                );
            }
            System.out.println("----------------------------------------------------\n");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionVehiculosJDBC.class.getName())
                .log(Level.SEVERE, "No se encontro el Driver de MySQL", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionVehiculosJDBC.class.getName())
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
                Logger.getLogger(ConexionVehiculosJDBC.class.getName())
                    .log(Level.SEVERE, "Fallo al intentar cerrar los componentes", ex);
            }
        }
    }
}
