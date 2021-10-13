package sample.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Data.User;

public class MainController implements Controller{
    public Scene prevScene;
    private double xOffset;
    private double yOffset;
    @Override
    public void minimizeWindow(Scene scene){
        Stage stage = (Stage) scene.getWindow();
        stage.setIconified(true);
    }

    @Override
    public void setUser(User user) {
        this.user=user;
    }

    @Override
    public void setPrevScene(Scene scene) {
        this.prevScene = scene;
    }

    @Override
    public void closeWindow(Scene scene) {
        Stage stage = (Stage) scene.getWindow();
        stage.close();
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

    private Node viewNode = null;
    private Node menuNode = null;
    private User user=null;

    @FXML
    void initialize() {
        exitBtn.setOnAction(event -> closeWindow(exitBtn.getScene()));
        minimizeBtn.setOnAction(event -> minimizeWindow(minimizeBtn.getScene()));
        viewNode = mainPane.getCenter();
        menuNode = mainPane.getLeft();
        securityBtn.setOnAction(event -> changeItem("security.fxml"));
        usersBtn.setOnAction(event -> changeItem("users.fxml"));
        settingsBtn.setOnAction(event -> changeItem("settings.fxml"));

        viewBtn.setOnAction(event -> {
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
            stage.setScene(prevScene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();

        });
    }
    public void changeItem(String fxml){
        try {
            mainPane.setCenter(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/resources/items/"+fxml))));
            mainPane.setLeft(null);
            mainPane.setLeft(menuNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
