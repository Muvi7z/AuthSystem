package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import sample.DBHandler;
import sample.Data.Controller;
import sample.Data.Log;
import sample.Data.Password;
import sample.Data.User;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Date;

public class SettingController implements Controller {
    private User user=null;
    public Scene prevScene;
    @FXML
    private Button saveBtn;

    @FXML
    private PasswordField passField;

    @FXML
    private PasswordField confPassField;

    @FXML
    private TextField nameField;
    @FXML
    private Label errorLabel;
    @FXML
    void initialize() {
        saveBtn.setOnAction(event -> editUser());
    }
    public void editUser(){
        DBHandler dbHandler = new DBHandler();
        final int minLog = 5;
        final int maxLog = 11;
        String login = nameField.getText();
        if(!login.equals("")) {
            errorLabel.setVisible(false);
            if (login.length() >= minLog && login.length() <= maxLog) {
                String pass = passField.getText();
                String confPass = confPassField.getText();
                String oldLogin = user.getLogin();
                user.setLogin(login);
                if(!pass.equals("") && pass.length()>5 && pass.equals(confPass)){
                    SecureRandom random = new SecureRandom();
                    byte[] salt = new byte[16];
                    random.nextBytes(salt);
                    user.setPass(Password.hashingPass(pass,salt));
                    user.setSalt(salt);
                }
                else if(!pass.equals("") && pass.equals(confPass)) {
                    try {
                        errorLabel.setVisible(true);
                        errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                        error("???????????? ???????????? ???????????? ???????? ???? ?????????? 6 ????????????????!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(!pass.equals("")){
                    try {
                        errorLabel.setVisible(true);
                        errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                        error("???????????? ???? ??????????????????!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(pass.equals("") || (pass.length()>5&&pass.equals(confPass))){
                    try {
                        dbHandler.editUser(user);
                        errorLabel.setTextFill(Paint.valueOf("00ff73")); //#f51f1f
                        error("?????????????? ???????????? ?????????????? ????????????????!");
                        System.out.println("?????????????? "+user.getLogin() + " ??????????????!");
                        Log log = new Log(new Date(),user.getLogin(), Log.Levels.INFO,"???????????????????????? "+oldLogin + "?????????????? ???????? ??????????????");
                        dbHandler.addLog(log);
                    } catch (SQLException e) {
                        errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                        error("???????????????????????? ?? ?????????? ??????????????  ?????? ??????.!");
                        System.out.println(e.getMessage());
                    }
                    catch (ClassNotFoundException e){
                        System.out.println(e.getMessage());
                    }
                }
            } else {
                try {
                    errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                    error("???????????? ???????????? ???????????? ???????? ???? ?????????? 5 ???????????????? ?? ???? ?????????? 11 ??????????????????!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void error(String text){
        errorLabel.setVisible(true);
        errorLabel.setText(text);
    }
    @Override
    public void setUser(User user) {
        this.user=user;
        nameField.setText(user.getLogin());

    }

    @Override
    public void setPrevScene(Scene scene) {
        this.prevScene = scene;
    }

    @Override
    public void setStage(Stage stage) {

    }

    @Override
    public Stage getStage() {
        return stage;
    }


}
