package sample.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DBHandler;
import sample.Password;
import sample.User;

public class RegistController {

    public Scene prevScene;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
        signUpCreateBtn.setOnAction(event -> {
            signUpNewUser();
        });
        backBtn.setOnAction(event -> {
            backBtn.getScene().getWindow().hide();
            Stage stage = new Stage();
            stage.setScene(prevScene);

            stage.showAndWait();

        });
    }
    public void error(String text) throws Exception{
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
                        errorLable.setVisible(true);
                        errorLable.setTextFill(Paint.valueOf("00ff73"));
                        error("Учетная запись успешно создана!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Аккаунт "+login + " создан!");
                    //TODO ЛОГИРОВАНИЕ
                }
                else {
                    try {
                        errorLable.setVisible(true);
                        error("Длинна пароля должна быть не менее 6 символов!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {

                try {
                    errorLable.setVisible(true);
                    error("Длинна логина должна быть не менее 5 символов и не более 11 симоволов!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            errorLable.setVisible(true);
            try {
                error("Вы не ввели логин!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setPrevScene(Scene prevScene) {
        this.prevScene = prevScene;
    }
}