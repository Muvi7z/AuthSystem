package sample.Controllers;

import java.awt.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
import sample.Data.Log;
import sample.Data.Password;
import sample.Data.User;

public class AuthController {
    int triedLeft = Settings.Tried_Pass;
    private double xOffset;
    private double yOffset;
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
        triedLeft=Settings.Tried_Pass;
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
        exitBtn.setOnAction(event -> closeWindow(exitBtn.getScene()));
        minimizeBtn.setOnAction(event -> minimizeWindow(minimizeBtn.getScene()));
        signUpBtn.setOnAction(event -> launchNewWindow("regist.fxml", signUpBtn.getScene(),signUpBtn.getScene().getWindow().getX(),signUpBtn.getScene().getWindow().getY(),null));
    }

    private void loginUser(String login, String pass) throws SQLException, ClassNotFoundException {
        DBHandler dbHandler = new DBHandler();
        User user = new User();
        user.setLogin(login);
        ResultSet result = dbHandler.getUser(user);
        if (result.next()){
            user.setPass(result.getString(Const.USER_PASS));
            user.setSalt(result.getBytes(Const.USER_SALT));
            if (Password.hashingPass(pass,user.getSalt()).equals(user.getPass())){
                user.setIs_block(result.getBoolean(Const.USER_BLOCK));
                if(!user.getIs_block()){
                    user.setGroup(result.getString(Const.USER_GROUP));
                    user.setId(result.getString(Const.USERS_ID));
                    Log log = new Log(new Date(),login, Log.Levels.INFO,"Успешный вход пользователя в систему");
                    dbHandler.addLog(log);
                    launchNewWindow("sample.fxml",signInBtn.getScene(),null,null, user);
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
                    if(triedLeft<=0){
                        setTriedLeft("Аккаунт временно заблокирован!!!");
                    }
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
    public void launchNewWindow(String fxml, Scene scene, Double positionX, Double positionY, User user){
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
        stage.getIcons().add(new Image("https://img.icons8.com/doodle/452/iris-scan.png"));
        Scene newScene = new Scene(root);
        newScene.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        newScene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
        if(positionX != null){
            stage.setY(positionY);
            stage.setX(positionX);
        }

        stage.setScene(newScene);
        stage.initStyle(StageStyle.UNDECORATED);
        Controller controller = loader.getController();
        controller.setUser(user);
        controller.setPrevScene(scene);
        stage.show();
    }
    public void minimizeWindow(Scene scene){
        Stage stage = (Stage) scene.getWindow();
        stage.setIconified(true);
    }
    public void closeWindow(Scene scene) {
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
}
