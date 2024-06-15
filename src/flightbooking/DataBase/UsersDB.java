package flightbooking.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsersDB {
    private ConnectDB connect = new ConnectDB();

    public boolean addUser(String username, String password) {
        // Check if the username already exists
        if (checkUsernameExists(username)) {
            return false; // Username already exists
        }

        String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";
        try (Connection cn = connect.GetConnectDB();
             PreparedStatement pStm = cn.prepareStatement(sql)) {
            pStm.setString(1, username);
            pStm.setString(2, password);
            int rowsAffected = pStm.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkUsernameExists(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (Connection cn = connect.GetConnectDB();
             PreparedStatement pStm = cn.prepareStatement(sql)) {
            pStm.setString(1, username);
            try (ResultSet rs = pStm.executeQuery()) {
                return rs.next(); // Return true if username exists
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
   public boolean authenticate(String username, String password) {
    String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
    try (Connection cn = connect.GetConnectDB();
         PreparedStatement pStm = cn.prepareStatement(sql)) {
        pStm.setString(1, username);
        pStm.setString(2, password);
        try (ResultSet rs = pStm.executeQuery()) {
            return rs.next(); // Return true if credentials are correct
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

}
