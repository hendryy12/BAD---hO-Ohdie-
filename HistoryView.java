package view;

import controller.HistoryController;
import controller.LoginController;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Transaction;
import model.TransactionDetail;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import application.DatabaseConnector;

public class HistoryView {

    private TableView<Transaction> transactionTable;
    private TableView<TransactionDetail> transactionDetailTable;
    private Label usernameLabel;
    private Label transactionIdLabel;
    private Label totalLabel;
    private TableColumn<Transaction, String> transactionIdColumn;
    private TableColumn<Transaction, String> userIdColumn;
    private TableColumn<TransactionDetail, String> detailTransactionIdColumn;
    private TableColumn<TransactionDetail, String> hoodieIdColumn;
    private TableColumn<TransactionDetail, String> hoodieNameColumn;
    private TableColumn<TransactionDetail, Integer> quantityColumn;
    private TableColumn<TransactionDetail, Double> totalPriceColumn;
    private Transaction selectedTransaction;
    private User loggedInUser;

    public void initialize(HistoryController historyController, String username, User loggedInUser) {
    	this.loggedInUser = loggedInUser;
        BorderPane root = new BorderPane();

        usernameLabel = new Label(username + "'s Transaction(s)");
        transactionIdLabel = new Label();
        totalLabel = new Label("Total Price: ");

        usernameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        root.setTop(createMenuBar(historyController));

        transactionTable = new TableView<>();
        createTransactionDetailTable();

        transactionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTransaction = newSelection;
                historyController.onTransactionSelected(newSelection);
                updateTotalPriceLabel(newSelection);
            } else {
                historyController.onNoTransactionSelected();
            }
        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(usernameLabel, transactionTable, transactionIdLabel, transactionDetailTable, totalLabel);

        root.setCenter(vBox);

        Scene scene = new Scene(root, 800, 600);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("History Scene");

        primaryStage.setOnCloseRequest(event -> historyController.close());

        primaryStage.show();
    }

    private MenuBar createMenuBar(HistoryController historyController) {
        MenuBar menuBar = new MenuBar();
        Menu accountMenu = new Menu("Account");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> {
            Stage currentStage = (Stage) usernameLabel.getScene().getWindow();
            currentStage.close();
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView);
            loginController.start(new Stage());
        });
        accountMenu.getItems().add(logoutItem);

        Menu userMenu = new Menu("User");

        MenuItem homeItem = new MenuItem("Home");
        homeItem.setOnAction(e -> historyController.navigateToHome(loggedInUser));

        MenuItem cartItem = new MenuItem("Cart");
        cartItem.setOnAction(e -> historyController.navigateToCart());

        MenuItem historyItem = new MenuItem("History");
        historyItem.setDisable(true);
        userMenu.getItems().addAll(homeItem, cartItem, historyItem);
        menuBar.getMenus().addAll(accountMenu, userMenu);
        return menuBar;
    }

    private void createTransactionDetailTable() {
        transactionDetailTable = new TableView<>();

        transactionDetailTable.getColumns().addAll(
                getDetailTransactionIdColumn(),
                getHoodieIdColumn(),
                getHoodieNameColumn(),
                getQuantityColumn(),
                getTotalPriceColumn()
        );

    }

    public TableView<Transaction> getTransactionTable() {
        return transactionTable;
    }

    public TableView<TransactionDetail> getTransactionDetailTable() {
        return transactionDetailTable;
    }

    public Label getTransactionIdLabel() {
        return transactionIdLabel;
    }

    public Label getTotalLabel() {
        return totalLabel;
    }

    public void show(HistoryController historyController, String username, User loggedInUser) {
        initialize(historyController, username, loggedInUser);
    }

    public void close() {
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.close();
    }

    public void showTransactionDetails(List<TransactionDetail> transactionDetails) {
        transactionDetailTable.getItems().clear();
        transactionDetailTable.getItems().addAll(transactionDetails);
        calculateTotalPrice(transactionDetails);

        updateTotalPriceLabel(selectedTransaction); 
    }

    private double calculateTotalPrice(List<TransactionDetail> transactionDetails) {
        double totalPrice = 0.0;

        for (TransactionDetail transactionDetail : transactionDetails) {
            double hoodiePrice = transactionDetail.getHoodiePrice();
            int quantity = transactionDetail.getQuantity();
            double totalHoodiePrice = hoodiePrice * quantity;
            totalPrice += totalHoodiePrice;
        }

        return totalPrice;
    }

    public void showTotalPrice(double totalPrice) {
        totalLabel.setText(String.format("Total Price: %.2f", totalPrice));
    }

    private TableColumn<Transaction, String> getTransactionIdColumn() {
        if (transactionIdColumn == null) {
            transactionIdColumn = new TableColumn<>("Transaction ID");
            transactionIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionID()));
        }
        return transactionIdColumn;
    }

    private TableColumn<Transaction, String> getUserIdColumn() {
        if (userIdColumn == null) {
            userIdColumn = new TableColumn<>("User ID");
            userIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUserID()));
        }
        return userIdColumn;
    }

    private TableColumn<TransactionDetail, String> getDetailTransactionIdColumn() {
        if (detailTransactionIdColumn == null) {
            detailTransactionIdColumn = new TableColumn<>("Transaction ID");
            detailTransactionIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionID()));
        }
        return detailTransactionIdColumn;
    }

    private TableColumn<TransactionDetail, String> getHoodieIdColumn() {
        if (hoodieIdColumn == null) {
            hoodieIdColumn = new TableColumn<>("Hoodie ID");
            hoodieIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoodieID()));
        }
        return hoodieIdColumn;
    }

    private TableColumn<TransactionDetail, String> getHoodieNameColumn() {
        if (hoodieNameColumn == null) {
            hoodieNameColumn = new TableColumn<>("Hoodie Name");
            hoodieNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoodieName()));
        }
        return hoodieNameColumn;
    }

    private TableColumn<TransactionDetail, Integer> getQuantityColumn() {
        if (quantityColumn == null) {
            quantityColumn = new TableColumn<>("Quantity");
            quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        }
        return quantityColumn;
    }

    private TableColumn<TransactionDetail, Double> getTotalPriceColumn() {
        if (totalPriceColumn == null) {
            totalPriceColumn = new TableColumn<>("Total Price");
            totalPriceColumn.setCellValueFactory(data -> {
                double totalHoodiePrice = calculateTotalPrice(List.of(data.getValue()));
                return new SimpleDoubleProperty(totalHoodiePrice).asObject();
            });
        }
        return totalPriceColumn;
    }

    private void updateTotalPriceLabel(Transaction selectedTransaction) {
        if (selectedTransaction != null) {
            double totalPriceFromDatabase = getTotalPriceFromDatabase(selectedTransaction);

            Platform.runLater(() -> {
                showTotalPrice(totalPriceFromDatabase);
            });
        }
    }

    public double getTotalPriceFromDatabase(Transaction selectedTransaction) {
        double totalPriceFromDatabase = 0.0;

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement("SELECT TotalPrice FROM TransactionHeader WHERE TransactionID = ?")) {
            statement.setString(1, selectedTransaction.getTransactionID());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalPriceFromDatabase = resultSet.getDouble("TotalPrice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return totalPriceFromDatabase;
    }
    
    
}
