package controllers;

import Database.DBHandler;
import controllers.ModelTables.ModelTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TrainerStatisticsManager implements Initializable {

    @FXML
    private ChoiceBox<String> selectTrainerChoiceBox;

    @FXML
    private Label ratingLabel;

    @FXML
    private TableColumn<ModelTable, String> columnFeedback;

    @FXML
    private Label totalWorkingHoursLabel;

    @FXML
    private Button backButton;

    @FXML
    private TableView<ModelTable> tableViewFeedback;

    @FXML
    private ChoiceBox<String> selectMonthChoiceBox;

    @FXML
    private Label failMessage;

    ObservableList<String> objectListTrainer = FXCollections.observableArrayList();
    ObservableList<String> objectListMonth = FXCollections.observableArrayList();
    ObservableList<ModelTable> objectListFeedback = FXCollections.observableArrayList();

    private DBHandler handler;
    private Connection connection;
    private PreparedStatement pst;

    @FXML
    void onClickToDisplayTrainer() throws SQLException {
        if(selectTrainerChoiceBox.getValue()==null || selectMonthChoiceBox.getValue()==null)
        {
            failMessage.setVisible(true);
        }
        else
        {
            failMessage.setVisible(false);
            connection = handler.getConnection();

            float rating = 0;
            String findRatingQuery = "SELECT ratingTrainer FROM trainer WHERE userTrainer=?";
            pst = connection.prepareStatement(findRatingQuery);
            pst.setString(1, getTrainer());
            ResultSet rsFindRating = pst.executeQuery();
            try {
                while (rsFindRating.next()) {
                    rating = Float.parseFloat(rsFindRating.getString("ratingTrainer"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ratingLabel.setText(String.valueOf(rating));

            tableViewFeedback.getItems().clear();

            String idMonth = null;
            String findIdMonthQuery = "SELECT idMonth FROM month WHERE nameMonth=?";
            pst = connection.prepareStatement(findIdMonthQuery);
            pst.setString(1, getMonth());
            ResultSet rsFindIdMonth = pst.executeQuery();
            try {
                while (rsFindIdMonth.next()) {
                    idMonth = rsFindIdMonth.getString("idMonth");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String findFeedbackQuery = "SELECT textFeedback FROM feedback INNER JOIN trainer ON feedback.fk_idTrainerFeedback=idTrainer WHERE userTrainer=? AND feedback.fk_idMonthFeedback=?";
            pst = connection.prepareStatement(findFeedbackQuery);
            pst.setString(1, getTrainer());
            pst.setString(2, idMonth);
            ResultSet rsFeedback = pst.executeQuery();
            try {
                while (rsFeedback.next()) {
                    objectListFeedback.add(new ModelTable(rsFeedback.getString("textFeedback")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            tableViewFeedback.setItems(objectListFeedback);

            float workingHours = 0;
            String findWorkingHoursQuery = "SELECT workingHoursTrainer FROM trainer WHERE userTrainer=?";
            pst = connection.prepareStatement(findWorkingHoursQuery);
            pst.setString(1, getTrainer());
            ResultSet rsWorkingHours = pst.executeQuery();
            try {
                while (rsWorkingHours.next()) {
                    workingHours = Integer.parseInt(rsWorkingHours.getString("workingHoursTrainer"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            totalWorkingHoursLabel.setText(String.valueOf(workingHours));



        }
    }

    void populateChoiceBoxes() throws SQLException {

        connection = handler.getConnection();

        //Populate Trainers
        String selectTrainersQuery = "SELECT userTrainer FROM trainer";
        pst = connection.prepareStatement(selectTrainersQuery);
        ResultSet rsFindTrainers = pst.executeQuery();
        while (rsFindTrainers.next()) {
            objectListTrainer.add(rsFindTrainers.getString("userTrainer"));
        }
        selectTrainerChoiceBox.setItems(FXCollections.observableArrayList(objectListTrainer));

        // Populate moths
        String findMonthsQuery = "SELECT nameMonth FROM month";
        pst = connection.prepareStatement(findMonthsQuery);
        ResultSet rsFindMonths = pst.executeQuery();
        while (rsFindMonths.next()) {
            objectListMonth.add(rsFindMonths.getString("nameMonth"));
        }
        selectMonthChoiceBox.setItems(FXCollections.observableArrayList(objectListMonth));

    }

    @FXML
    void onBack() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ManagerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Manager");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    public String getTrainer() {
        return selectTrainerChoiceBox.getSelectionModel().getSelectedItem();
    }
    public String getMonth() {
        return selectMonthChoiceBox.getSelectionModel().getSelectedItem();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();

        columnFeedback.setCellValueFactory(new PropertyValueFactory<>("Something"));
        try {
            populateChoiceBoxes();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
