package sample.Controllers;

import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.DBHandler;
import sample.Data.Password;
import sample.Data.User;

import java.sql.SQLException;

public class RegistController implements Controller{

    public Scene prevScene;


    @FXML
    private TextField singUpPassFild1;

    @FXML
    private Button signUpCreateBtn;

    @FXML
    private TextField singUpLoginFild;
    @FXML
    private Label errorLable;
    @FXML
    private Button backBtn;

    @FXML
    void initialize() {
        signUpCreateBtn.setOnAction(event -> signUpNewUser());
        backBtn.setOnAction(event -> {
            backBtn.getScene().getWindow().hide();
            Stage stage = new Stage();
            stage.setScene(prevScene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();

        });
    }
    public void error(String text){
        errorLable.setVisible(true);
        errorLable.setText(text);
    }
    private void signUpNewUser(){
        DBHandler dbHandler = new DBHandler();
        final int minLog = 5;
        final int maxLog = 11;
        String login = singUpLoginFild.getText().trim();
        User.UserType group = User.UserType.User;

        if(!login.equals("")) {
            if (login.length() >= minLog && login.length() <= maxLog) {
                errorLable.setVisible(false);
                String pass = singUpPassFild1.getText().trim();
                if(!pass.equals("") && pass.length()>5){
                    User user = new User(login,Password.hashingPass(pass),group);
                    try {
                        dbHandler.addUser(user);
                        errorLable.setTextFill(Paint.valueOf("00ff73")); //#f51f1f
                        error("Учетная запись успешно создана!");
                        System.out.println("Аккаунт "+login + " создан!");
                        //TODO ЛОГИРОВАНИЕ
                    } catch (SQLException e) {
                        errorLable.setTextFill(Paint.valueOf("f51f1f"));
                        error("Пользователь с таким логином  уже создан!");
                        System.out.println(e.getMessage());
                    }
                    catch (ClassNotFoundException e){
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    try {
                        errorLable.setTextFill(Paint.valueOf("f51f1f"));
                        error("Длинна пароля должна быть не менее 6 символов!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {

                try {
                    errorLable.setTextFill(Paint.valueOf("f51f1f"));
                    error("Длинна логина должна быть не менее 5 символов и не более 11 симоволов!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            try {
                errorLable.setTextFill(Paint.valueOf("f51f1f"));
                error("Вы не ввели логин!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void setPrevScene(Scene scene) {
        this.prevScene = scene;
    }
}