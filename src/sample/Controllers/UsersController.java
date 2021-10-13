package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Const;
import sample.DBHandler;
import sample.Data.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersController{
    public Scene prevScene;

    @FXML
    private Button addBtn;
    @FXML
    private Button editBtn;

    @FXML
    private Button delBtn;
    @FXML
    private TableView<User> tb;
    @FXML
    private TableColumn<User, String> tcID;
    @FXML
    private TableColumn<User, String> tcLogin;
    @FXML
    private TableColumn<User, String> tcPass;
    @FXML
    private TableColumn<User, String> tcGroup;
    @FXML
    private TableColumn<User, String> tcBlock;

    private double xOffset;
    private double yOffset;

    private ObservableList<User> usersList;

    @FXML
    void initialize() {
        tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcLogin.setCellValueFactory(new PropertyValueFactory<>("Login"));
        tcPass.setCellValueFactory(new PropertyValueFactory<>("Pass"));
        tcGroup.setCellValueFactory(new PropertyValueFactory<>("Group"));
        tcBlock.setCellValueFactory(new PropertyValueFactory<>("is_block"));
        tb.setEditable(true);
        UpdateData();
        editBtn.setOnAction(event -> launchNewWindow("items/userEdit.fxml",editBtn.getScene(),tb.getSelectionModel().getSelectedItem()));
        delBtn.setOnAction(event -> deleteUser(tb.getSelectionModel().getSelectedItem()));
        addBtn.setOnAction(event -> launchNewWindow("items/userAdd.fxml",addBtn.getScene(),null));
    }
    public void deleteUser(User user){
        DBHandler dbHandler = new DBHandler();
        try {
            dbHandler.delUser(user);
        }
        catch (SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        UpdateData();
    }
    public void launchNewWindow(String fxml, Scene scene, User user){
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
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UNDECORATED);
        Controller controller = loader.getController();
        controller.setUser(user);
        controller.setPrevScene(scene);
        stage.showAndWait();
        UpdateData();
    }
    public void UpdateData() {
        DBHandler dbHandler = new DBHandler();
        ResultSet result = dbHandler.getAllUsers();
        try {
            usersList = FXCollections.observableArrayList();
            while (result.next()){
                User user = new User(result.getString(Const.USERS_ID), result.getString(Const.USER_LOGIN),result.getString(Const.USER_PASS), User.UserType.valueOf(result.getString(Const.USER_GROUP)),result.getBoolean(Const.USER_BLOCK));
                usersList.add(user);
            }
            tb.setItems(usersList);
        }
        catch (SQLException e ){
            System.out.println(e.getMessage());
        }

    }

}
