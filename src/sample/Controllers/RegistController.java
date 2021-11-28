package sample.Controllers;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.DBHandler;
import sample.Data.*;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Date;

public class RegistController extends Window implements Controller {

    public Scene prevScene;
    private double xOffset;
    private double yOffset;

    @FXML
    private Button minimizeBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private TextField singUpPassFild1;

    @FXML
    private Button signUpCreateBtn;

    @FXML
    private TextField singUpLoginFild;
    @FXML
    private Label errorLabel;
    @FXML
    private Button backBtn;

    @FXML
    void initialize() {
        exitBtn.setOnAction(event -> close(exitBtn.getScene()));
        minimizeBtn.setOnAction(event -> minimize(minimizeBtn.getScene()));
        signUpCreateBtn.setOnAction(event -> signUpNewUser(singUpLoginFild.getText().trim(),singUpPassFild1.getText().trim()));
        backBtn.setOnAction(event -> {
            backBtn.getScene().getWindow().hide();
            Stage stage = new Stage();
            stage.getIcons().add(new Image("https://img.icons8.com/doodle/452/iris-scan.png"));
            prevScene.setOnMousePressed(event1 -> {
                xOffset = stage.getX() - event1.getScreenX();
                yOffset = stage.getY() - event1.getScreenY();
            });
            prevScene.setOnMouseDragged(event12 -> {
                stage.setX(event12.getScreenX() + xOffset);
                stage.setY(event12.getScreenY() + yOffset);
            });
            stage.setY(backBtn.getScene().getWindow().getY());
            stage.setX(backBtn.getScene().getWindow().getX());
            stage.setScene(prevScene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();

        });
    }
    public void error(String text){
        errorLabel.setVisible(true);
        errorLabel.setText(text);
    }
    private void signUpNewUser(String login,String pass){
        DBHandler dbHandler = new DBHandler();
        final int minLog = 5;
        final int maxLog = 11;
        User.UserType group = User.UserType.User;

        if(!login.equals("")) {
            if (login.length() >= minLog && login.length() <= maxLog) {
                errorLabel.setVisible(false);
                if(pass.length() > 5){
                    SecureRandom random = new SecureRandom();

                    byte[] salt = new byte[16];
                    random.nextBytes(salt);
                    User user = new User(login,Password.hashingPass(pass,salt),group,salt);
                    try {
                        dbHandler.addUser(user);
                        errorLabel.setTextFill(Paint.valueOf("00ff73")); //#f51f1f
                        error("Учетная запись успешно создана!");
                        Log log = new Log(new Date(),"", Log.Levels.INFO,"Создан аккаунт "+login);
                        dbHandler.addLog(log);
                        log = new Log(new Date(), login, Log.Levels.INFO, "Успешный вход пользователя в систему");
                        dbHandler.addLog(log);
                        launchNewWindow("sample.fxml", signUpCreateBtn.getScene(), null, null, user,prevScene);
                        System.out.println("Аккаунт "+login + " создан!");
                    } catch (SQLException e) {
                        errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                        error("Пользователь с таким логином  уже создан!");
                        System.out.println(e.getMessage());
                    }
                    catch (ClassNotFoundException e){
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    try {
                        errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                        error("Длинна пароля должна быть не менее 6 символов!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {

                try {
                    errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                    error("Длинна логина должна быть не менее 5 символов и не более 11 симоволов!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            try {
                errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                error("Вы не ввели логин!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setUser(User user) {}

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