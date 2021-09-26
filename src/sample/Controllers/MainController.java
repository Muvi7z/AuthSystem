package sample.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class MainController implements Controller{
    public Scene prevScene;

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

    private Node viewNode = null;
    private Node menuNode = null;

    @FXML
    void initialize() {
        viewNode = mainPane.getCenter();
        menuNode = mainPane.getLeft();
        securityBtn.setOnAction(event -> {
            try {
                mainPane.setCenter(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/resources/items/security.fxml"))));
                mainPane.setLeft(null);
                mainPane.setLeft(menuNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        viewBtn.setOnAction(event -> {
            mainPane.setCenter(viewNode);
            mainPane.setLeft(null);
            mainPane.setLeft(menuNode);
        });
        usersBtn.setOnAction(event -> {
            try {
                mainPane.setCenter(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/resources/items/users.fxml"))));
                mainPane.setLeft(null);
                mainPane.setLeft(menuNode);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}
