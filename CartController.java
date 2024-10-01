package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.CartItem;
import model.CartManager;
import model.TransactionManager;
import model.User;
import model.UserManager;
import view.CartView;
import view.HistoryView;
import view.HomeView;
import view.PaymentConfirmationPopup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class CartController {
    private CartView cartView;
    private CartManager cartManager;
    private UserManager userManager;
    private User loggedInUser;
    private HomeView homeView;
    private Stage primaryStage;

    public CartController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.cartManager = new CartManager();
        this.userManager = new UserManager();
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    public void setHomeView(HomeView homeView) {
        this.homeView = homeView;
    }

    public void setCartView(CartView cartView) {
        this.cartView = cartView;
        cartView.setLoggedInUser(loggedInUser);
    }

    public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage; 
        primaryStage.setTitle("Cart Form");

        cartView = new CartView();
        cartView.initialize(primaryStage, this, loggedInUser);

        updateCartItems();

        updateContactInformation();

        primaryStage.show();
    }

    public void updateCartItems() {
        String userID = loggedInUser.getUserID();
        ObservableList<CartItem> cartItems = FXCollections.observableArrayList(cartManager.getCartItemsByUserID(userID));
        cartView.setCartItems(cartItems);
        updateCartTotalPrice();
    }

    public void updateContactInformation() {
        String userID = loggedInUser.getUserID();
        User user = userManager.getUserByID(userID);
        cartView.setContactInformation(user.getEmail(), user.getPhoneNumber(), user.getAddress());
    }

    public void removeFromCart(CartItem selectedCartItem) {
        String userID = loggedInUser.getUserID();
        boolean success = cartManager.removeFromCart(userID, selectedCartItem.getHoodieID());

        if (success) {
            updateCartItems();
        } else {
            System.out.println("Failed to remove item from cart.");
        }

        updateCartTotalPrice();
    }

    public double calculateTotalPrice() {
        ObservableList<CartItem> cartItems = cartView.getCartItems();
        double totalPrice = 0;

        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getTotalPrice();
        }

        return totalPrice;
    }
    
    public void showCartItemDetails(CartItem selectedItem) {
        cartView.showCartItemDetails(selectedItem);
    }
    
    public void logout() {

    }
    
    public void checkoutAction() {
        if (cartView.getCartItems().isEmpty()) {
            showErrorAlert("Error", "Cannot checkout. Cart is empty.");
        } else {
            boolean proceed = PaymentConfirmationPopup.display("Are you sure you want to proceed with the payment?");
            if (proceed) {
                boolean paymentSuccess = makePayment();
                if (paymentSuccess) {
                    showSuccessAlert("Payment Successful", "Thank you for your purchase!");
                    clearCartAndNavigateToHistory();
                } else {
                    showErrorAlert("Payment Failed", "Payment was not successful. Please try again.");
                }
            }
        }
    }



    private void clearCartAndNavigateToHistory() {
        String userID = loggedInUser.getUserID();
        cartManager.clearCart(userID); 
        updateCartItems(); 

    }
    
    public boolean makePayment() {
        String userID = loggedInUser.getUserID();
        double totalAmount = calculateTotalPrice();

        String transactionID = "TR" + String.format("%03d", cartManager.getTransactionIndex() + 1);

        ObservableList<CartItem> cartItems = cartView.getCartItems();

        boolean success = cartManager.insertTransaction(userID, transactionID, totalAmount, cartItems);

        if (success) {
            cartManager.clearCart(userID);

            System.out.println("Payment successful. Transaction ID: " + transactionID);

            return true;
        } else {
            System.out.println("Payment failed. Unable to insert transaction into the database.");
            return false;
        }
    }


    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void navigateToHome() {
        HomeController homeController = new HomeController(loggedInUser);
        homeController.start(new Stage());
        cartView.close();
    }


    public double getTotalPrice() {
        return 0.0;
    }
    
    private void updateCartTotalPrice() {
        double totalPrice = calculateTotalPrice();
        cartView.setCartTotalPrice(totalPrice);
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void navigateToHistory(User loggedInUser) {
        HistoryView historyView = new HistoryView();
        HistoryController historyController = new HistoryController(new TransactionManager(), loggedInUser.getUserID(), loggedInUser);
        historyController.start();
        primaryStage.close(); 
    }

}
