package controllers;

import Database.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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

public class CreateDiet implements Initializable {

    @FXML
    private Label failMessage;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button backButton;

    @FXML
    private Label successMessage;

    private DBHandler handler;
    private String currentUserId;

    @FXML
    void onCreate() throws SQLException {
        if(nameTextField.getText().isEmpty() || descriptionTextArea.getText().isEmpty())
        {
            failMessage.setVisible(true);

            nameTextField.setText("");
            descriptionTextArea.setText("");
        }
        else {
            failMessage.setVisible(false);
            try {
                File file = new File("current_user.txt");
                Scanner sc = new Scanner(file);
                currentUserId = sc.nextLine();
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Connection connection = handler.getConnection();
            String sql="INSERT INTO diet(nameDiet,textDiet,fk_idTrainerDiet)"+"VALUES(?,?,?)";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, nameTextField.getText());
            ps.setString(2, descriptionTextArea.getText());
            ps.setString(3, currentUserId);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
    }
}
