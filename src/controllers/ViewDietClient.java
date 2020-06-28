package controllers;

import Database.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class ViewDietClient implements Initializable {

    @FXML
    private Label descriptionDietLabel;

    @FXML
    private Button backButton;

    @FXML
    private Label nameDietLabel;

    private DBHandler handler;
    private String currentUserId;

    @FXML
    void onBackButton() throws IOException {
        backButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ClientView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Client");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    void populateLabels() throws SQLException {
        Connection connection = handler.getConnection();

        try {
            File file = new File("current_user.txt");
            Scanner sc = new Scanner(file);
            currentUserId = sc.nextLine();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Find current clients's diet's name
        String findDietNameQuery = "SELECT nameDiet FROM diet INNER JOIN client ON diet.idDiet=fk_idDietClient WHERE idClient=?";
        PreparedStatement pst = connection.prepareStatement(findDietNameQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindDietName = pst.executeQuery();
        while (rsFindDietName.next()) {
            nameDietLabel.setText(rsFindDietName.getString("nameDiet"));
        }

        //Find current clients'sdiet's description
        String findDietDescriptionQuery = "SELECT textDiet FROM diet INNER JOIN client ON diet.idDiet=fk_idDietClient WHERE idClient=?";
        pst = connection.prepareStatement(findDietDescriptionQuery);
        pst.setString(1, currentUserId);
        ResultSet rsFindDietDescription = pst.executeQuery();
        while (rsFindDietDescription.next()) {
            descriptionDietLabel.setText(rsFindDietDescription.getString("textDiet"));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            handler = new DBHandler();
            try {
                populateLabels();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    }
}
