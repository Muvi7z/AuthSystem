package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import sample.DBHandler;
import sample.Data.*;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

public class AddUserController extends Window implements Controller  {
    public Scene prevScene;
    public Stage stage;

    @FXML
    private TextField passCreateField;

    @FXML
    private Button addUserBtn;

    @FXML
    private TextField loginCreateField1;

    @FXML
    private ChoiceBox<User.UserType> choiceGroupUser;

    @FXML
    private Button exitBtn;

    @FXML
    private Label errorLabel;
    private User user=null;
    @FXML
    void initialize() {

        ObservableList<User.UserType> usersT = FXCollections.observableList(Arrays.stream(User.UserType.values()).toList());
        choiceGroupUser.setItems(usersT);
        choiceGroupUser.setValue(User.UserType.User);
        addUserBtn.setOnAction(event -> {
            createNewUser(loginCreateField1.getText(),choiceGroupUser.getValue());
        });
        exitBtn.setOnAction(event -> close(exitBtn.getScene()));
    }
    public void createNewUser(String login, User.UserType type){
        DBHandler dbHandler = new DBHandler();
        final int minLog = 5;
        final int maxLog = 11;
        if(!login.equals("")) {
            errorLabel.setVisible(false);
            if (login.length() >= minLog && login.length() <= maxLog) {
                String pass = Password.generatePassword(login.length(), 10);
                SecureRandom random = new SecureRandom();

                byte[] salt = new byte[16];
                random.nextBytes(salt);
                User user = new User(login,Password.hashingPass(pass,salt),type,salt);
                try {
                    dbHandler.addUser(user);
                    errorLabel.setTextFill(Paint.valueOf("00ff73")); //#f51f1f
                    error("Учетная запись успешно создана!");
                    Log log = new Log(new Date(),this.user.getLogin(), Log.Levels.INFO,"Администратор создал аккаунт "+login);
                    dbHandler.addLog(log);
                    passCreateField.setText(pass);
                    System.out.println("Аккаунт "+login + " создан!");
                } catch (SQLException e) {
                    errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                    error("Пользователь с таким логином  уже создан!");
                    System.out.println(e.getMessage());
                }
                catch (ClassNotFoundException e){
                    System.out.println(e.getMessage());
                }

            } else {
                try {
                    errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                    error("Длинна логина должна быть не менее 5 символов и не более 11 симоволов!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                error("Вы не ввели логин!");
            } catch (Exception e) {
                e.printStackTrace();
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
    }

    @Override
    public void setPrevScene(Scene scene) {
        prevScene=scene;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage=stage;
        stage.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(MainController.thread!=null)
                    MainController.thread.setNoActiveDelay(0);
            }
        });
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

}
