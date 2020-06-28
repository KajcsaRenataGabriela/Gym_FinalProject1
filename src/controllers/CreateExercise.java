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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CreateExercise implements Initializable {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField durationTextField;

    @FXML
    private Button backButton;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label successMessage;

    @FXML
    private Label failMessage;

    @FXML
    private ChoiceBox<Object> chooseEquipment;

    private DBHandler handler;
    private Connection connection;
    private PreparedStatement pst;
    private String currentUserId = null;

    @FXML
    void onCreate() throws SQLException {
        if(nameTextField.getText().isEmpty() || descriptionTextArea.getText().isEmpty() || durationTextField.getText().isEmpty())
        {
            failMessage.setVisible(true);

            nameTextField.setText("");
            descriptionTextArea.setText("");
        }
        else {
            failMessage.setVisible(false);

            connection = handler.getConnection();

            try {
                File file = new File("current_user.txt");
                Scanner sc = new Scanner(file);
                currentUserId = sc.nextLine();
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String currentTrainersGymId =null;
            String findUsersGymQuery = "SELECT fk_idGymHallTrainer FROM trainer WHERE idTrainer=?";
            pst = connection.prepareStatement(findUsersGymQuery);
            pst.setString(1, currentUserId);
            ResultSet rsFindUsersTrainer = pst.executeQuery();
            while (rsFindUsersTrainer.next()) {
                currentTrainersGymId=rsFindUsersTrainer.getString("fk_idGymHallTrainer");
            }

            String sql="INSERT INTO exerciseinfo(nameExercise,textExercise,durationExercise,fk_idGymHallExercise,fk_idTrainerExercise)"+"VALUES(?,?,?,?,?)";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, nameTextField.getText());
            ps.setString(2, descriptionTextArea.getText());
            ps.setString(3, durationTextField.getText());
            ps.setString(4, currentTrainersGymId);
            ps.setString(5, currentUserId);
            ps.executeUpdate();

            successMessage.setVisible(true);
        }
    }

    @FXML
    void onBack() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/TrainerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Trainer");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    public String getGymHall() {
        return (String) chooseEquipment.getSelectionModel().getSelectedItem();
    }

    private void getCurrentUser(){
        try {
            File file = new File("current_user.txt");
            Scanner sc = new Scanner(file);
            currentUserId = sc.nextLine();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void populateChoiceBox() throws SQLException {
        connection = handler.getConnection();

        getCurrentUser();

        // Find current user ID
        String currentTrainersGymId =null;
        String findUsersGymQuery = "SELECT fk_idGymHallTrainer FROM trainer WHERE idTrainer=?";
        pst = connection.prepareStatement(findUsersGymQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindUsersTrainer = pst.executeQuery();
        while (rsFindUsersTrainer.next()) {
            currentTrainersGymId=rsFindUsersTrainer.getString("fk_idGymHallTrainer");
        }

        String equipmentsForChoiceBoxQuery = "SELECT nameEquipment FROM equipment WHERE fk_gymHallID=?";
        try {
            pst = connection.prepareStatement(equipmentsForChoiceBoxQuery);
            pst.setString(1, currentTrainersGymId);
            ResultSet rsEquipment = pst.executeQuery();
            while (rsEquipment.next()) {
                objectListEquipments.add(rsEquipment.getString("nameEquipment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        chooseEquipment.setItems(FXCollections.observableArrayList(objectListEquipments));
    }

    ObservableList<String> objectListEquipments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        try {
            populateChoiceBox();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
