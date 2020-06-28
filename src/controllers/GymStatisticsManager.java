package controllers;

import Database.DBHandler;
import controllers.ModelTables.GymStatisticsManagerModelTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GymStatisticsManager implements Initializable {

    ObservableList<GymStatisticsManagerModelTable> objectList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<GymStatisticsManagerModelTable, String> columnClients;

    @FXML
    private TableColumn<GymStatisticsManagerModelTable, String> columnTrainers;

    @FXML
    private Button backButton;

    @FXML
    private TableView<GymStatisticsManagerModelTable> tableView;

    @FXML
    private TableColumn<GymStatisticsManagerModelTable, String> columnEquipment;

    @FXML
    private TableColumn<GymStatisticsManagerModelTable, String> columnExercises;

    @FXML
    private TableColumn<GymStatisticsManagerModelTable, String> columnDiet;

    @FXML
    private TableColumn<GymStatisticsManagerModelTable, String> columnGym;

    private DBHandler handler;

    @FXML
    void onBackButton() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ManagerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Manager");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }



    void display() throws SQLException {

        tableView.getItems().clear();

        try (Connection connection = handler.getConnection()) {

            String findClientQuery = "SELECT userClient,fk_idTrainerClient,fk_idExerciseClient,fk_idDietClient,fk_idGymHall FROM client";
            PreparedStatement pst = connection.prepareStatement(findClientQuery);
            ResultSet rsFindClient = pst.executeQuery();

            try {
                while (rsFindClient.next()) {

                    String client, clientTrainerId, clientExerciseId, clientDietId, trainer = null, gym = null, exercise = null, clientEquipmentId = null, equipment = null, diet = null;

                    client = rsFindClient.getString("userClient");
                    clientTrainerId = rsFindClient.getString("fk_idTrainerClient");
                    String clientGymId = rsFindClient.getString("fk_idGymHall");
                    clientExerciseId = rsFindClient.getString("fk_idExerciseClient");
                    clientDietId = rsFindClient.getString("fk_idDietClient");

                    String findTrainerQuery = "SELECT userTrainer FROM trainer WHERE idTrainer=?";
                    pst = connection.prepareStatement(findTrainerQuery);
                    pst.setString(1, clientTrainerId);
                    ResultSet rsFindTrainer = pst.executeQuery();
                    try {
                        while (rsFindTrainer.next()) {
                            trainer = rsFindTrainer.getString("userTrainer");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    String findGymQuery = "SELECT typeGymHall FROM gymhall WHERE idGymHall=?";
                    pst = connection.prepareStatement(findGymQuery);
                    pst.setString(1, clientGymId);
                    ResultSet rsFindGym = pst.executeQuery();
                    try {
                        while (rsFindGym.next()) {
                            gym = rsFindGym.getString("typeGymHall");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    String findExerciseQuery = "SELECT nameExercise,fk_idEquipmentExercise FROM exerciseinfo WHERE idExercise=?";
                    pst = connection.prepareStatement(findExerciseQuery);
                    pst.setString(1, clientExerciseId);
                    ResultSet rsFindExercise = pst.executeQuery();
                    try {
                        while (rsFindExercise.next()) {
                            exercise = rsFindExercise.getString("nameExercise");
                            clientEquipmentId = rsFindExercise.getString("fk_idEquipmentExercise");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    String findEquipmentQuery = "SELECT nameEquipment FROM equipment WHERE idEquipment=?";
                    pst = connection.prepareStatement(findEquipmentQuery);
                    pst.setString(1, clientEquipmentId);
                    ResultSet rsFindEquipment = pst.executeQuery();
                    try {
                        while (rsFindEquipment.next()) {
                            equipment = rsFindEquipment.getString("nameEquipment");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    String findDietQuery = "SELECT nameDiet FROM diet WHERE idDiet=?";
                    pst = connection.prepareStatement(findDietQuery);
                    pst.setString(1, clientDietId);
                    ResultSet rsFindDiet = pst.executeQuery();
                    try {
                        while (rsFindDiet.next()) {
                            diet = rsFindDiet.getString("nameDiet");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    objectList.add(new GymStatisticsManagerModelTable(client, gym, trainer, exercise, equipment, diet));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        tableView.setItems(objectList);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();

        columnClients.setCellValueFactory(new PropertyValueFactory<>("client"));
        columnGym.setCellValueFactory(new PropertyValueFactory<>("gym"));
        columnTrainers.setCellValueFactory(new PropertyValueFactory<>("trainer"));
        columnExercises.setCellValueFactory(new PropertyValueFactory<>("exercise"));
        columnEquipment.setCellValueFactory(new PropertyValueFactory<>("equipment"));
        columnDiet.setCellValueFactory(new PropertyValueFactory<>("diet"));

        try {
            display();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
