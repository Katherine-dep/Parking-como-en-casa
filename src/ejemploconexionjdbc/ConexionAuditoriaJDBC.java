package ejemploconexionjdbc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionAuditoriaJDBC {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/parking_como_en_casa";
        String usuario = "root";
        String password = ""; 
        String driver = "com.mysql.cj.jdbc.Driver"; 
        
        Connection conexion = null;
        PreparedStatement psInsert = null;
        PreparedStatement psSelect = null;
        ResultSet resultado = null;

        try {
            // 1. Registrar y cargar el driver
            Class.forName(driver);
            
            // 2. Establecer el puente seguro con la base de datos
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("=== CONEXION EXITOSA A XAMPP ===");
            
            // 3. CAPTURA AUTOMÁTICA DE LA DIRECCIÓN IP REAL DEL USUARIO
            String ipRealUser = "127.0.0.1"; // IP de respaldo
            try {
                ipRealUser = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                System.out.println("No se pudo detectar la IP, usando local: " + e.getMessage());
            }

            // Usamos "?" como cajas de seguridad para proteger los datos ingresados
            String sqlInsertAuditoria = 
                "INSERT INTO auditoria (id, id_usuario, accion_realizada, tabla_afectada, descripcion_detalle, fecha_hora_accion, direccion_ip) " +
                "VALUES (NULL, ?, ?, ?, ?, NOW(), ?)";
            
            psInsert = conexion.prepareStatement(sqlInsertAuditoria);
            
            // Llenamos las cajas con la informacion correspondiente
            psInsert.setInt(1, 1); // id_usuario 
            psInsert.setString(2, "INGRESO VEHICULO"); // accion_realizada
            psInsert.setString(3, "registros_parqueo"); // tabla_afectada
            psInsert.setString(4, "Se registro ingreso de placas ABC-123 de forma dinamica"); // descripcion_detalle
            psInsert.setString(5, ipRealUser); // <--- Aqui se inyecta la IP real automatica
            
            psInsert.executeUpdate();
            System.out.println("[CRUD AUDITORIA] CREATE: Log guardado de forma segura con la IP: " + ipRealUser);

            // 4. READ: Consultar y listar los registros almacenados en consola
            String sqlSelect = "SELECT * FROM auditoria";
            psSelect = conexion.prepareStatement(sqlSelect);
            resultado = psSelect.executeQuery();
            
            System.out.println("\n--- LOG DE AUDITORIA EN PARKING COMO EN CASA ---");
            while (resultado.next()) {
                System.out.println(
                    "ID Audit: " + resultado.getInt("id") + 
                    " | Usuario ID: " + resultado.getInt("id_usuario") + 
                    " | Accion: " + resultado.getString("accion_realizada") +
                    " | IP Registrada: " + resultado.getString("direccion_ip") +
                    " | Fecha/Hora: " + resultado.getTimestamp("fecha_hora_accion")
                );
            }
            System.out.println("------------------------------------------------\n");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionClienteJDBC.class.getName()).log(Level.SEVERE, "Driver no encontrado", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionClienteJDBC.class.getName()).log(Level.SEVERE, "Error de SQL ejecutado", ex);
        } finally {
            // Cierre seguro y obligatorio de recursos abiertos
            try {
                if (resultado != null) resultado.close();
                if (psInsert != null) psInsert.close();
                if (psSelect != null) psSelect.close();
                if (conexion != null) conexion.close();
                System.out.println("=== CONEXIONES CERRADAS SEGURAS ===");
            } catch (SQLException ex) {
                Logger.getLogger(ConexionClienteJDBC.class.getName()).log(Level.SEVERE, "Error al cerrar componentes", ex);
            }
        }
    }
}

