package sample.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            String loginText = loginFild.getText().trim();
            String passText = passFild.getText().trim();

            if(!loginText.equals("") && !passText.equals("")){

            }
        });
        signUpBtn.setOnAction(event -> {
            signUpBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/resources/regist.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            RegistController registController = loader.getController();

            registController.setPrevScene(signUpBtn.getScene());
            stage.show();
        });
    }
}
