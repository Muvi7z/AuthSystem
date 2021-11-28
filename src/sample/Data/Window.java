package sample.Data;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Window {
    private double xOffset;
    private double yOffset;
    public void close(Scene scene){
        Stage stage = (Stage) scene.getWindow();
        stage.close();

    }
    public void minimize(Scene scene){
        Stage stage = (Stage) scene.getWindow();
        stage.setIconified(true);
    }
    public void launchNewWindow(String fxml, Scene scene, Double positionX, Double positionY, User user, Scene authScene){
        scene.getWindow().hide();
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
        if(positionX != null){
            stage.setY(positionY);
            stage.setX(positionX);
        }

        stage.setScene(newScene);
        stage.initStyle(StageStyle.UNDECORATED);
        Controller controller = loader.getController();
        controller.setUser(user);
        controller.setPrevScene(authScene);
        controller.setStage(stage);
        stage.show();
    }
}
