package sample.Controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.*;
import sample.Data.Password;
import sample.Data.User;

public class AuthController {
    int triedLeft = Settings.Tried_Pass;
    @FXML
    private Label triedLeftLable;

    @FXML
    private Label errorLable;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField loginFild;

    @FXML
    private Button signInBtn;

    @FXML
    private PasswordField passFild;
    @FXML
    void initialize() {
        signInBtn.setOnAction(event -> {
            String login =loginFild.getText().trim();
            String pass = passFild.getText().trim();
            if(!login.equals("")){
                if(!pass.equals("")){
                    try {
                        loginUser(login,pass);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
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
        signUpBtn.setOnAction(event -> {
            launchNewWindow("regist.fxml", signUpBtn.getScene());
        });
    }

    private void loginUser(String login, String pass) throws SQLException {
        DBHandler dbHandler = new DBHandler();
        User user = new User();
        user.setLogin(login);
        ResultSet result = dbHandler.getUser(user);
        int counter = 0;
        if (result.next()){
            user.setPass(result.getString(Const.USER_PASS));
            if (Password.hashingPass(pass).equals(user.getPass())){
                user.setIs_block(result.getBoolean(Const.USER_BLOCK));
                if(!user.getIs_block()){
                    user.setGroup(result.getString(Const.USER_GROUP));
                    launchNewWindow("sample.fxml",signInBtn.getScene());
                }
                else {
                    try {
                        error("Пользователь с таким логином заблокирован!",Paint.valueOf("f51f1f"));
                        setTriedLeft("Разблокировка через:"+ "1 век");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else {
                try {
                    error("Введне неверный пароль!",Paint.valueOf("f51f1f"));
                    triedLeft--;
                    setTriedLeft("Осталось попыток: "+ triedLeft);
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
        errorLable.setVisible(true);
        errorLable.setTextFill(paint);
        errorLable.setText(text);
    }
    public void setTriedLeft(String text){
        triedLeftLable.setVisible(true);
        triedLeftLable.setText(text);
    }
    public void launchNewWindow(String fxml, Scene scene){
        scene.getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/resources/"+fxml));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        Controller controller = loader.getController();

        controller.setPrevScene(scene);
        stage.show();
    }
}
