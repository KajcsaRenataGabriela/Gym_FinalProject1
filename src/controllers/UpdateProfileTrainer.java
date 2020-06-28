package controllers;

import Database.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class UpdateProfileTrainer implements Initializable {

    @FXML
    private Label linkLabel;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label pictureLabel;

    @FXML
    private Button backButton;

    @FXML
    private TextField linkTextField;

    @FXML
    private TextField pictureTextField;

    @FXML
    private Label passwordLabel;

    private DBHandler handler;
    private Connection connection;
    private PreparedStatement pst;
    private String currentUserId;

    @FXML
    void onSaveLink() throws SQLException {

        connection = handler.getConnection();

        getCurrentUserId();

        String insertDataQuery = "UPDATE trainer SET linkTrainer=? WHERE idTrainer=?";
        pst = connection.prepareStatement(insertDataQuery);
        pst.setString(1, linkTextField.getText());
        pst.setString(2, currentUserId);
        pst.executeUpdate();

        linkLabel.setVisible(true);
    }

    @FXML
    void onSavePicture() throws SQLException {
        getCurrentUserId();

        connection = handler.getConnection();

        String insertDataQuery = "UPDATE trainer SET pictureTrainer=? WHERE idTrainer=?";
        pst = connection.prepareStatement(insertDataQuery);
        pst.setString(1, pictureTextField.getText());
        pst.setString(2, currentUserId);
        pst.executeUpdate();

        pictureLabel.setVisible(true);
    }

    @FXML
    void onUpdatePassword() throws SQLException {

        getCurrentUserId();

        connection = handler.getConnection();

        String insertDataQuery = "UPDATE trainer SET passwordTrainer=? WHERE idTrainer=?";
        pst = connection.prepareStatement(insertDataQuery);
        pst.setString(1, passwordTextField.getText());
        pst.setString(2, currentUserId);
        pst.executeUpdate();

        passwordLabel.setVisible(true);

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

    private void getCurrentUserId() {
        connection = handler.getConnection();

        try {
            File file = new File("current_user.txt");
            Scanner sc = new Scanner(file);
            currentUserId = sc.nextLine();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
    }
}
