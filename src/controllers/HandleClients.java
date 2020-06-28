package controllers;

import Database.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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

public class HandleClients implements Initializable {

    @FXML
    private ChoiceBox<Object> selectDiet;

    @FXML
    private ChoiceBox<Object> selectClient;

    @FXML
    private ChoiceBox<Object> selectExercise;

    @FXML
    private ChoiceBox<Object> selectPendingClient;

    @FXML
    private Button backButton;

    @FXML
    private Button assignButton;

    @FXML
    private Label successMessage;

    @FXML
    private Label failMessage;

    @FXML
    private Label numberOfRequestsLabel;

    @FXML
    private Button acceptNewClient;

    @FXML
    private Label successReqMessage;

    @FXML
    private Label failReqMessage;

    ObservableList<String> objectListClients = FXCollections.observableArrayList();
    ObservableList<String> objectListDiets = FXCollections.observableArrayList();
    ObservableList<String> objectListExercises = FXCollections.observableArrayList();
    ObservableList<String> objectListPendingClients = FXCollections.observableArrayList();

    private DBHandler handler;
    private Connection connection;
    private PreparedStatement pst;
    private String currentUserId;

    @FXML
    void onAcceptNewClient() throws SQLException {
        if(selectPendingClient.getValue()==null) {
            failReqMessage.setVisible(true);
            successReqMessage.setVisible(false);
        }
        else{
            failReqMessage.setVisible(false);

            connection = handler.getConnection();

            //I search for the ID of the selected pending client
            String selectedClientsId=null;
            String findSelectedClientsIdQuery = "SELECT idClient FROM client WHERE userClient=?";
            pst = connection.prepareStatement(findSelectedClientsIdQuery);
            pst.setString(1, getPendingClient());
            ResultSet rsSelectedClientsId = pst.executeQuery();
            try {
                while (rsSelectedClientsId.next()) {
                    selectedClientsId = rsSelectedClientsId.getString("idClient");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //I insert the fk
            String insertTrainerIdQuery="UPDATE client SET fk_idTrainerClient=? WHERE userClient=?";
            pst = connection.prepareStatement(insertTrainerIdQuery);
            pst.setString(1, currentUserId);
            pst.setString(2, getPendingClient());
            pst.executeUpdate();

            //I delete the row
            String sql="DELETE FROM notification WHERE fk_idClientNotification=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, selectedClientsId);
            ps.executeUpdate();

            successReqMessage.setVisible(true);
        }
    }

    @FXML
    void onAssignButton() throws SQLException {
        if(selectExercise.getValue()==null || selectDiet.getValue()==null || selectClient.getValue()==null)
        {
            successMessage.setVisible(false);
            failMessage.setVisible(true);
        }
        else
        {
            failMessage.setVisible(false);
            connection = handler.getConnection();

            String dietId = null;
            String findIdDietQuery = "SELECT idDiet FROM diet WHERE nameDiet=?";
            pst = connection.prepareStatement(findIdDietQuery);
            pst.setString(1, getDiet());
            ResultSet rsFindDietIdQuery = pst.executeQuery();
            try {
                while (rsFindDietIdQuery.next()) {
                    dietId = rsFindDietIdQuery.getString("idDiet");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String exerciseId = null;
            String findIdExerciseQuery = "SELECT idExercise FROM exerciseinfo WHERE nameExercise=?";
            pst = connection.prepareStatement(findIdExerciseQuery);
            pst.setString(1, getExercise());
            ResultSet rsFindExerciseIdQuery = pst.executeQuery();
            try {
                while (rsFindExerciseIdQuery.next()) {
                    exerciseId = rsFindExerciseIdQuery.getString("idExercise");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String insertDataQuery="UPDATE client SET fk_idDietClient=? , fk_idExerciseClient=? WHERE userClient=?";
            pst = connection.prepareStatement(insertDataQuery);
            pst.setString(1, dietId);
            pst.setString(2, exerciseId);
            pst.setString(3, getClient());
            pst.executeUpdate();

            successMessage.setVisible(true);
        }
    }

    @FXML
    void onBackButton() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/TrainerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Trainer");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    public String getClient() {
        return (String) selectClient.getSelectionModel().getSelectedItem();
    }
    public String getDiet() {
        return (String) selectDiet.getSelectionModel().getSelectedItem();
    }
    public String getExercise() {
        return (String) selectExercise.getSelectionModel().getSelectedItem();
    }
    public String getPendingClient() {
        return (String) selectPendingClient.getSelectionModel().getSelectedItem();
    }

    void populateChoiceBoxes() throws SQLException {

        connection = handler.getConnection();

        try {
            File file = new File("current_user.txt");
            Scanner sc = new Scanner(file);
            currentUserId = sc.nextLine();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Populate his pending clients
        int numberOfRequests = 0;
        String findPendingClientsQuery = "SELECT userClient FROM client INNER JOIN notification ON client.idClient=fk_idClientNotification WHERE fk_idTrainerNotification=?";
        pst = connection.prepareStatement(findPendingClientsQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindUsersPendingClients = pst.executeQuery();
        while (rsFindUsersPendingClients.next()) {
            objectListPendingClients.add(rsFindUsersPendingClients.getString("userClient"));
            numberOfRequests++;
        }
        numberOfRequestsLabel.setText(String.valueOf(numberOfRequests));
        selectPendingClient.setItems(FXCollections.observableArrayList(objectListPendingClients));

        // Populate his clients
        String findClientsQuery = "SELECT userClient FROM client WHERE fk_idTrainerClient=?";
        pst = connection.prepareStatement(findClientsQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindClients = pst.executeQuery();
        while (rsFindClients.next()) {
            objectListClients.add(rsFindClients.getString("userClient"));
        }
        selectClient.setItems(FXCollections.observableArrayList(objectListClients));

        // Populate his diets
        String findUsersDietsQuery = "SELECT nameDiet FROM diet WHERE fk_idTrainerDiet=?";
        pst = connection.prepareStatement(findUsersDietsQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindUsersDiets = pst.executeQuery();
        while (rsFindUsersDiets.next()) {
            objectListDiets.add(rsFindUsersDiets.getString("nameDiet"));
        }
        selectDiet.setItems(FXCollections.observableArrayList(objectListDiets));

        // Populate his exercises
        String findUsersExercisesQuery = "SELECT nameExercise FROM exerciseinfo WHERE fk_idTrainerExercise=?";
        pst = connection.prepareStatement(findUsersExercisesQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindUsersExercises = pst.executeQuery();
        while (rsFindUsersExercises.next()) {
            objectListExercises.add(rsFindUsersExercises.getString("nameExercise"));
        }
        selectExercise.setItems(FXCollections.observableArrayList(objectListExercises));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        try {
            populateChoiceBoxes();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
