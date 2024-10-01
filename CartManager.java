package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.DatabaseConnector;

public class CartManager {
	public List<CartItem> getCartItemsByUserID(String userID) {
        List<CartItem> cartItems = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM Cart WHERE UserID = ?")) {

            preparedStatement.setString(1, userID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    CartItem cartItem = new CartItem();
                    cartItem.setUserID(resultSet.getString("UserID"));
                    cartItem.setHoodieID(resultSet.getString("HoodieID"));
                    cartItem.setQuantity(resultSet.getInt("Quantity"));
                    Hoodie hoodie = getHoodieByID(cartItem.getHoodieID());
                    cartItem.setHoodieName(hoodie.getHoodieName());
                    cartItem.setHoodiePrice(hoodie.getPrice());
                    cartItems.add(cartItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartItems;
    }

    private Hoodie getHoodieByID(String hoodieID) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM Hoodie WHERE HoodieID = ?")) {

            preparedStatement.setString(1, hoodieID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Hoodie hoodie = new Hoodie();
                    hoodie.setHoodieID(resultSet.getString("HoodieID"));
                    hoodie.setHoodieName(resultSet.getString("HoodieName"));
                    hoodie.setPrice(resultSet.getInt("Price"));
   
                    return hoodie;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addToCart(String userID, String hoodieID, int quantity) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO Cart (UserID, HoodieID, Quantity) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, hoodieID);
            preparedStatement.setInt(3, quantity);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFromCart(String userID, String hoodieID) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM Cart WHERE UserID = ? AND HoodieID = ?")) {

            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, hoodieID);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getTransactionIndex() {
        try (Connection connection = DatabaseConnector.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(TransactionID) FROM transactionheader")) {

            if (resultSet.next()) {
                String maxTransactionID = resultSet.getString(1);
                if (maxTransactionID != null) {
                    int transactionIndex = Integer.parseInt(maxTransactionID.substring(2));
                    return transactionIndex + 1;
                } else {
                    return 1; 
                }
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; 
        }
    }


  
    public boolean clearCart(String userID) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM Cart WHERE UserID = ?")) {

            preparedStatement.setString(1, userID);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean insertTransaction(String userID, String transactionID, double totalAmount, List<CartItem> cartItems) {
        try (Connection connection = DatabaseConnector.connect()) {
            String insertTransactionHeaderSQL = "INSERT INTO transactionheader (TransactionID, UserID, Date, TotalPrice) VALUES (?, ?, NOW(), ?)";
            try (PreparedStatement transactionHeaderStatement = connection.prepareStatement(insertTransactionHeaderSQL)) {
                transactionHeaderStatement.setString(1, transactionID);
                transactionHeaderStatement.setString(2, userID);
                transactionHeaderStatement.setDouble(3, totalAmount);

                transactionHeaderStatement.executeUpdate();
            }

            String insertTransactionDetailSQL = "INSERT INTO transactiondetail (TransactionID, HoodieID, Quantity) VALUES (?, ?, ?)";
            try (PreparedStatement transactionDetailStatement = connection.prepareStatement(insertTransactionDetailSQL)) {
                for (CartItem cartItem : cartItems) {
                    transactionDetailStatement.setString(1, transactionID);
                    transactionDetailStatement.setString(2, cartItem.getHoodieID());
                    transactionDetailStatement.setInt(3, cartItem.getQuantity());

                    transactionDetailStatement.executeUpdate();
                }
            }

            String clearCartSQL = "DELETE FROM cart WHERE UserID = ?";
            try (PreparedStatement clearCartStatement = connection.prepareStatement(clearCartSQL)) {
                clearCartStatement.setString(1, userID);
                clearCartStatement.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
