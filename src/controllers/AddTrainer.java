package controllers;

import Database.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddTrainer implements Initializable {

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button backButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField telephoneTextField;

    @FXML
    private Label lblMessage;

    @FXML
    private Label lblMessageSuccess;

    @FXML
    private ChoiceBox<Object> chooseTrainerGymHall;

    @FXML
    private Label userAlreadyExistsLabel;

    private DBHandler handler;
    private Connection connection;
    private PreparedStatement pst;

    void clearAllFields()
    {
        usernameTextField.setText("");
        passwordTextField.setText("");
        chooseTrainerGymHall.setValue(null);
        telephoneTextField.setText("");
    }

    private boolean userAlreadyExists(String usernameTrainerTextField) throws SQLException {
        connection = handler.getConnection();

        String verifyIfUserExistsQuery= "SELECT userTrainer FROM trainer WHERE userTrainer=?";
        pst = connection.prepareStatement(verifyIfUserExistsQuery);
        pst.setString(1, usernameTrainerTextField);
        ResultSet rsFindTrainerIdQuery = pst.executeQuery();
        try {
            if(rsFindTrainerIdQuery.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void clearFields(){
        usernameTextField.setText("");
        passwordTextField.setText("");
        telephoneTextField.setText("");
        chooseTrainerGymHall.setValue(null);
    }

    @FXML
    void onAddNewTrainer() throws SQLException{
        if(usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() || chooseTrainerGymHall.getValue()==null ||telephoneTextField.getText().isEmpty())
        {
            userAlreadyExistsLabel.setVisible(false);
            lblMessageSuccess.setVisible(false);
            lblMessage.setVisible(true);
            clearAllFields();
        }
        else {
            connection = handler.getConnection();
            //Here I search the gym's ID

            String gymId = null;
            String findIdTrainerQuery = "SELECT idGymHall FROM gymhall WHERE typeGymHall=?";
            pst = connection.prepareStatement(findIdTrainerQuery);
            pst.setString(1, String.valueOf(getGymHall()));
            ResultSet rsFindTrainerIdQuery = pst.executeQuery();
            try {
                while (rsFindTrainerIdQuery.next()) {
                    gymId = rsFindTrainerIdQuery.getString("idGymHall");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Here I register the new trainer
            if(userAlreadyExists(usernameTextField.getText())) {
                lblMessageSuccess.setVisible(false);
                lblMessage.setVisible(false);
                userAlreadyExistsLabel.setVisible(true);
                clearFields();
            }
            else{

                String sql="INSERT INTO trainer(userTrainer,passwordTrainer,fk_idGymHallTrainer,telTrainer)"+"VALUES(?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, usernameTextField.getText());
                ps.setString(2, passwordTextField.getText());
                ps.setString(3, gymId);
                ps.setString(4, telephoneTextField.getText());
                ps.executeUpdate();

                userAlreadyExistsLabel.setVisible(false);
                lblMessage.setVisible(false);
                lblMessageSuccess.setVisible(true);
                clearAllFields();
            }

        }
    }

    @FXML
    void onBack() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ManagerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Manager " + usernameTextField.getText());
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    public String getGymHall() {
        return (String) chooseTrainerGymHall.getSelectionModel().getSelectedItem();
    }

    ObservableList<String> objectList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();

        connection = handler.getConnection();
        String sql = "SELECT typeGymHall FROM gym_db.gymhall";
        try {
            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                objectList.add(rs.getString("typeGymHall"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        chooseTrainerGymHall.setItems(FXCollections.observableArrayList(objectList));
    }
}
