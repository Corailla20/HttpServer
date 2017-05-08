package serverhttp;

/**
 * Created by Corailla20 on 03/03/2016.
 */
import java.sql.*;
import java.util.Properties;

/**
 * Represents a database connection (create custom result sets)
 */
public class DatabaseConnection {

    private Connection db;
    public ResultSet resultSet;

    public DatabaseConnection(String host, String port, String user, String password, String dbname) throws SQLException {
        this.db = DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":" + port + ":" + dbname, user, password);
    }

    public DatabaseSet query(String sql) throws SQLException {
        try{
            resultSet = this.db.prepareStatement(sql).executeQuery();
        }
        catch (java.sql.SQLException e){
            db.close();
            System.out.println("\n\n\nConnection fermée\n\n\n");
            this.db = DriverManager.getConnection("jdbc:oracle:thin:@***.***.***.***:1521:" + "orcl", "sys as sysdba", "*****");
            resultSet = this.db.prepareStatement(sql).executeQuery();
        }
        return new DatabaseSet(resultSet);
    }

}