package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import sample.DBHandler;
import sample.Data.*;
import sample.Settings;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class EditUserController extends Window implements Controller {
    private User user=null;
    private User selectUser=null;
    public Scene prevScene;
    public Stage stage;
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
        exitBtn.setOnAction(event -> close(exitBtn.getScene()));
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
                String oldLogin = selectUser.getLogin();
                selectUser.setLogin(login);


                if(!pass.equals("") && pass.length()>5){
                    byte[] salt = new byte[16];
                    SecureRandom random = new SecureRandom();
                    random.nextBytes(salt);
                    selectUser.setSalt(salt);
                    selectUser.setPass(Password.hashingPass(pass,salt));
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
                    selectUser.setGroup(choiceGroupUser.getValue().name());
                    selectUser.setIsBlock(isBlockCheckBox.isSelected());
                    try {
                        dbHandler.editUser(selectUser);
                        if (isBlockCheckBox.isSelected()){
                            Date date = new Date(new Date().getTime()+(Settings.TimeBlock*60000L));
                            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                            dbHandler.setBlockUser(selectUser,true,formatForDateNow.format(date));
                            Log log = new Log(new Date(),user.getLogin(), Log.Levels.INFO,"Администратор заблокировал аккаунт "+selectUser.getLogin()+
                                    " до "+ date);
                            dbHandler.addLog(log);
                        }
                        else {
                            dbHandler.setBlockUser(selectUser,false,null);
                            Log log = new Log(new Date(),user.getLogin(), Log.Levels.INFO,"Администратор разблокировал аккаунт "+selectUser.getLogin());
                            dbHandler.addLog(log);
                        }
                        errorLabel.setTextFill(Paint.valueOf("00ff73")); //#f51f1f
                        error("Учетная запись успешно изменена!");
                        System.out.println("Аккаунт "+selectUser.getLogin() + " изменен!");
                        error("Учетная запись успешно создана!");
                        Log log = new Log(new Date(),user.getLogin(), Log.Levels.INFO,"Администратор изменил аккаунт "+oldLogin);
                        dbHandler.addLog(log);
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

    }

    public void setSelectUser(User selectUser) {
        this.selectUser = selectUser;
        choiceGroupUser.setValue(User.UserType.User);
        isBlockCheckBox.setSelected(selectUser.getIsBlock());
        loginEditField1.setText(selectUser.getLogin());
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
        return stage;
    }

}
