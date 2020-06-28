package controllers;

import Database.DBHandler;
import javafx.collections.FXCollections;
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
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.Scanner;

public class GiveFeedback implements Initializable {

    @FXML
    private TextArea feedbackTextField;

    @FXML
    private Button backButton;

    @FXML
    private ChoiceBox<Object> ratingChoiceBox;

    @FXML
    private Label failLabel;

    @FXML
    private Label failTrainerLabel;

    @FXML
    private Label successLabel;

    private DBHandler handler;
    private Connection connection;
    private PreparedStatement pst;
    private String currentUserId;

    @FXML
    void onBack() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ClientView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Client");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    private String getCurrentUserId(){
        try {
            File file = new File("current_user.txt");
            Scanner sc = new Scanner(file);
            currentUserId = sc.nextLine();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return currentUserId;
    }

    private boolean currentUserHasATrainer() throws SQLException {
        connection = handler.getConnection();
        String currentUsersTrainerId = null;
        String findUsersTrainerQuery = "SELECT fk_idTrainerClient FROM client WHERE idClient=?";
        pst = connection.prepareStatement(findUsersTrainerQuery);
        pst.setString(1, getCurrentUserId());
        ResultSet rsFindUsersTrainer = pst.executeQuery();
        while (rsFindUsersTrainer.next()) {
            currentUsersTrainerId = rsFindUsersTrainer.getString("fk_idTrainerClient");
        }
        return currentUsersTrainerId != null;
    }

    @FXML
    void onSubmit() throws SQLException {
        if(currentUserHasATrainer()) {
            if (ratingChoiceBox.getValue() == null || feedbackTextField.getText().trim().isEmpty()) {
                successLabel.setVisible(false);
                failLabel.setVisible(true);
            } else {
                failLabel.setVisible(false);
                successLabel.setVisible(true);

                connection = handler.getConnection();

                //I detect my client from the "current_user.txt" file:
                String currentUserId = getCurrentUserId();

                //Now that I found his name, i'm searching for his Trainer
                String currentUsersTrainerId = null;
                String findUsersTrainerQuery = "SELECT fk_idTrainerClient FROM client WHERE idClient=?";
                pst = connection.prepareStatement(findUsersTrainerQuery);
                pst.setString(1, currentUserId);
                ResultSet rsFindUsersTrainer = pst.executeQuery();
                while (rsFindUsersTrainer.next()) {
                    currentUsersTrainerId = rsFindUsersTrainer.getString("fk_idTrainerClient");
                }

                //Now that I have my user's trainer, I update his/her rating

                //First I take his current rating
                String ratingQuery = "SELECT ratingTrainer FROM trainer WHERE idTrainer=?";
                pst = connection.prepareStatement(ratingQuery);
                pst.setString(1, currentUsersTrainerId);
                ResultSet rs = pst.executeQuery();
                float ratingFromChoiceBox = Float.parseFloat(String.valueOf(getStar()));
                float rating = 0;
                try {
                    while (rs.next()) {
                        rating = rs.getFloat("ratingTrainer");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if(rating==0){
                    rating=ratingFromChoiceBox;
                }
                else {
                    rating = (rating + ratingFromChoiceBox) / 2;
                }
                // Here I update his rating
                String updateQuery = "UPDATE trainer SET ratingTrainer = ? WHERE idTrainer = ?";
                PreparedStatement ps = connection.prepareStatement(updateQuery);
                ps.setString(1, String.valueOf(rating));
                ps.setString(2, currentUsersTrainerId);
                ps.executeUpdate();

                LocalDate currentDate = LocalDate.now();
                Month month = currentDate.getMonth();

                //search for this month's ID
                String idMonth = null;
                String findIdMonthQuery = "SELECT idMonth FROM month WHERE nameMonth=?";
                pst = connection.prepareStatement(findIdMonthQuery);
                pst.setString(1, String.valueOf(month));
                ResultSet rsFindIdMonth = pst.executeQuery();
                while (rsFindIdMonth.next()) {
                    idMonth = rsFindIdMonth.getString("idMonth");
                }

                String updateFeedbackQuery = "INSERT INTO feedback(textFeedback, fk_idTrainerFeedback, fk_idClientFeedback, fk_idMonthFeedback)" + "VALUES(?,?,?,?)";
                PreparedStatement psUpdateFeedback = connection.prepareStatement(updateFeedbackQuery);
                psUpdateFeedback.setString(1, feedbackTextField.getText());
                psUpdateFeedback.setString(2, currentUsersTrainerId);
                psUpdateFeedback.setString(3, currentUserId);
                psUpdateFeedback.setString(4, idMonth);
                psUpdateFeedback.executeUpdate();
            }
        }
        else{
            failTrainerLabel.setVisible(true);
        }
    }

    public Object getStar() {
        return ratingChoiceBox.getSelectionModel().getSelectedItem();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        ratingChoiceBox.setItems(FXCollections.observableArrayList(1, new Separator(), 2, new Separator(), 3, new Separator(), 4, new Separator(), 5));
    }
}
