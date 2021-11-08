package sample.Data;

import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Data.User;

public interface Controller {
    public Stage stage = null;
    public Scene scene = null;
    public User user = null;
    public void setUser(User user);
    public void setPrevScene(Scene scene);
    public void setStage(Stage stage);
    public Stage getStage();

}
