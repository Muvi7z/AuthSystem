package sample;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        String connection = "jdbc:mysql://"+ dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connection, dbUser,dbPass);
        return dbConnection;
    }

    public void addUser(User user){
        String insert = "INSERT into "+ Const.USER_TABLE + "("
                + Const.USER_LOGIN + "," + Const.USER_PASS + "," + Const.USER_GROUP+")"
                + "VALUES(?,?,?)";
        System.out.println(insert);

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getPass());
            prSt.setString(3, user.getGroup());
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
