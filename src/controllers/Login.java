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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private ChoiceBox<Object> choiceBoxRole;

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label lblMessage;

    @FXML
    private Button toSignUpButton;

    @FXML
    private Label lblMessageChooseRole;

    private DBHandler handler;
    private Connection connection;
    private PreparedStatement pst;

    @FXML
    void onToSignUpButton() throws Exception {
        toSignUpButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/SignUpView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Sign Up");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    void clear() {
        usernameTextField.setText("");
        passwordTextField.setText("");
        lblMessageChooseRole.setVisible(false);
        lblMessage.setVisible(true);
    }

    void writeInFile(String userId){
        File f = new File("current_user.txt");

        FileWriter fr = null;
        BufferedWriter br = null;

        try{
            fr = new FileWriter(f);
            br = new BufferedWriter(fr);

            br.write(userId);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                assert br != null;
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void loadManagerView() throws IOException {
        loginButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ManagerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Manager " + usernameTextField.getText());
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    void saveManagerIntoFile(){
        try {
            String userId = null;

            String findIdUserQuery = "SELECT idManager FROM manager WHERE userManager=?";
            pst = connection.prepareStatement(findIdUserQuery);
            pst.setString(1, usernameTextField.getText());
            ResultSet rsFindCurrentUserIdQuery = pst.executeQuery();
            try {
                while (rsFindCurrentUserIdQuery.next()) {
                    userId = rsFindCurrentUserIdQuery.getString("idManager");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            writeInFile(userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loginManager(){
        String queryManager = "SELECT * FROM manager WHERE userManager=? and passwordManager=?";
        try {
            pst = connection.prepareStatement(queryManager);
            pst.setString(1, usernameTextField.getText());
            pst.setString(2, passwordTextField.getText());
            ResultSet rs = pst.executeQuery();
            int flag = 0;
            while (rs.next())
                flag++;
            if (flag > 0) {
                {
                    saveManagerIntoFile();
                    loadManagerView();
                }
            } else {
                clear();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    void loadTrainerView() throws IOException {
        loginButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/TrainerView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Trainer " + usernameTextField.getText());
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

//    void findUserId(String idField, String table, String usernameField, String usernameTextField) throws SQLException {
//
//        String findUserIdQuery = "SELECT ? FROM ? WHERE ?=?";
//        pst = connection.prepareStatement(findUserIdQuery);
//        pst.setString(1, idField);
//        pst.setString(2, table);
//        pst.setString(3, usernameField);
//        pst.setString(4, usernameTextField);
//
//        ResultSet rsFindCurrentUserIdQuery = pst.executeQuery();
//        try {
//            while (rsFindCurrentUserIdQuery.next()) {
//                userId = rsFindCurrentUserIdQuery.getString(idField);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    void loginTrainer(){

        try {

            String userId = null;

            String findUserIdQuery = "SELECT idTrainer FROM trainer WHERE userTrainer=?";
            pst = connection.prepareStatement(findUserIdQuery);
            pst.setString(1, usernameTextField.getText());
            ResultSet rsFindCurrentUserIdQuery = pst.executeQuery();
            try {
                while (rsFindCurrentUserIdQuery.next()) {
                    userId = rsFindCurrentUserIdQuery.getString("idTrainer");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            findUserId("idTrainer", "trainer", "userTrainer" , usernameTextField.getText());

            writeInFile(userId);

            String queryTrainer = "SELECT * FROM trainer WHERE userTrainer=? and passwordTrainer=?";
            pst = connection.prepareStatement(queryTrainer);
            pst.setString(1, usernameTextField.getText());
            pst.setString(2, passwordTextField.getText());
            ResultSet rs = pst.executeQuery();
            int flag = 0;
            while (rs.next())
                flag++;
            if (flag > 0) {
                try {
                    loadTrainerView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void loadClientView() throws IOException {
        loginButton.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("../view/ClientView.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Client " + usernameTextField.getText());
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();
    }

    void loginClient(){
        String queryClient = "SELECT * FROM client WHERE userClient=? and passwordClient=?";
        try {
            pst = connection.prepareStatement(queryClient);
            pst.setString(1, usernameTextField.getText());
            pst.setString(2, passwordTextField.getText());
            ResultSet rs = pst.executeQuery();
            int flag = 0;
            while (rs.next())
                flag++;
            if (flag > 0) {
                try {

                    File f = new File("current_user.txt");

                    FileWriter fr = null;
                    BufferedWriter br = null;
                    String userId = null;

                    String findUserIdQuery = "SELECT idClient FROM client WHERE userClient=?";
                    pst = connection.prepareStatement(findUserIdQuery);
                    pst.setString(1, usernameTextField.getText());
                    ResultSet rsFindCurrentUserIdQuery = pst.executeQuery();
                    try {
                        while (rsFindCurrentUserIdQuery.next()) {
                            userId = rsFindCurrentUserIdQuery.getString("idClient");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    try{
                        fr = new FileWriter(f);
                        br = new BufferedWriter(fr);

                        assert userId != null;
                        br.write(userId);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally{
                        try {
                            assert br != null;
                            br.close();
                            fr.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    loadClientView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onActionLogin() {
        connection = handler.getConnection();
        if (choiceBoxRole.getValue() == null) {
            lblMessageChooseRole.setVisible(true);
        } else {
            if (getRole().equals("Manager")) {
                loginManager();
            }
            if (getRole().equals("Trainer")) {
                loginTrainer();
            }
            if (getRole().equals("Client")) {
                loginClient();
            }
        }
    }

    public String getRole() {
        return (String) choiceBoxRole.getSelectionModel().getSelectedItem();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        choiceBoxRole.setItems(FXCollections.observableArrayList("Manager", new Separator(), "Trainer", new Separator(), "Client"));
    }
}
