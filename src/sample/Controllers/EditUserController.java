package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import sample.DBHandler;
import sample.Data.Password;
import sample.Data.User;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;

public class EditUserController implements Controller{
    private User user=null;
    public Scene prevScene;
    @FXML
    private TextField loginEditField1;

    @FXML
    private ChoiceBox<User.UserType> choiceGroupUser;

    @FXML
    private Button editUserBtn;

    @FXML
    private TextField passEditField;

    @FXML
    private Button exitBtn;

    @FXML
    private Label errorLabel;

    @FXML
    private CheckBox isBlockCheckBox;

    @FXML
    void initialize() {
        ObservableList<User.UserType> usersT = FXCollections.observableList(Arrays.stream(User.UserType.values()).toList());
        choiceGroupUser.setItems(usersT);
        exitBtn.setOnAction(event -> closeWindow(exitBtn.getScene()));
        editUserBtn.setOnAction(event -> editUser());
    }
    public void editUser(){
        DBHandler dbHandler = new DBHandler();
        final int minLog = 5;
        final int maxLog = 11;
        String login = loginEditField1.getText();
        if(!login.equals("")) {
            errorLabel.setVisible(false);
            if (login.length() >= minLog && login.length() <= maxLog) {
                String pass = passEditField.getText();
                user.setLogin(login);
                if(!pass.equals("") && pass.length()>5){
                    SecureRandom random = new SecureRandom();
                    byte[] salt = new byte[16];
                    random.nextBytes(salt);
                    user.setPass(Password.hashingPass(pass,salt));
                    user.setSalt(salt);
                }
                else if(!pass.equals("")) {
                    try {
                        errorLabel.setVisible(true);
                        errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                        error("Длинна пароля должна быть не менее 6 символов!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(pass.equals("") || pass.length()>5){
                    user.setGroup(choiceGroupUser.getValue().name());
                    user.setIs_block(isBlockCheckBox.isSelected());
                    try {
                        dbHandler.editUser(user);
                        errorLabel.setTextFill(Paint.valueOf("00ff73")); //#f51f1f
                        error("Учетная запись успешно изменена!");
                        System.out.println("Аккаунт "+user.getLogin() + " изменен!");
                        //TODO ЛОГИРОВАНИЕ
                    } catch (SQLException e) {
                        errorLabel.setTextFill(Paint.valueOf("f51f1f"));
                        error("Пользователь с таким логином  уже сущ.!");
                        System.out.println(e.getMessage());
                    }
                    catch (ClassNotFoundException e){
                        System.out.println(e.getMessage());
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
    }
    public void error(String text){
        errorLabel.setVisible(true);
        errorLabel.setText(text);
    }

    @Override
    public void setUser(User user) {
        this.user=user;
        choiceGroupUser.setValue(User.UserType.User);
        isBlockCheckBox.setSelected(user.getIs_block());
        loginEditField1.setText(user.getLogin());
    }

    @Override
    public void setPrevScene(Scene scene) {
        prevScene=scene;
    }
    @Override
    public void minimizeWindow(Scene scene){}
    @Override
    public void closeWindow(Scene scene) {
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
}
