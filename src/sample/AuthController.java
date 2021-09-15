package sample;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField loginFild;

    @FXML
    private Button signInBtn;

    @FXML
    private PasswordField passFild;
    @FXML
    void initialize() {
        signInBtn.setOnAction(event -> {

        });
    }
}
