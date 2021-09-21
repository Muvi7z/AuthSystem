package sample.Controllers;

import javafx.scene.Scene;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController implements Controller{
    public Scene prevScene;
    @FXML
    private ImageView viewImage;

    @Override
    public void setPrevScene(Scene scene) {
        this.prevScene = scene;
    }

    @FXML
    void initialize() {
        this.viewImage.setImage(new Image("file:../icons/view.png"));
    }
}
