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
import javafx.stage.Stage;

import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUp implements Initializable {

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button toLoginButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField telephoneTextField;

    @FXML
    private Label lblMessage;

    @FXML
    private Label userAlreadyExistsLabel;

    private DBHandler handler;
    private Connection connection;

    private boolean userAlreadyExists(String usernameTrainerTextField) throws SQLException {
        connection = handler.getConnection();

        String verifyIfUserExistsQuery= "SELECT userClient FROM client WHERE userClient=?";
        PreparedStatement pst = connection.prepareStatement(verifyIfUserExistsQuery);
        pst.setString(1, usernameTrainerTextField);
        ResultSet rsFindTrainerIdQuery = pst.executeQuery();
        try {
            if (rsFindTrainerIdQuery.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void clearFields(){
        usernameTextField.setText("");
        passwordTextField.setText("");
        emailTextField.setText("");
        telephoneTextField.setText("");
    }

    private void toLoginView() throws IOException {
        toLoginButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/LoginView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Login");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    @FXML
    void onSignUpButton() throws SQLException, IOException {
        if(usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() || emailTextField.getText().isEmpty() ||telephoneTextField.getText().isEmpty())
        {
            userAlreadyExistsLabel.setVisible(false);
            lblMessage.setVisible(true);
            clearFields();
        }
        else {
            connection = handler.getConnection();

            if(userAlreadyExists(usernameTextField.getText())) {
                userAlreadyExistsLabel.setVisible(true);
                clearFields();
            }
            else{
                String sql = "INSERT INTO client(userClient,passwordClient,emailClient,telClient)" + "VALUES(?,?,?,?)";

                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, usernameTextField.getText());
                ps.setString(2, passwordTextField.getText());
                ps.setString(3, emailTextField.getText());
                ps.setString(4, telephoneTextField.getText());
                ps.executeUpdate();

                toLoginView();
            }
        }
    }

    @FXML
    void onToLogIn() throws IOException {
        toLoginView();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler=new DBHandler();
    }
}
