package controllers;

import Database.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class BeginWorkoutClient implements Initializable {

    @FXML
    private Label descriptionExerciseLabel;

    @FXML
    private Label nameExerciseLabel;

    @FXML
    private Label durationExerciseLabel;

    @FXML
    private Button backButton;

    @FXML
    private Button beginWorkoutButton;

    @FXML
    private Label failMessage;

    private DBHandler handler;
    private Connection connection;
    private PreparedStatement pst;
    private String currentUserId;
    private String currentUsersGymId;

    @FXML
    void onBackButton() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ClientView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Client");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onBeginWorkout() throws SQLException, IOException {
        connection = handler.getConnection();

        String availabilityEquipment = null;
        String findAvailabilityEquipmentQuery = "SELECT availabilityEquipment FROM equipment INNER JOIN client ON equipment.fk_gymHallID=fk_idGymHall WHERE idClient=?";
        pst = connection.prepareStatement(findAvailabilityEquipmentQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindAvailabilityEquipment = pst.executeQuery();
        while (rsFindAvailabilityEquipment.next()) {
            availabilityEquipment=rsFindAvailabilityEquipment.getString("availabilityEquipment");
            System.out.print(availabilityEquipment);
        }

        String availabilityTrainer = null;
        String findExerciseNameQuery = "SELECT availabilityTrainer FROM trainer INNER JOIN client ON trainer.idTrainer=fk_idTrainerClient WHERE idClient=?";
        pst = connection.prepareStatement(findExerciseNameQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindExerciseName = pst.executeQuery();
        while (rsFindExerciseName.next()) {
            availabilityTrainer=rsFindExerciseName.getString("availabilityTrainer");
        }

        assert availabilityEquipment != null;
        if(availabilityEquipment.equals("0") && Objects.equals(availabilityTrainer, "0")){
            failMessage.setVisible(false);

            String updateEquipmentDataQuery="UPDATE equipment SET availabilityEquipment=1 WHERE fk_gymHallID=?";
            pst = connection.prepareStatement(updateEquipmentDataQuery);
            pst.setString(1, currentUsersGymId);
            pst.executeUpdate();

            //searching for user's trainer
            String idTrainer = null;
            String findIdTrainerQuery = "SELECT idTrainer FROM trainer INNER JOIN client ON trainer.idTrainer=fk_idTrainerClient WHERE idClient=?";
            pst = connection.prepareStatement(findIdTrainerQuery);
            pst.setString(1, currentUserId);
            ResultSet rsFindIdTrainer = pst.executeQuery();
            while (rsFindIdTrainer.next()) {
                idTrainer=rsFindIdTrainer.getString("idTrainer");
            }

            String updateTrainerDataQuery="UPDATE trainer SET availabilityTrainer=1 WHERE idTrainer=?";
            pst = connection.prepareStatement(updateTrainerDataQuery);
            pst.setString(1, idTrainer);
            pst.executeUpdate();

            loadWorkoutInProgressView();
        }
        else{
            failMessage.setVisible(true);
        }
    }

    void loadWorkoutInProgressView() throws IOException {
        beginWorkoutButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/WorkoutInProgressView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Working in Progress");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    void populateLabels() throws SQLException {
        connection = handler.getConnection();

        try {
            File file = new File("current_user.txt");
            Scanner sc = new Scanner(file);
            currentUserId = sc.nextLine();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String findCurrentUsersGymIdQuery = "SELECT fk_idGymHall FROM client WHERE idClient=?";
        pst = connection.prepareStatement(findCurrentUsersGymIdQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindCurrentUsersGymId = pst.executeQuery();
        while (rsFindCurrentUsersGymId.next()) {
            currentUsersGymId = rsFindCurrentUsersGymId.getString("fk_idGymHall");
        }


        //Find current clients's exercise's name
        String findExerciseNameQuery = "SELECT nameExercise FROM exerciseinfo INNER JOIN client ON exerciseinfo.idExercise=fk_idExerciseClient WHERE idClient=?";
        pst = connection.prepareStatement(findExerciseNameQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindExerciseName = pst.executeQuery();
        while (rsFindExerciseName.next()) {
            nameExerciseLabel.setText(rsFindExerciseName.getString("nameExercise"));
        }

        //Find current clients's exercise's duration
        String findExerciseDurationQuery = "SELECT durationExercise FROM exerciseinfo INNER JOIN client ON exerciseinfo.idExercise=fk_idExerciseClient WHERE idClient=?";
        pst = connection.prepareStatement(findExerciseDurationQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindExerciseDuration = pst.executeQuery();
        while (rsFindExerciseDuration.next()) {
            durationExerciseLabel.setText(rsFindExerciseDuration.getString("durationExercise"));
        }

        //Find current clients's exercise's description
        String findExerciseDescriptionQuery = "SELECT textExercise FROM exerciseinfo INNER JOIN client ON exerciseinfo.idExercise=fk_idExerciseClient WHERE idClient=?";
        pst = connection.prepareStatement(findExerciseDescriptionQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindExerciseDescription = pst.executeQuery();
        while (rsFindExerciseDescription.next()) {
            descriptionExerciseLabel.setText(rsFindExerciseDescription.getString("textExercise"));
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        try {
            populateLabels();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
