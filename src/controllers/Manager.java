package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Manager implements Initializable {

    @FXML
    private Button registerTrainer;

    @FXML
    private Button statisticsButton;

    @FXML
    private Button gymHallsButton;

    @FXML
    private Button backButton;

    @FXML
    private Button clientStatisticsButton;

    @FXML
    void onRegisterTrainer() throws IOException {
        registerTrainer.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/AddTrainerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Add Trainer");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onGymHalls() throws IOException {
        gymHallsButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/TrainerStatisticsManagerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Trainer Statistics");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onStatistics() throws IOException {
        statisticsButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/StatisticsManagerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Statistics");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onBack() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/LoginView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Log in");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onClientStatistics() throws IOException {
        clientStatisticsButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/GymStatisticsManagerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("ClientStatistics");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
