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
        tcLogin.setEditable(true);
        tcLogin.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<User, String> event) {
                User user = ((User) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                );
                user.setLogin(event.getNewValue());
                System.out.println(event.getNewValue());
                System.out.println("Логин "+user.getLogin());
            }
        });
        UpdateData();
        addBtn.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/resources/items/userAdd.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.getIcons().add(new Image("https://img.icons8.com/doodle/452/iris-scan.png"));
            Scene newScene = new Scene(root);
            newScene.setOnMousePressed(eventD -> {
                xOffset = stage.getX() - eventD.getScreenX();
                yOffset = stage.getY() - eventD.getScreenY();
            });
            newScene.setOnMouseDragged(eventD -> {
                stage.setX(eventD.getScreenX() + xOffset);
                stage.setY(eventD.getScreenY() + yOffset);
            });

            stage.setScene(newScene);
            stage.initStyle(StageStyle.UNDECORATED);
            Controller controller = loader.getController();

            controller.setPrevScene(addBtn.getScene());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            UpdateData();
        });
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
