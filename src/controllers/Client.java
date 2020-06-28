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

public class Client implements Initializable {

    @FXML
    private Button chooseGymHallButton;

    @FXML
    private Button giveFeedbackButton;

    @FXML
    private Button chooseTrainerButton;

    @FXML
    private Button backButton;

    @FXML
    private Button viewDietButton;

    @FXML
    private Button beginWorkoutButton;

    @FXML
    void onChooseGymHall() throws IOException {
        chooseGymHallButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/GymHallsClientView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Choose a Gym Hall");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onChooseTrainer() throws IOException {
        chooseTrainerButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ChooseTrainerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Choose a Trainer");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onViewDiet() throws IOException {
        viewDietButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ViewDietClientView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("View your diet");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onBeginWorkout() throws IOException {
        beginWorkoutButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/BeginWorkoutClientView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Begin your workout");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onGiveFeedback() throws IOException {
        giveFeedbackButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/GiveFeedbackView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Give Feedback");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onBack() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/LoginView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Login");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
