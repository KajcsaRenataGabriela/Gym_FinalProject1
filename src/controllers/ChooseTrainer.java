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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;
import  javafx.scene.image.Image;

public class ChooseTrainer implements Initializable {

    @FXML
    private Label numberOfClientsLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private ChoiceBox<Object> selectTrainerBox;

    @FXML
    private Button backButton;

    @FXML
    private Label successLabel;

    @FXML
    private Label failLabel;

    @FXML
    private Label failTrainerLabel;

    @FXML
    private TableColumn<ModelTable, String> columnTrainer;

    @FXML
    private TableView<ModelTable> tableViewTrainer;

    @FXML
    private Hyperlink linkHyperlink;

    @FXML
    private ImageView profilePicture;

    private DBHandler handler;
    private Connection connection;
    private PreparedStatement pst;
    private String currentUserId;

    String getCurrentUserId(){
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
        String currentUsersGymId = null;
        String findUsersGymQuery = "SELECT fk_idGymHall FROM client WHERE idClient=?";
        pst = connection.prepareStatement(findUsersGymQuery);
        pst.setString(1, getCurrentUserId());
        ResultSet rsFindUsersGym = pst.executeQuery();
        while (rsFindUsersGym.next()) {
            currentUsersGymId = rsFindUsersGym.getString("fk_idGymHall");
        }
        return currentUsersGymId != null;
    }

    @FXML
    void onSubmit() throws SQLException {
        if(currentUserHasATrainer()) {
            if (selectTrainerBox.getValue() == null) {
                successLabel.setVisible(false);
                failLabel.setVisible(true);
            } else {
                failLabel.setVisible(false);

                connection = handler.getConnection();

                String trainerString = String.valueOf(getTrainer());
                String trainerId = null;
                String findIdTrainerQuery = "SELECT idTrainer FROM trainer WHERE userTrainer=?";
                pst = connection.prepareStatement(findIdTrainerQuery);
                pst.setString(1, trainerString);
                ResultSet rsFindTrainerIdQuery = pst.executeQuery();
                try {
                    while (rsFindTrainerIdQuery.next()) {
                        trainerId = rsFindTrainerIdQuery.getString("idTrainer");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                String currentUserId;
                currentUserId = getCurrentUserId();

                String sql = "INSERT INTO notification(fk_idTrainerNotification,fk_idClientNotification)" + "VALUES(?,?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, trainerId);
                ps.setString(2, currentUserId);
                ps.executeUpdate();

                String updateDataQuery = "UPDATE client SET fk_idDietClient=? , fk_idExerciseClient=? , fk_idTrainerClient=? WHERE idClient=?";
                pst = connection.prepareStatement(updateDataQuery);
                pst.setString(1, null);
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setString(4, getCurrentUserId());
                pst.executeUpdate();


                successLabel.setVisible(true);
            }
        }
        else{
            failTrainerLabel.setVisible(true);
        }
    }

    @FXML
    void onClickToDisplay() throws SQLException {
        if(currentUserHasATrainer()) {
            if (selectTrainerBox.getValue() == null) {
                failLabel.setVisible(true);
            } else {
                failLabel.setVisible(false);
                connection = handler.getConnection();

                String selectedTrainerId = null;
                String findSelectedTrainerIDQuery = "SELECT idTrainer FROM trainer WHERE userTrainer=?";
                pst = connection.prepareStatement(findSelectedTrainerIDQuery);
                pst.setString(1, getTrainer());
                ResultSet rsFindSelectedTrainerIDQuery = pst.executeQuery();
                try {
                    while (rsFindSelectedTrainerIDQuery.next()) {
                        selectedTrainerId = rsFindSelectedTrainerIDQuery.getString("idTrainer");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //Here I count the number of this trainer's clients
                String clientNumberQuery = "SELECT userClient FROM client INNER JOIN trainer ON client.fk_idTrainerClient=idTrainer WHERE idTrainer=?";
                pst = connection.prepareStatement(clientNumberQuery);
                pst.setString(1, selectedTrainerId);
                ResultSet rsClientNumberQuery = pst.executeQuery();
                int numberOfClients = 0;
                try {
                    while (rsClientNumberQuery.next()) {
                        numberOfClients++;
                    }
                    numberOfClientsLabel.setText(String.valueOf(numberOfClients));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //Here I display his rating
                String ratingTrainerQuery = "SELECT ratingTrainer FROM trainer WHERE idTrainer=?";
                pst = connection.prepareStatement(ratingTrainerQuery);
                pst.setString(1, selectedTrainerId);
                ResultSet rs2 = pst.executeQuery();
                try {
                    while (rs2.next()) {
                        ratingLabel.setText(String.valueOf(rs2.getString("ratingTrainer")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //Here I display his link
                String linkTrainerQuery = "SELECT linkTrainer FROM trainer WHERE idTrainer=?";
                pst = connection.prepareStatement(linkTrainerQuery);
                pst.setString(1, selectedTrainerId);
                ResultSet rsLinkTrainer = pst.executeQuery();
                try {
                    while (rsLinkTrainer.next()) {
                        linkHyperlink.setText(String.valueOf(rsLinkTrainer.getString("linkTrainer")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //Here I display his profile picture
                String pictureTrainerQuery = "SELECT pictureTrainer FROM trainer WHERE idTrainer=?";
                pst = connection.prepareStatement(pictureTrainerQuery);
                pst.setString(1, selectedTrainerId);
                ResultSet rsPictureTrainer = pst.executeQuery();
                try {
                    while (rsPictureTrainer.next()) {
                        String url = rsPictureTrainer.getString("pictureTrainer");
                        Image image = new Image(url, true);
                        profilePicture.setImage(image);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            failTrainerLabel.setVisible(true);
        }
    }

    @FXML
    void onLink() {
        try {
            Desktop.getDesktop().browse(new URL(linkHyperlink.getText()).toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onBack() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ClientView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Client");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    public String getTrainer() {
        return (String) selectTrainerBox.getSelectionModel().getSelectedItem();
    }

    ObservableList<Object> objectListTrainerName = FXCollections.observableArrayList();
    ObservableList<ModelTable> objectListTopTrainers = FXCollections.observableArrayList();

    String selectedGymId=null;

    private void populateTable() throws SQLException {
        connection = handler.getConnection();

        String currentUserId = null;
        try {
            File file = new File("current_user.txt");
            Scanner sc = new Scanner(file);
            currentUserId = sc.nextLine();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //search current user's gym
        String findSelectedUsersGymQuery = "SELECT fk_idGymHall FROM client WHERE idClient=?";
        pst = connection.prepareStatement(findSelectedUsersGymQuery );
        pst.setString(1, currentUserId);
        ResultSet rsSelectedUsersGym = pst.executeQuery();
        try {
            while (rsSelectedUsersGym.next()) {
                selectedGymId=rsSelectedUsersGym.getString("fk_idGymHall");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String findTopTrainersQuery = "SELECT userTrainer FROM trainer WHERE fk_idGymHallTrainer=? ORDER BY ratingTrainer DESC";
        pst = connection.prepareStatement(findTopTrainersQuery);
        pst.setString(1, selectedGymId);
        ResultSet rsTopTrainers = pst.executeQuery();
        try {
            while (rsTopTrainers.next()) {
                objectListTopTrainers.add(new ModelTable(rsTopTrainers.getString("userTrainer")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewTrainer.setItems(objectListTopTrainers);
    }

    private void populateChoiceBox() throws SQLException {
        String trainersQuery = "SELECT userTrainer FROM gym_db.trainer WHERE fk_idGymHallTrainer=?";
        pst = connection.prepareStatement(trainersQuery);
        pst.setString(1, selectedGymId);
        ResultSet rsTrainers = pst.executeQuery();
        try {
            while (rsTrainers.next()) {
                objectListTrainerName.add(rsTrainers.getString("userTrainer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        selectTrainerBox.setItems(FXCollections.observableArrayList(objectListTrainerName));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        columnTrainer.setCellValueFactory(new PropertyValueFactory<>("Something"));
        try {
            populateTable();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        try {
            populateChoiceBox();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

    }
}
