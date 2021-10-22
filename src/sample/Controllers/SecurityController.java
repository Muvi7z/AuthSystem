package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import sample.Data.Controller;
import sample.Data.User;

public class SecurityController implements Controller {
    private User user= null;

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setPrevScene(Scene scene) {

    }
    @FXML
    private Button saveBtn;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField triedField1;

    @FXML
    private Spinner<Long> timeBlockSpinner;

    @FXML
    private Spinner<Long> timeOutSpinner;

    @FXML
    private Label timeBlockLabel;

    @FXML
    private Label timeOutLabel;

    @FXML
    void initialize() {
        SpinnerValueFactory<Long> factory = new SpinnerValueFactory<Long>() {
            @Override
            public void decrement(int i) {
                this.setValue(this.getValue()-i);
            }

            @Override
            public void increment(int i) {
                this.setValue(this.getValue()+i);
            }
        };
        factory.setValue(1L);
        timeBlockSpinner.setValueFactory(factory);
        //saveBtn.setOnAction(event -> editSecurity(triedField1.getText(), timeBockSpinner.getValue(),timeOutSpinner.getValue()));
    }
    public void editSecurity(String kol, long timeBlock, long timeOut){

    }
}
