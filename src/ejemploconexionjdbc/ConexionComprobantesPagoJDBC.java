package ejemploconexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionComprobantesPagoJDBC {

    public static void main(String[] args) {
        // Se definen las variables de conexión adaptadas al entorno local XAMPP
        String url = "jdbc:mysql://localhost:3306/parking_como_en_casa";
        String usuario = "root";
        String password = ""; 
        String driver = "com.mysql.cj.jdbc.Driver"; 
        
        Connection conexion = null;
        PreparedStatement psInsert = null;
        PreparedStatement psSelect = null;
        ResultSet resultado = null;

        try {
            // 1. Se registra y se carga el driver de MySQL en el sistema
            Class.forName(driver);
            
            // 2. Se establece el puente seguro con la base de datos del parqueadero
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("=== CONEXION EXITOSA A XAMPP ===");

            // 1. CREATE: Se emite un nuevo comprobante de pago utilizando marcadores de posición "?"
            // Nota: Se usa NOW() para registrar de forma automática el momento exacto del recaudo
            // Nota 2: Se usa 'INSERT IGNORE' debido a que 'numero_ticket' es UNIQUE
            String sqlInsert = 
                "INSERT IGNORE INTO comprobantes_pago (id, id_registro_parqueo, id_suscripcion, id_metodo_pago, numero_ticket, subtotal, impuestos, total_pagado, fecha_hora_pago) " +
                "VALUES (NULL, ?, NULL, ?, ?, ?, ?, ?, NOW())";
            
            psInsert = conexion.prepareStatement(sqlInsert);
            
            // Se asignan los valores de la transacción monetaria resguardando la seguridad
            psInsert.setInt(1, 1);                 // 1er "?": id_registro_parqueo
            psInsert.setInt(2, 1);                 // 2do "?": id_metodo_pago (Vinculado a Efectivo COP ID 1)
            psInsert.setString(3, "FAC-0001");     // 3er "?": numero_ticket (Valor único)
            psInsert.setDouble(4, 10000.00);       // 4to "?": subtotal
            psInsert.setDouble(5, 1900.00);        // 5to "?": impuestos (Ejemplo: IVA del 19%)
            psInsert.setDouble(6, 11900.00);       // 6to "?": total_pagado
            psInsert.executeUpdate();
            
            System.out.println("[CRUD COMPROBANTES] CREATE: Se emitio el comprobante de pago 'FAC-0001' con exito.");

            // 2. READ: Se consulta y se lista el registro total de facturación en la consola
            String sqlSelect = "SELECT * FROM comprobantes_pago";
            psSelect = conexion.prepareStatement(sqlSelect);
            resultado = psSelect.executeQuery();
            
            System.out.println("\n--- REGISTRO CENTRAL DE COMPROBANTES DE PAGO ---");
            while (resultado.next()) {
                System.out.println(
                    "Factura ID: " + resultado.getInt("id") + 
                    " | Ticket No: " + resultado.getString("numero_ticket") + 
                    " | Metodo Pago ID: " + resultado.getInt("id_metodo_pago") + 
                    " | Total Recaudado: $" + resultado.getDouble("total_pagado") + 
                    " | Fecha Pago: " + resultado.getTimestamp("fecha_hora_pago")
                );
            }
            System.out.println("-------------------------------------------------\n");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionComprobantesPagoJDBC.class.getName())
                .log(Level.SEVERE, "No se encontro el Driver de MySQL", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionComprobantesPagoJDBC.class.getName())
                .log(Level.SEVERE, "Se detecto un error en el SQL ejecutado", ex);
        } finally {
            // Se cierran de forma obligatoria y segura todos los componentes y flujos abiertos
            try {
                if (resultado != null) resultado.close();
                if (psInsert != null) psInsert.close();
                if (psSelect != null) psSelect.close();
                if (conexion != null) conexion.close();
                System.out.println("=== CONEXIONES CERRADAS SEGURAS ===");
            } catch (SQLException ex) {
                Logger.getLogger(ConexionComprobantesPagoJDBC.class.getName())
                    .log(Level.SEVERE, "Fallo al intentar cerrar los componentes", ex);
            }
        }
    }
}
