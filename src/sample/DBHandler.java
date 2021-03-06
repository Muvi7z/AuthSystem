package sample;
import sample.Data.Log;
import sample.Data.User;

import java.sql.*;

public class DBHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        String connection = "jdbc:mysql://"+ dbHost + ":" + dbPort + "/" + dbName; //"jdbc:oracle:thin:@"+ dbHost + ":" + dbPort+ ":" +dbSID;
        Class.forName("com.mysql.cj.jdbc.Driver");

//------*ORACLE*---------------------------------------------------------------
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//        } catch (ClassNotFoundException e) {
//            System.out.println("Oracle JDBC Driver is not found");
//            e.printStackTrace();
//        }
//---------------------------------------------------------------------------------------
        dbConnection = DriverManager.getConnection(connection, dbUser,dbPass);
        return dbConnection;
    }

    public void addUser(User user) throws ClassNotFoundException, SQLException{
        String insert = "INSERT into `"+ Const.USER_TABLE + "`(`"
                + Const.USER_LOGIN + "`,`" + Const.USER_PASS + "`,`" + Const.USER_GROUP+ "`,`" +Const.USER_SALT+"`)"
                + "VALUES(?,?,?,?)";
        System.out.println(insert);
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, user.getLogin());
        prSt.setString(2, user.getPass());
        prSt.setString(3, user.getGroup());
        prSt.setBytes(4, user.getSalt());
        prSt.executeUpdate();

    }
    public void editUser(User user) throws ClassNotFoundException, SQLException{
        String insert = "UPDATE `"+ Const.USER_TABLE + "` SET `" +Const.USER_LOGIN
                + "` = ?,`"+Const.USER_GROUP+ "` = ?,`" + Const.USER_BLOCK+ "` = ?";
        System.out.println(insert);
        if(user.getSalt() != null){
            insert += ",`"+Const.USER_SALT+"` = ?,`"+Const.USER_PASS + "`= ?"+" WHERE(`"+Const.USERS_ID+"` = ?)";
        }
        else {
            insert += " WHERE(`"+Const.USERS_ID+"` = ?)";
        }
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, user.getLogin());
        prSt.setString(2, user.getGroup());
        prSt.setBoolean(3, user.getIsBlock());
        if(user.getSalt() != null){
            prSt.setBytes(4, user.getSalt());
            prSt.setString(5, user.getPass());
            prSt.setString(6, user.getId());
        }
        else prSt.setString(4, user.getId());
        prSt.executeUpdate();
    }
    public void setBlockUser(User user, Boolean block, String date) throws ClassNotFoundException, SQLException{
        String insert = "UPDATE `"+ Const.USER_TABLE + "` SET `" + Const.USER_BLOCK+ "` = ?,`"+Const.USER_TIMEBLOCK+"` = ?"
                +" WHERE(`"+Const.USERS_ID+"` = ?)";
        System.out.println(insert);
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setBoolean(1, block);
        prSt.setString(2, date);
        prSt.setString(3, user.getId());
        prSt.executeUpdate();
    }
    public void delUser(User user) throws ClassNotFoundException, SQLException{
        String insert = "DELETE FROM `"+Const.USER_TABLE+"` WHERE(`"+Const.USERS_ID+"` = ?)";
        System.out.println(insert);
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, user.getId());
        prSt.executeUpdate();

    }
    public ResultSet getUser(User user){
        ResultSet resSet = null;
        String select = "SELECT * from `"+Const.USER_TABLE +"` where "+
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
    public ResultSet getAllUsers(){
        ResultSet resSet = null;
        String select = "SELECT * from `"+Const.USER_TABLE+"`";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet= prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return resSet;
    }
    public void changeSettings(int TriedPass, long TimeBlock, long TimeOut) throws ClassNotFoundException, SQLException{
        String insert = "UPDATE `"+ Const.SETTINGS_TABLE + "` SET `" + Const.SETTINGS_TRIED+ "` = ?,`"+Const.SETTINGS_TIMEBLOCK+"` = ?,`"
                +Const.SETTINGS_TIMEOUT+"` = ?" +" WHERE(`"+Const.SETTINGS_ID+"` = ?)";
        System.out.println(insert);
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1,Integer.toString(TriedPass));
        prSt.setString(2, Long.toString(TimeBlock));
        prSt.setString(3, Long.toString(TimeOut));
        prSt.setString(4, "1");
        prSt.executeUpdate();
    }
    public ResultSet getSettings(){
        ResultSet resSet = null;
        String select = "SELECT * from `"+Const.SETTINGS_TABLE+"`";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet= prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return resSet;
    }
    public void addLog(Log log) throws ClassNotFoundException, SQLException{
        String insert = "INSERT into `"+ Const.LOG_TABLE + "`(`"
                + Const.LOG_DATE + "`,`" + Const.LOG_UNAME + "`,`" + Const.LOG_LEVEL+ "`,`" +Const.LOG_DESC+"`)"
                + "VALUES(?,?,?,?)";
        System.out.println(insert);
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, log.getDate());
        prSt.setString(2, log.getUserName());
        prSt.setString(3, log.getLevel());
        prSt.setString(4, log.getDescription());
        prSt.executeUpdate();

    }
    public ResultSet getAllLogs(){
        ResultSet resSet = null;
        String select = "SELECT * from `"+Const.LOG_TABLE+"`";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            resSet= prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return resSet;
    }
    public void deleteAllLogs() throws ClassNotFoundException, SQLException{
        String delete = "DELETE from "+Const.LOG_TABLE;
        System.out.println(delete);
        PreparedStatement prSt = getDbConnection().prepareStatement(delete);
        prSt.executeUpdate();
    }
}
