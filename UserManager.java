package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import application.DatabaseConnector;

public class UserManager {
    public boolean validateCredentials(String username, String password) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM User WHERE Username = ? AND Password = ?")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUserRole(String username) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT Role FROM User WHERE Username = ?")) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Role");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean saveUser(String userId, String username, String password, String email, String gender, String phoneNumber, String address, String role) {
    	try (Connection connection = DatabaseConnector.connect();
			PreparedStatement preparedStatement = connection.prepareStatement(
			     "INSERT INTO User (UserId, Username, Password, Email, Gender, PhoneNumber, Address, Role) " +
			             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
			
				preparedStatement.setString(1, userId);
				preparedStatement.setString(2, username);
				preparedStatement.setString(3, password);
				preparedStatement.setString(4, email);
				preparedStatement.setString(5, gender);
				preparedStatement.setString(6, phoneNumber);
				preparedStatement.setString(7, address);
				preparedStatement.setString(8, role);
				
				int rowsAffected = preparedStatement.executeUpdate();
				return rowsAffected > 0;
	
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    public int getUserCount() {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM User");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public User getUserByUsername(String username) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM User WHERE Username = ?")) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setUserID(resultSet.getString("UserId"));
                    user.setUsername(resultSet.getString("Username"));
                    user.setPassword(resultSet.getString("Password"));
                    user.setEmail(resultSet.getString("Email"));
                    user.setGender(resultSet.getString("Gender"));
                    user.setPhoneNumber(resultSet.getString("PhoneNumber"));
                    user.setAddress(resultSet.getString("Address"));
                    user.setRole(resultSet.getString("Role"));
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public User getUserByID(String userID) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM User WHERE UserID = ?")) {

            preparedStatement.setString(1, userID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setUserID(resultSet.getString("UserID"));
                    user.setUsername(resultSet.getString("Username"));
                    user.setEmail(resultSet.getString("Email"));
                    user.setPhoneNumber(resultSet.getString("PhoneNumber"));
                    user.setAddress(resultSet.getString("Address"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}