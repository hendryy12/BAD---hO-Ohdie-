package controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Transaction;
import model.TransactionDetail;
import model.TransactionManager;
import model.User;
import view.CartView;
import view.HistoryView;

public class HistoryController {

    private HistoryView historyView;
    private TransactionManager transactionManager;
    private String currentUsername;
    private User loggedInUser;

    public HistoryController(TransactionManager transactionManager, String currentUsername, User loggedInUser) {
        this.transactionManager = transactionManager;
        this.currentUsername = currentUsername;
        this.loggedInUser = loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void start() {
        historyView = new HistoryView();
        historyView.initialize(this, currentUsername, loggedInUser);  

        bindTransactionTable();
        bindTransactionDetailTable();

        loadTransactions();
    }

    private void bindTransactionTable() {
        TableView<Transaction> transactionTable = historyView.getTransactionTable();
        TableColumn<Transaction, String> transactionIdColumn = new TableColumn<>("Transaction ID");
        TableColumn<Transaction, String> userIdColumn = new TableColumn<>("User ID");

        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));

        transactionTable.getColumns().addAll(transactionIdColumn, userIdColumn);

        transactionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                onTransactionSelected(newSelection);
            } else {
                onNoTransactionSelected();
            }
        });
    }

    private void bindTransactionDetailTable() {
        TableView<TransactionDetail> transactionDetailTable = historyView.getTransactionDetailTable();
        TableColumn<TransactionDetail, String> detailTransactionIdColumn = new TableColumn<>("Transaction ID");
        TableColumn<TransactionDetail, String> hoodieIdColumn = new TableColumn<>("Hoodie ID");
        TableColumn<TransactionDetail, String> hoodieNameColumn = new TableColumn<>("Hoodie Name");
        TableColumn<TransactionDetail, Integer> quantityColumn = new TableColumn<>("Quantity");
        TableColumn<TransactionDetail, Double> totalPriceColumn = new TableColumn<>("Total Price");

        detailTransactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        hoodieIdColumn.setCellValueFactory(new PropertyValueFactory<>("hoodieID"));
        hoodieNameColumn.setCellValueFactory(new PropertyValueFactory<>("hoodieName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    private void loadTransactions() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(
                transactionManager.getTransactionsByUserID(currentUsername)
        );

        historyView.getTransactionTable().setItems(transactions);
    }

    private void updateTotalPriceLabel(ObservableList<TransactionDetail> transactionDetails) {
        double total = transactionDetails.stream().mapToDouble(TransactionDetail::getTotalPrice).sum();
        historyView.getTotalLabel().setText("Total Price: " + total);
    }

    public void onNoTransactionSelected() {
        historyView.getTransactionIdLabel().setText("");
        historyView.getTransactionDetailTable().getItems().clear();
        historyView.getTotalLabel().setText("Total Price: ");
    }

    public void show() {
        historyView.show(this, currentUsername, loggedInUser);
    }

    public void close() {
        historyView.close();
    }

    public void onTransactionSelected(Transaction selectedTransaction) {
        String selectedTransactionID = selectedTransaction.getTransactionID();

        List<TransactionDetail> transactionDetails = transactionManager.getTransactionDetails(selectedTransactionID);

        double totalPrice = calculateTotalPrice(transactionDetails);

        historyView.showTransactionDetails(transactionDetails);
        historyView.showTotalPrice(totalPrice);
    }

    private double calculateTotalPrice(List<TransactionDetail> transactionDetails) {
        double totalPrice = 0.0;

        for (TransactionDetail transactionDetail : transactionDetails) {
            totalPrice += transactionDetail.getTotalPrice();
        }

        return totalPrice;
    }

    public void navigateToHome(User loggedInUser) {
        HomeController homeController = new HomeController(loggedInUser);
        homeController.start(new Stage());
        historyView.close();
    }

    public void navigateToCart() {
        CartView cartView = new CartView();
        CartController cartController = new CartController(loggedInUser); 
        cartController.setCartView(cartView);
        Stage cartStage = new Stage();
        cartController.start(cartStage);
        historyView.close();
    }

}
