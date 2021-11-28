package sample.Controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.*;
import sample.Data.*;

public class AuthController extends Window {
    int tried = 0;

    @FXML
    private Label triedLeftLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Button minimizeBtn;

    @FXML
    private Button exitBtn;


    @FXML
    private Button signUpBtn;

    @FXML
    private TextField loginField;

    @FXML
    private Button signInBtn;

    @FXML
    private PasswordField passField;
    @FXML
    void initialize() {
        tried=0;
        updateSettings();
        errorLabel.setVisible(false);
        triedLeftLabel.setVisible(false);
        signInBtn.setOnAction(event -> {
            String login = loginField.getText().trim();
            String pass = passField.getText().trim();
            if(!login.equals("")){
                if(!pass.equals("")){
                    try {
                        loginUser(login,pass);
                    } catch (SQLException throwable) {
                        throwable.printStackTrace();
                    }
                    catch (ClassNotFoundException e){
                        System.out.println(e.getMessage());
                    }
                }else {
                    try {
                        error("Введите пароль",Paint.valueOf("f51f1f"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                try {
                    error("Введите логин!",Paint.valueOf("f51f1f"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        exitBtn.setOnAction(event -> close(exitBtn.getScene()));
        minimizeBtn.setOnAction(event -> close(minimizeBtn.getScene()));
        signUpBtn.setOnAction(event -> launchNewWindow("regist.fxml", signUpBtn.getScene(),signUpBtn.getScene().getWindow().getX(),signUpBtn.getScene().getWindow().getY(),null,signUpBtn.getScene()));
    }

    private void loginUser(String login, String pass) throws SQLException, ClassNotFoundException {
        DBHandler dbHandler = new DBHandler();
        User user = new User();
        user.setLogin(login);
        ResultSet result = dbHandler.getUser(user);
        if (result.next()){
            user.setPass(result.getString(Const.USER_PASS));
            user.setSalt(result.getBytes(Const.USER_SALT));
            user.setId(result.getString(Const.USERS_ID));
            user.setDateBlock(result.getTimestamp(Const.USER_TIMEBLOCK));
            user.setIsBlock(result.getBoolean(Const.USER_BLOCK));
            if (user.getIsBlock() && (new Date()).after(user.getDateBlock())) {
                user.setIsBlock(false);
                dbHandler.setBlockUser(user, false, null);
            }
            if(!user.getIsBlock()) {
                if (Password.hashingPass(pass, user.getSalt()).equals(user.getPass())) {
                    user.setGroup(result.getString(Const.USER_GROUP));
                    Log log = new Log(new Date(), login, Log.Levels.INFO, "Успешный вход пользователя в систему");
                    dbHandler.addLog(log);
                    error("Успешный вход", Paint.valueOf("00ff73"));
                    tried=0;
                    triedLeftLabel.setVisible(false);
                    launchNewWindow("sample.fxml", signInBtn.getScene(), null, null, user,signInBtn.getScene());

                } else {
                    try {
                        error("Введне неверный пароль!", Paint.valueOf("f51f1f"));
                        tried++;
                        setTriedLeft("Осталось попыток: " + (Settings.Tried_Pass - tried));
                        if ((Settings.Tried_Pass - tried) <= 0) {
                            Log log = new Log(new Date(), null, Log.Levels.INFO, "Несанкционированый доступ к пользователю " + login);
                            dbHandler.addLog(log);

                            Date date = new Date(new Date().getTime() + (Settings.TimeBlock * 60000L));
                            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                            setTriedLeft("Аккаунт заблокирован: " + formatForDateNow.format(date));
                            dbHandler.setBlockUser(user, true, formatForDateNow.format(date));
                            log = new Log(new Date(), user.getLogin(), Log.Levels.INFO, "Аккаут был заблокирован до: " + formatForDateNow.format(date));
                            dbHandler.addLog(log);
                            tried = 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                try {
                    error("Пользователь с таким логином заблокирован!", Paint.valueOf("f51f1f"));
                    setTriedLeft("Заблокирован до: " + user.getDateBlock());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            try {
                error("Пользователь с таким логином не найден!",Paint.valueOf("f51f1f"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void error(String text, Paint paint){
        errorLabel.setVisible(true);
        errorLabel.setTextFill(paint);
        errorLabel.setText(text);
    }
    public void setTriedLeft(String text){
        triedLeftLabel.setVisible(true);
        triedLeftLabel.setText(text);
    }
    public void updateSettings(){
        DBHandler dbHandler = new DBHandler();
        ResultSet result = dbHandler.getSettings();
        try {
            if (result.next()){
                Settings.Tried_Pass = Integer.parseInt(result.getString(Const.SETTINGS_TRIED));
                Settings.TimeBlock = Long.parseLong(result.getString(Const.SETTINGS_TIMEBLOCK));
                Settings.TimeOut = Long.parseLong(result.getString(Const.SETTINGS_TIMEOUT));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



}
