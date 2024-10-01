package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.DatabaseConnector;

public class HoodieManager {
    public List<Hoodie> getAllHoodies() {
        List<Hoodie> hoodies = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Hoodie");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Hoodie hoodie = new Hoodie();
                hoodie.setHoodieID(resultSet.getString("HoodieID"));
                hoodie.setHoodieName(resultSet.getString("HoodieName"));
                hoodie.setPrice(resultSet.getInt("Price"));
                hoodies.add(hoodie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hoodies;
    }

    public Hoodie getHoodieById(String hoodieId) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Hoodie WHERE HoodieID = ?")) {

            preparedStatement.setString(1, hoodieId);

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
    
    public boolean addHoodieToDatabase(Hoodie hoodie) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO Hoodie (HoodieID, HoodieName, Price) VALUES (?, ?, ?)")) {

            String generatedID = generateHoodieID();
            hoodie.setHoodieID(generatedID);

            preparedStatement.setString(1, hoodie.getHoodieID());
            preparedStatement.setString(2, hoodie.getHoodieName());
            preparedStatement.setInt(3, hoodie.getPrice());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    private String generateHoodieID() {
        List<Hoodie> allHoodies = getAllHoodies();
        int lastIndex = allHoodies.size() - 1;
        String lastHoodieID = lastIndex >= 0 ? allHoodies.get(lastIndex).getHoodieID() : null;

        if (lastHoodieID == null) {
            return "HO001";
        }
        int lastNumber = Integer.parseInt(lastHoodieID.substring(2));
        String newNumber = String.format("%03d", lastNumber + 1);
        return "HO" + newNumber;
    }
    
    public boolean updateHoodie(Hoodie hoodie, int newPrice) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE Hoodie SET Price = ? WHERE HoodieID = ?")) {

            preparedStatement.setInt(1, newPrice);
            preparedStatement.setString(2, hoodie.getHoodieID());

            int rowsAffected = preparedStatement.executeUpdate();

            System.out.println("SQL Statement: " + preparedStatement.toString());

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHoodie(Hoodie hoodie) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM Hoodie WHERE HoodieID = ?")) {

            preparedStatement.setString(1, hoodie.getHoodieID());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}