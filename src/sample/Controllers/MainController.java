package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Const;
import sample.DBHandler;
import sample.Data.Controller;
import sample.Data.Log;
import sample.Data.User;
import sample.Data.Window;

public class MainController extends Window implements Controller {
    public Scene prevScene;
    private double xOffset;
    private double yOffset;
    @Override
    public void setUser(User user) {
        this.user=user;
    }

    @Override
    public void setPrevScene(Scene scene) {
        this.prevScene = scene;
    }


    @FXML
    private Button minimizeBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private Button viewBtn;

    @FXML
    private Button securityBtn;

    @FXML
    private Button usersBtn;

    @FXML
    private Button settingsBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private BorderPane mainPane;
    @FXML
    private TableView<Log> tb;

    @FXML
    private TableColumn<Log, String> tcID;

    @FXML
    private TableColumn<Log, String> tcName;

    @FXML
    private TableColumn<Log, String> tcUsername;

    @FXML
    private TableColumn<Log, String> tcLevel;

    @FXML
    private TableColumn<Log, String> tcDate;

    private ObservableList<Log> logsList;
    private Node viewNode = null;
    private Node menuNode = null;
    private User user=null;

    @FXML
    void initialize() {
        tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tcLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tb.setEditable(true);
        UpdateData();

        exitBtn.setOnAction(event -> close(exitBtn.getScene()));
        minimizeBtn.setOnAction(event -> minimize(minimizeBtn.getScene()));
        viewNode = mainPane.getCenter();
        menuNode = mainPane.getLeft();
        securityBtn.setOnAction(event -> changeItem("security.fxml"));
        usersBtn.setOnAction(event -> changeItem("users.fxml"));
        settingsBtn.setOnAction(event -> changeItem("settings.fxml"));

        viewBtn.setOnAction(event -> {
            UpdateData();
            mainPane.setCenter(viewNode);
            mainPane.setLeft(null);
            mainPane.setLeft(menuNode);
        });
        logoutBtn.setOnAction(event -> {
            logoutBtn.getScene().getWindow().hide();
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
            DBHandler dbHandler = new DBHandler();
            Log log = new Log(new Date(),this.user.getLogin(), Log.Levels.INFO,"Пользователь вышел из аккаунта");
            try {
                dbHandler.addLog(log);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            stage.setScene(prevScene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();

        });
    }
    public void changeItem(String fxml){
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("/sample/resources/items/"+fxml));
            mainPane.setCenter(loader.load());
            Controller controller = loader.getController();
            controller.setPrevScene(prevScene);
            controller.setUser(user);
            mainPane.setLeft(null);
            mainPane.setLeft(menuNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UpdateData() {
        DBHandler dbHandler = new DBHandler();
        ResultSet result = dbHandler.getAllLogs();
        try {
            logsList = FXCollections.observableArrayList();
            while (result.next()){
                Log log = new Log(result.getString(Const.LOG_ID), result.getString(Const.LOG_DESC),result.getString(Const.LOG_UNAME), Log.Levels.valueOf(result.getString(Const.LOG_LEVEL)),result.getTimestamp(Const.LOG_DATE));
                logsList.add(log);
            }
            tb.setItems(logsList);
        }
        catch (SQLException e ){
            System.out.println(e.getMessage());
        }

    }
}
