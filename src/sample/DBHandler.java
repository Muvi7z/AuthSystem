package sample;
import sample.Data.User;

import java.sql.*;

public class DBHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        String connection = "jdbc:mysql://"+ dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connection, dbUser,dbPass);
        return dbConnection;
    }

    public void addUser(User user) throws ClassNotFoundException, SQLException{
        String insert = "INSERT into "+ Const.USER_TABLE + "(`"
                + Const.USER_LOGIN + "`,`" + Const.USER_PASS + "`,`" + Const.USER_GROUP+"`)"
                + "VALUES(?,?,?)";
        System.out.println(insert);
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, user.getLogin());
        prSt.setString(2, user.getPass());
        prSt.setString(3, user.getGroup());
        prSt.executeUpdate();

    }

    public ResultSet getUser(User user){
        ResultSet resSet = null;
        String select = "SELECT * from "+Const.USER_TABLE +" where "+
                Const.USER_LOGIN + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getLogin());

            resSet= prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return resSet;
    }
}