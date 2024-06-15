package flightbooking.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PassengersDB {
    private ConnectDB connect = new ConnectDB();

    public boolean addPassenger(int passengerID, String firstName, String lastName, String passportNumber, String email, String phone, String dateOfBirth, String nationality) {
        // Check if the passenger ID already exists
        if (checkPassengerExists(passengerID)) {
            return false; // Passenger already exists
        }

        String sql = "INSERT INTO Passengers (PassengerID, FirstName, LastName, PassportNumber, Email, Phone, DateOfBirth, Nationality) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = connect.GetConnectDB();
             PreparedStatement pStm = cn.prepareStatement(sql)) {
            pStm.setInt(1, passengerID);
            pStm.setString(2, firstName);
            pStm.setString(3, lastName);
            pStm.setString(4, passportNumber);
            pStm.setString(5, email);
            pStm.setString(6, phone);
            pStm.setString(7, dateOfBirth);
            pStm.setString(8, nationality);
            int rowsAffected = pStm.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkPassengerExists(int passengerID) {
        String sql = "SELECT * FROM Passengers WHERE PassengerID = ?";
        try (Connection cn = connect.GetConnectDB();
             PreparedStatement pStm = cn.prepareStatement(sql)) {
            pStm.setInt(1, passengerID);
            try (ResultSet rs = pStm.executeQuery()) {
                return rs.next(); // Return true if passenger exists
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassenger(int passengerID, String firstName, String lastName, String passportNumber, String email, String phone, String dateOfBirth, String nationality) {
        String sql = "UPDATE Passengers SET FirstName=?, LastName=?, PassportNumber=?, Email=?, Phone=?, DateOfBirth=?, Nationality=? WHERE PassengerID=?";
        try (Connection cn = connect.GetConnectDB();
             PreparedStatement pStm = cn.prepareStatement(sql)) {
            pStm.setString(1, firstName);
            pStm.setString(2, lastName);
            pStm.setString(3, passportNumber);
            pStm.setString(4, email);
            pStm.setString(5, phone);
            pStm.setString(6, dateOfBirth);
            pStm.setString(7, nationality);
            pStm.setInt(8, passengerID);
            int rowsAffected = pStm.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePassenger(int passengerID) {
        String sql = "DELETE FROM Passengers WHERE PassengerID=?";
        try (Connection cn = connect.GetConnectDB();
             PreparedStatement pStm = cn.prepareStatement(sql)) {
            pStm.setInt(1, passengerID);
            int rowsDeleted = pStm.executeUpdate();
            return rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticate(String passportNumber, String email) {
        String sql = "SELECT * FROM Passengers WHERE PassportNumber=? AND Email=?";
        try (Connection cn = connect.GetConnectDB();
             PreparedStatement pStm = cn.prepareStatement(sql)) {
            pStm.setString(1, passportNumber);
            pStm.setString(2, email);
            try (ResultSet rs = pStm.executeQuery()) {
                return rs.next(); // Return true if credentials are correct
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
