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

/*
        VERY IMPORTANT!!!!

        I ACCIDENTALY CHANGED CreateExerciseView.fxml with CreateDietView.fxml

        Sorry
 */

public class Trainer implements Initializable {

    @FXML
    private Button createExerciseButton;

    @FXML
    private Button createDietButton;

    @FXML
    private Button updateProfileButton;

    @FXML
    private Button backButton;

    @FXML
    void onCreateDiet() throws IOException {
        createDietButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/CreateExerciseView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Create a diet");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onUpdateProfile() throws IOException {
        updateProfileButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/UpdateProfileTrainerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Update profile");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onCreateExercise() throws IOException {
        createExerciseButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/CreateDietView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Create an exercise");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onHandleClients() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/HandleClientsView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Handle Clients");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onBack() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/LoginView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Trainer");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
