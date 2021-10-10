package sample.Controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Data.User;

public interface Controller {
    public void setUser(User user);
    public void setPrevScene(Scene scene);
    public void closeWindow(Scene scene);
    public void minimizeWindow(Scene scene);
}
