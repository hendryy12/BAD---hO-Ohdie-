package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.DatabaseConnector;

public class TransactionManager {

    private List<Transaction> transactions;
    private List<TransactionDetail> transactionDetails;

    public TransactionManager() {
        this.transactions = new ArrayList<>();
        this.transactionDetails = new ArrayList<>();
    }

    public List<Transaction> getTransactionsByUserID(String userID) {
        List<Transaction> userTransactions = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM transactionheader WHERE UserID = ?")) {

            preparedStatement.setString(1, userID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionID(resultSet.getString("TransactionID"));
                    transaction.setUserID(resultSet.getString("UserID"));
                    userTransactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userTransactions;
    }


    public List<TransactionDetail> getTransactionDetailsByTransactionID(String transactionID) {
        List<TransactionDetail> details = new ArrayList<>();
        for (TransactionDetail detail : transactionDetails) {
            if (detail.getTransactionID().equals(transactionID)) {
                details.add(detail);
            }
        }
        return details;
    }
    
    public List<TransactionDetail> getTransactionDetails(String transactionID) {
        List<TransactionDetail> transactionDetails = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT td.TransactionID, td.HoodieID, td.Quantity, h.HoodieName, h.Price " +
                     "FROM TransactionDetail td " +
                     "JOIN Hoodie h ON td.HoodieID = h.HoodieID " +
                     "WHERE td.TransactionID = ?")) {

            preparedStatement.setString(1, transactionID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    TransactionDetail transactionDetail = new TransactionDetail();
                    transactionDetail.setTransactionID(resultSet.getString("TransactionID"));
                    transactionDetail.setHoodieID(resultSet.getString("HoodieID"));
                    transactionDetail.setQuantity(resultSet.getInt("Quantity"));
                    transactionDetail.setHoodieName(resultSet.getString("HoodieName"));
                    
                    // Ambil harga hoodie dari database dan atur di objek TransactionDetail
                    double hoodiePrice = resultSet.getDouble("Price");
                    transactionDetail.setHoodiePrice(hoodiePrice);

                    transactionDetails.add(transactionDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactionDetails;
    }


    
    public double getHoodiePriceById(String hoodieID) {
        double hoodiePrice = 0.0;

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT Price FROM Hoodie WHERE HoodieID = ?")) {

            preparedStatement.setString(1, hoodieID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hoodiePrice = resultSet.getDouble("Price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hoodiePrice;
    }

}
