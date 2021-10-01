package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Const;
import sample.DBHandler;
import sample.Data.User;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersController {


    @FXML
    private Button addBtn;

    @FXML
    private TableView<User> tb;
    @FXML
    private TableColumn<User, String> tcID;
    @FXML
    private TableColumn<User, String> tcLogin;
    @FXML
    private TableColumn<User, String> tcPass;
    @FXML
    private TableColumn<User, String> tcGroup;
    @FXML
    private TableColumn<User, String> tcBlock;



    private ObservableList<User> usersList;

    @FXML
    void initialize() {
        tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcLogin.setCellValueFactory(new PropertyValueFactory<>("Login"));
        tcPass.setCellValueFactory(new PropertyValueFactory<>("Pass"));
        tcGroup.setCellValueFactory(new PropertyValueFactory<>("Group"));
        tcBlock.setCellValueFactory(new PropertyValueFactory<>("is_block"));
        tcLogin.setEditable(true);
        UpdateData();

        addBtn.setOnAction(event -> {
            User user = tb.getSelectionModel().getSelectedItem();
            System.out.println(user.getLogin());
        });
    }

    private void UpdateData() {
        DBHandler dbHandler = new DBHandler();
        ResultSet result = dbHandler.getAllUsers();
        try {
            usersList = FXCollections.observableArrayList();
            while (result.next()){
                User user = new User(result.getString(Const.USERS_ID), result.getString(Const.USER_LOGIN),result.getString(Const.USER_PASS), User.UserType.valueOf(result.getString(Const.USER_GROUP)),result.getBoolean(Const.USER_BLOCK));
                usersList.add(user);
            }
            tb.setItems(usersList);
        }
        catch (SQLException e ){
            System.out.println(e.getMessage());
        }

    }
}
