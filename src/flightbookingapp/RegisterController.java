package flightbookingapp;

import flightbooking.DataBase.UsersDB;
import java.io.File;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button signUpButton;

    @FXML
    private Button signInButton;

    @FXML
    private Button closeButton;

    @FXML
    private Button minimizeButton;

    private UsersDB usersDB;

    @FXML
    private void initialize() {
        usersDB = new UsersDB();
    }
   @FXML
    private void handleSignUp(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
          if (usersDB.checkUsernameExists(username)) {
            showAlert("Error", "Username already exists. Please choose another one.", Alert.AlertType.ERROR);
            return;
        }

        // Validate the input fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please fill in all fields.", Alert.AlertType.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.", Alert.AlertType.ERROR);
            return;
        }

        // Validate username length
        if (username.length() < 4 || username.length() > 20) {
            showAlert("Error", "Username must be between 4 and 20 characters.", Alert.AlertType.ERROR);
            return;
        }

        // Validate password length
        if (password.length() < 8 || password.length() > 20) {
            showAlert("Error", "Password must be between 8 and 20 characters.", Alert.AlertType.ERROR);
            return;
        }

        // Validate password strength
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$")) {
            showAlert("Error", "Password must contain at least one digit, one uppercase letter, one lowercase letter, one special character, and no whitespace.", Alert.AlertType.ERROR);
            return;
        }
       
        // Attempt to register user in the database
        boolean success = usersDB.addUser(username, password);

        if (success) {
            showAlert("Registration Successful", "Account created for " + username, Alert.AlertType.INFORMATION);
            navigateToMainPage();
        } else {
            showAlert("Error", "Failed to register user. Please try again.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSignIn(ActionEvent event) {
        // Logic to switch to the login page
        navigateToLoginPage();
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleMinimize(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    private void navigateToMainPage() {
        try {
            URL url = new File("src/flightbookingapp/Main.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED); // Optional: Decorate the stage as per your need
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current registration window
            Stage currentStage = (Stage) signUpButton.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToLoginPage() {
        // Logic to switch back to the login page (if needed)
        try {
            URL url = new File("src/flightbookingapp/Login.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED); // Optional: Decorate the stage as per your need
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current registration window
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
