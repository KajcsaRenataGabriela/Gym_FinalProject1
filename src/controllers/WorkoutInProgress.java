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
import java.util.ResourceBundle;
import java.util.Scanner;

public class WorkoutInProgress implements Initializable {

    @FXML
    private Label descriptionDietLabel;

    @FXML
    private Label nameDietLabel;

    @FXML
    private Label durationDietLabel;

    @FXML
    private Button endWorkoutButton;

    private DBHandler handler;
    private Connection connection;
    private PreparedStatement pst;
    private String currentUserId;
    private int durationExercise;

    @FXML
    void onEndWorkout() throws SQLException, IOException {

        connection = handler.getConnection();

       // searching for user's trainer
        String idTrainer = null;
        String findIdTrainerQuery = "SELECT idTrainer FROM trainer INNER JOIN client ON trainer.idTrainer=fk_idTrainerClient WHERE idClient=?";
        pst = connection.prepareStatement(findIdTrainerQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindIdTrainer = pst.executeQuery();
        while (rsFindIdTrainer.next()) {
            idTrainer=rsFindIdTrainer.getString("idTrainer");
        }

        String idEquipment = null;
        String findIdEquipmentQuery = "SELECT idEquipment FROM equipment INNER JOIN client ON equipment.fk_gymHallID=fk_idGymHall WHERE idClient=?";
        pst = connection.prepareStatement(findIdEquipmentQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindIdEquipment = pst.executeQuery();
        while (rsFindIdEquipment.next()) {
            idEquipment=rsFindIdEquipment.getString("idEquipment");
        }


        String updateEquipmentDataQuery = "UPDATE equipment SET availabilityEquipment=0 WHERE idEquipment=?";
        pst = connection.prepareStatement(updateEquipmentDataQuery);
        pst.setString(1, idEquipment);
        pst.executeUpdate();

        String updateDataQuery = "UPDATE trainer SET availabilityTrainer=0 WHERE idTrainer=?";
        pst = connection.prepareStatement(updateDataQuery);
        pst.setString(1, idTrainer);
        pst.executeUpdate();

        //getting trainer's total working hours
        int currentWorkingHours = 0;
        String findCurrentWorkingHoursQuery = "SELECT workingHoursTrainer FROM trainer INNER JOIN client ON trainer.idTrainer=fk_idTrainerClient WHERE idClient=?";
        pst = connection.prepareStatement(findCurrentWorkingHoursQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindCurrentWorkingHoursQuery = pst.executeQuery();
        while (rsFindCurrentWorkingHoursQuery.next()) {
            currentWorkingHours=Integer.parseInt(rsFindCurrentWorkingHoursQuery.getString("workingHoursTrainer"));
        }

        currentWorkingHours+=durationExercise;

        String updateDataTrainerQuery = "UPDATE trainer SET workingHoursTrainer=? WHERE idTrainer=?";
        pst = connection.prepareStatement(updateDataTrainerQuery);
        pst.setString(1, String.valueOf(currentWorkingHours));
        pst.setString(2, idTrainer);
        pst.executeUpdate();

        // go back to client menu
        endWorkoutButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ClientView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Client");
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

        //Find current clients's exercise's name
        String findExerciseNameQuery = "SELECT nameExercise FROM exerciseinfo INNER JOIN client ON exerciseinfo.idExercise=fk_idExerciseClient WHERE idClient=?";
        pst = connection.prepareStatement(findExerciseNameQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindExerciseName = pst.executeQuery();
        while (rsFindExerciseName.next()) {
            nameDietLabel.setText(rsFindExerciseName.getString("nameExercise"));
        }

        //Find current clients's exercise's duration
        String findExerciseDurationQuery = "SELECT durationExercise FROM exerciseinfo INNER JOIN client ON exerciseinfo.idExercise=fk_idExerciseClient WHERE idClient=?";
        pst = connection.prepareStatement(findExerciseDurationQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindExerciseDuration = pst.executeQuery();
        while (rsFindExerciseDuration.next()) {
            durationDietLabel.setText(rsFindExerciseDuration.getString("durationExercise"));
            durationExercise = Integer.parseInt(rsFindExerciseDuration.getString("durationExercise"));
        }

        //Find current clients's exercise's description
        String findExerciseDescriptionQuery = "SELECT textExercise FROM exerciseinfo INNER JOIN client ON exerciseinfo.idExercise=fk_idExerciseClient WHERE idClient=?";
        pst = connection.prepareStatement(findExerciseDescriptionQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindExerciseDescription = pst.executeQuery();
        while (rsFindExerciseDescription.next()) {
            descriptionDietLabel.setText(rsFindExerciseDescription.getString("textExercise"));
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
