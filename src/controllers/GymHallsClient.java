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

public class GymHallsClient implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Label successMessage;
    @FXML
    private ChoiceBox<Object> chooseGymHall;
    @FXML
    private Label failMessage;

    private DBHandler handler;
    private Connection connection;

    @FXML
    void onBack() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ClientView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Client");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onSubmit() throws SQLException {
        if (chooseGymHall.getValue() == null) {
            successMessage.setVisible(false);
            failMessage.setVisible(true);
        } else {

            failMessage.setVisible(false);

            connection = handler.getConnection();

            String gymHallString = String.valueOf(getGymHall());
            String gymHallId = null;
            String findIdGymHallQuery = "SELECT idGymHall FROM gymhall WHERE typeGymHall=?";
            PreparedStatement pst = connection.prepareStatement(findIdGymHallQuery);
            pst.setString(1, gymHallString);
            ResultSet rsFindGymHallIdQuery = pst.executeQuery();
            try {
                while (rsFindGymHallIdQuery.next()) {
                    gymHallId = rsFindGymHallIdQuery.getString("idGymHall");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String currentUserId = null;
            try {
                File file = new File("current_user.txt");
                Scanner sc = new Scanner(file);
                currentUserId = sc.nextLine();
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String insertGymQuery="UPDATE client SET fk_idGymHall=?, fk_idTrainerClient=?, fk_idDietClient=?, fk_idExerciseClient=?, fk_idTrainerClient=? WHERE idClient=?";
            pst = connection.prepareStatement(insertGymQuery);
            pst.setString(1, gymHallId);
            pst.setString(2, null);
            pst.setString(3, null);
            pst.setString(4, null);
            pst.setString(5, null);
            pst.setString(6, currentUserId);
            pst.executeUpdate();

            successMessage.setVisible(true);

        }
    }

    public String getGymHall() {
        return (String) chooseGymHall.getSelectionModel().getSelectedItem();
    }

    ObservableList<String> objectListGymHalls = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
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
        chooseGymHall.setItems(FXCollections.observableArrayList(objectListGymHalls));
    }
}
