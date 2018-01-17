
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getDBConnection() {
        Connection connection;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            connection = DriverManager.getConnection("jdbc:odbc:Bus");
            return connection;
        } catch (Exception ex) {
            return null;
        }//try catch closed
    }//getDBConnection() closed
}//class closed
