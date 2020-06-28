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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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

public class StatisticsManager implements Initializable {

    ObservableList<ModelTable> objectListTrainers = FXCollections.observableArrayList();
    ObservableList<ModelTable> objectListClients = FXCollections.observableArrayList();
    ObservableList<ModelTable> objectListExercises = FXCollections.observableArrayList();
    ObservableList<ModelTable> objectListEquipment = FXCollections.observableArrayList();
    ObservableList<String> objectListGymHalls = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Object, Object> columnClients;

    @FXML
    private TableColumn<ModelTable, Object> columnEquipment;

    @FXML
    private TableView<ModelTable> tableViewEquipment;

    @FXML
    private ChoiceBox<Object> selectGymHall;

    @FXML
    private TableView<ModelTable> tableViewClients;

    @FXML
    private TableColumn<ModelTable, String> columnTrainers;

    @FXML
    private Button backButton;

    @FXML
    private TableView<ModelTable> tableViewExercises;

    @FXML
    private TableView<ModelTable> tableViewTrainers;

    @FXML
    private TableColumn<ModelTable, String> columnExercises;

    private DBHandler handler;
    private Connection connection;

    @FXML
    void onBackButton() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ManagerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Manager");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    void clearTables(){
        tableViewTrainers.getItems().clear();
        tableViewClients.getItems().clear();
        tableViewExercises.getItems().clear();
        tableViewEquipment.getItems().clear();
    }

    @FXML
    void onClickToDisplay() throws SQLException {
        clearTables();

        String gymHallString = String.valueOf(getGymHall());
        String gymHallId = null;
        String findIdGymHallQuery = "SELECT idGymHall FROM gymhall WHERE typeGymHall=?";
        PreparedStatement pst = connection.prepareStatement(findIdGymHallQuery);
        pst.setString(1, gymHallString);
        ResultSet rsFindGymHallIDQuery = pst.executeQuery();
        try {
            while (rsFindGymHallIDQuery.next()) {
                gymHallId=rsFindGymHallIDQuery.getString("idGymHall");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connection = handler.getConnection();

        String findTrainerQuery = "SELECT userTrainer FROM trainer INNER JOIN gymhall ON trainer.fk_idGymHallTrainer=idGymHall WHERE idGymHall=?";
        pst = connection.prepareStatement(findTrainerQuery);
        pst.setString(1, gymHallId);
        ResultSet rsFindTrainerQuery = pst.executeQuery();
        try {

            while (rsFindTrainerQuery.next()) {
                objectListTrainers.add(new ModelTable(rsFindTrainerQuery.getString("userTrainer")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String findClientQuery = "SELECT userClient FROM client INNER JOIN gymhall ON client.fk_idGymHall=idGymHall WHERE idGymHall=?";
        pst = connection.prepareStatement(findClientQuery);
        pst.setString(1, gymHallId);
        ResultSet rsFindClientQuery = pst.executeQuery();
        try {
            while (rsFindClientQuery.next()) {
                objectListClients.add(new ModelTable(rsFindClientQuery.getString("userClient")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String findExerciseQuery = "SELECT nameExercise FROM exerciseinfo INNER JOIN gymhall ON exerciseinfo.fk_idGymHallExercise=idGymHall WHERE idGymHall=?";
        pst = connection.prepareStatement(findExerciseQuery);
        pst.setString(1, gymHallId);
        ResultSet rsFindExerciseQuery = pst.executeQuery();
        try {
            while (rsFindExerciseQuery.next()) {
                objectListExercises.add(new ModelTable(rsFindExerciseQuery.getString("nameExercise")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String findEquipmentQuery = "SELECT nameEquipment FROM equipment INNER JOIN gymhall ON equipment.fk_gymHallID=idGymHall WHERE idGymHall=?";
        pst = connection.prepareStatement(findEquipmentQuery);
        pst.setString(1, gymHallId);
        ResultSet rsFindEquipment = pst.executeQuery();
        try {

            while (rsFindEquipment.next()) {
                objectListEquipment.add(new ModelTable(rsFindEquipment.getString("nameEquipment")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        populateTables();
    }

    private void populateTables(){
        tableViewTrainers.setItems(objectListTrainers);
        tableViewClients.setItems(objectListClients);
        tableViewExercises.setItems(objectListExercises);
        tableViewEquipment.setItems(objectListEquipment);
    }

    public String getGymHall() {
        return (String) selectGymHall.getSelectionModel().getSelectedItem();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();

        columnTrainers.setCellValueFactory(new PropertyValueFactory<>("Something"));
        columnClients.setCellValueFactory(new PropertyValueFactory<>("Something"));
        columnExercises.setCellValueFactory(new PropertyValueFactory<>("Something"));
        columnEquipment.setCellValueFactory(new PropertyValueFactory<>("Something"));

        connection = handler.getConnection();
        String sql = "SELECT typeGymHall FROM gym_db.gymhall";
        try {
            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                objectListGymHalls.add(rs.getString("typeGymHall"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        selectGymHall.setItems(FXCollections.observableArrayList(objectListGymHalls));
    }
}
