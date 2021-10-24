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
        SpinnerValueFactory<Long> factory = new SpinnerValueFactory<>() {
            @Override
            public void decrement(int i) {
                if (this.getValue() > 0)
                    this.setValue(this.getValue() - i);

            }

            @Override
            public void increment(int i) {
                this.setValue(this.getValue() + i);
            }

        };
        SpinnerValueFactory<Long> timeOutFactory = new SpinnerValueFactory<>() {
            @Override
            public void decrement(int i) {
                if (this.getValue() > 0)
                    this.setValue(this.getValue() - i);

            }

            @Override
            public void increment(int i) {
                this.setValue(this.getValue() + i);
            }

        };
        factory.setValue(1L);
        timeOutFactory.setValue(1L);
        timeBlockSpinner.setValueFactory(factory);
        timeOutSpinner.setValueFactory(timeOutFactory);
        timeBlockSpinner.getEditor().setOnAction(actionEvent -> {
            String text = timeBlockSpinner.getEditor().getText();
            Long newV = factory.getValue();
            try{
                newV = Long.parseLong(text);
            }
            catch (Exception e){
                timeBlockLabel.setVisible(true);
                timeBlockLabel.setText("Неверный ввод!");
            }
            factory.setValue(newV);
        });
        timeOutSpinner.getEditor().setOnAction(actionEvent -> {
            String text = timeOutSpinner.getEditor().getText();
            Long newV = timeOutFactory.getValue();
            try{
                newV = Long.parseLong(text);
            }
            catch (Exception e){
                timeOutLabel.setVisible(true);
                timeOutLabel.setText("Неверный ввод!");
            }
            timeOutFactory.setValue(newV);
        });
        timeBlockSpinner.valueProperty().addListener((observableValue, old, newV) -> {
            int days = (int) (newV/1440);
            int hour = (int) ((newV%1440)/60);
            String text = "";
            if (days>0){
                text = "Дней: "+days+" ";
            }
            if(hour>0){
                text += "Часов: "+hour+" ";
            }
            text+="Минут: "+(newV%60);
            timeBlockLabel.setVisible(true);
            timeBlockLabel.setText(text);
        });
        timeBlockSpinner.valueProperty().addListener((observableValue, old, newV) -> {
            String text = "";
            text+="Минут: "+newV;
            timeOutLabel.setVisible(true);
            timeOutLabel.setText(text);
        });
        saveBtn.setOnAction(event -> editSecurity(triedField1.getText(), timeBlockSpinner.getValue(),timeOutSpinner.getValue()));
    }
    public void editSecurity(String kol, long timeBlock, long timeOut){
        System.out.println(timeBlock);
        System.out.println(timeOut);
        System.out.println(kol);
    }
}
