package sample.Controllers;

import javafx.scene.Scene;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
    void initialize() {
    }
}
