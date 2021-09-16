package sample;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        String connection = "jdbc:mysql://"+ dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connection, dbUser,dbPass);
        return dbConnection;
    }

}
