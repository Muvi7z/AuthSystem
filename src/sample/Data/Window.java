package sample.Data;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window {
    public void close(Scene scene){
        Stage stage = (Stage) scene.getWindow();
        stage.close();

    }
    public void minimize(Scene scene){
        Stage stage = (Stage) scene.getWindow();
        stage.setIconified(true);
    }
}
