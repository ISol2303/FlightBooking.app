package flightbookingapp;

import flightbooking.DataBase.Model.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import flightbooking.DataBase.ConnectDB;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.sql.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    // Injecting controls from FXML for Section 1: Flight Management
    @FXML
    private TableView<Flight> flightsTable;

    @FXML
    private TableColumn<Flight, String> flightNumberCol;

    @FXML
    private TableColumn<Flight, String> originCol;

    @FXML
    private TableColumn<Flight, String> destinationCol;

    // Injecting controls from FXML for Section 2: Ticket Booking
    @FXML
    private ComboBox<String> flightComboBox;

    @FXML
    private TextField passengerNameField;

    @FXML
    private TextField passportNumberField;

    @FXML
    private TextField seatNumberField;

    @FXML
    private ComboBox<String> paymentMethodComboBox;

    // Injecting controls from FXML for Section 3: Account Information
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField dobField;

    @FXML
    private TextField nationalityField;

    // Injecting the Panes for showing/hiding
    @FXML
    private Pane flightManagementPane;

    @FXML
    private Pane ticketBookingPane;

    @FXML
    private Pane accountInfoPane;

    private ConnectDB connectDB = new ConnectDB();


// ...
    private void initialize() {
        // Using lambda expressions to set cell value factories
        flightNumberCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFlightNumber()));
        originCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrigin()));
        destinationCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDestination()));

        // Alternatively, if Flight class has JavaFX properties (e.g., SimpleStringProperty),
        // you can use PropertyValueFactory as follows:
        // flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        // originCol.setCellValueFactory(new PropertyValueFactory<>("origin"));
        // destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        // Load data into TableView
        loadFlightsTable();
    }

    // Load flights data into TableView for Section 1: Flight Management
    private void loadFlightsTable() {
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        originCol.setCellValueFactory(new PropertyValueFactory<>("origin"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));

        ObservableList<Flight> flightsList = FXCollections.observableArrayList();

        String query = "SELECT FlightNumber, Origin, Destination FROM Flights";

        try (Connection con = connectDB.GetConnectDB(); PreparedStatement pstmt = con.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String flightNumber = rs.getString("FlightNumber");
                String origin = rs.getString("Origin");
                String destination = rs.getString("Destination");

                Flight flight = new Flight(flightNumber, origin, destination);
                flightsList.add(flight);
            }

            flightsTable.setItems(flightsList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load flights data into ComboBox for Section 2: Ticket Booking
    private void loadFlightComboBox() {
        String query = "SELECT FlightNumber FROM Flights";

        try (Connection con = connectDB.GetConnectDB(); PreparedStatement pstmt = con.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String flightNumber = rs.getString("FlightNumber");
                flightComboBox.getItems().add(flightNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load user account information into Account Information section
    private void loadAccountInfo() {
        String query = "SELECT FirstName, LastName, Email, Phone, DateOfBirth, Nationality FROM Passengers WHERE PassengerID = ?";

        try (Connection con = connectDB.GetConnectDB(); PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, 1); // Assuming PassengerID 1 for demonstration

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                firstNameField.setText(rs.getString("FirstName"));
                lastNameField.setText(rs.getString("LastName"));
                emailField.setText(rs.getString("Email"));
                phoneField.setText(rs.getString("Phone"));
                dobField.setText(rs.getString("DateOfBirth"));
                nationalityField.setText(rs.getString("Nationality"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle booking ticket action for Section 2: Ticket Booking
    @FXML
    private void handleBookTicket() {
        String flightNumber = flightComboBox.getValue();
        String passengerName = passengerNameField.getText();
        String passportNumber = passportNumberField.getText();
        String seatNumber = seatNumberField.getText();
        String paymentMethod = paymentMethodComboBox.getValue();

        if (flightNumber == null || passengerName.isEmpty() || passportNumber.isEmpty() || seatNumber.isEmpty() || paymentMethod == null) {
            showAlert("Error", "Please fill in all fields.", Alert.AlertType.ERROR, ticketBookingPane);
            return;
        }

        try (Connection con = connectDB.GetConnectDB(); PreparedStatement pstmt = con.prepareStatement("INSERT INTO Bookings (PassengerID, FlightID, BookingDate, SeatNumber, PaymentStatus, TotalPrice, BookingStatus, PaymentMethod) "
                + "VALUES (?, (SELECT FlightID FROM Flights WHERE FlightNumber = ?), ?, ?, 'Pending', ?, 'Confirmed', ?)")) {

            pstmt.setInt(1, 1); // Assuming PassengerID 1 for demonstration
            pstmt.setString(2, flightNumber);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(4, seatNumber);
            pstmt.setDouble(5, calculateTotalPrice()); // Implement this method to calculate total price
            pstmt.setString(6, paymentMethod);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Success", "Booking successful.", Alert.AlertType.INFORMATION, ticketBookingPane);
                clearTicketBookingFields();
            } else {
                showAlert("Error", "Booking failed.", Alert.AlertType.ERROR, ticketBookingPane);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Booking failed. Please try again.", Alert.AlertType.ERROR, ticketBookingPane);
        }
    }

    // Helper method to calculate total price (example implementation)
    private double calculateTotalPrice() {
        // Implement your logic here to calculate total price based on seat class, etc.
        return 200.0; // Example total price
    }

    // Handle edit account action for Section 3: Account Information
    @FXML
    private void handleEditAccount() {
        enableAccountFields(true);
    }

    // Handle save account action for Section 3: Account Information
    @FXML
    private void handleSaveAccount() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String dob = dobField.getText();
        String nationality = nationalityField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty() || nationality.isEmpty()) {
            showAlert("Error", "Please fill in all fields.", Alert.AlertType.ERROR, accountInfoPane);
            return;
        }

        try (Connection con = connectDB.GetConnectDB(); PreparedStatement pstmt = con.prepareStatement("UPDATE Passengers SET FirstName=?, LastName=?, Email=?, Phone=?, DateOfBirth=?, Nationality=? WHERE PassengerID=?")) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, dob);
            pstmt.setString(6, nationality);
            pstmt.setInt(7, 1); // Assuming PassengerID 1 for demonstration

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Success", "Account information updated.", Alert.AlertType.INFORMATION, accountInfoPane);
                enableAccountFields(false);
            } else {
                showAlert("Error", "Failed to update account information.", Alert.AlertType.ERROR, accountInfoPane);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update account information. Please try again.", Alert.AlertType.ERROR, accountInfoPane);
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType, Pane paneToShow) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        showPane(paneToShow);
    }

    // Helper method to clear ticket booking fields after successful booking
    private void clearTicketBookingFields() {
        passengerNameField.clear();
        passportNumberField.clear();
        seatNumberField.clear();
        flightComboBox.getSelectionModel().clearSelection();
        paymentMethodComboBox.getSelectionModel().clearSelection();
    }

    // Helper method to enable/disable account fields for editing
    private void enableAccountFields(boolean enable) {
        firstNameField.setEditable(enable);
        lastNameField.setEditable(enable);
        emailField.setEditable(enable);
        phoneField.setEditable(enable);
        dobField.setEditable(enable);
        nationalityField.setEditable(enable);
    }

    // Helper method to show the specified pane and hide others
    private void showPane(Pane paneToShow) {
        flightManagementPane.setVisible(false);
        ticketBookingPane.setVisible(false);
        accountInfoPane.setVisible(false);

        paneToShow.setVisible(true);
    }

    // Other methods and event handlers as needed
    @FXML
    private void handleAddFlightBooking() {
        showPane(flightManagementPane);
    }

    // Handle menu button for Ticket Booking
    @FXML
    private void handleBooking() {
        showPane(ticketBookingPane);
    }

    // Handle menu button for Account Information
    @FXML
    private void handleAccount() {
        showPane(accountInfoPane);
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

    @FXML
    private void handleLogOut(ActionEvent event) {
        try {
            // Load the login FXML file
            URL url = new File("src/flightbookingapp/Login.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);

            // Get the current stage (window)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);

            // Optionally, set any other properties for the new stage
            // For example:
            // currentStage.setTitle("Login");
            // currentStage.setResizable(false);
            // Show the stage (window)
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
