package dam.jmdh;

import java.sql.*;

public class JDBC_ProcAlmacenado {
    public static void main(String[] args) {
        String basedatos = "bbdd_ejemplos_libro";
        String host = "localhost";
        String port = "3306";
        String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + basedatos;
        String user = "root";
        String pwd = "12345";

        try (
                Connection c = DriverManager.getConnection(urlConnection, user, pwd);
                CallableStatement s = c.prepareCall("{call listado_parcial_clientes(?,?)}")
        ) {

            // Asigna parametros del procedimiento
            s.setString(1, "78901234X");
            s.setInt(2, 0);

            // Registra el segundo parámetro como párametro de salida
            s.registerOutParameter(2, java.sql.Types.INTEGER);

            // Llamada al procedimiento almacenado
            s.execute();
            ResultSet rs = s.getResultSet();

            // Recoge y muestra el valor del parámetro de salida, que es la longitud del nombre
            int inout_long = s.getInt(2);
            System.out.println("=> inout long: " + inout_long);

           // Muestra los clientes con apellidos menor al del dni del párametro de entrada
            int nCli = 0;
            while (rs.next()) {
                System.out.println("[" + (++nCli) + "]");
                System.out.println("DNI: " + rs.getString("DNI"));
                System.out.println("Apellidos: " + rs.getString("APELLIDOS"));
            }

        } catch (SQLException e) {
            muestraErrorSQL(e);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    public static void muestraErrorSQL(SQLException e) {
        System.err.println("SQL ERROR mensaje: " + e.getMessage());
        System.err.println("SQL Estado: " + e.getSQLState());
        System.err.println("SQL código específico: " + e.getErrorCode());
    }
}