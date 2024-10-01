package controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.CartManager;
import model.Hoodie;
import model.HoodieManager;
import model.TransactionManager;
import model.User;
import view.CartView;
import view.HistoryView;
import view.HomeView;

public class HomeController {
    private HomeView homeView;
    private HoodieManager hoodieManager;
    private CartManager cartManager;
    private User loggedInUser;

    public HomeController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.hoodieManager = new HoodieManager();
        this.cartManager = new CartManager(); 
    }

    public void setHomeView(HomeView homeView) {
        this.homeView = homeView;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Home Form");

        homeView = new HomeView();
        homeView.initialize(primaryStage, this);

        homeView.setProductListData(hoodieManager.getAllHoodies());

        primaryStage.show();
    }

    public void addToCart(Hoodie selectedHoodie, int quantity) {
        String userID = loggedInUser.getUserID();

        boolean addToCartSuccess = cartManager.addToCart(userID, selectedHoodie.getHoodieID(), quantity);

        if (addToCartSuccess) {
            showAlert("Success", "Product added to cart successfully!");
        } else {
            showAlert("Error", "Failed to add product to cart. Please try again.");
        }
    }

    public void showProductDetails(Hoodie selectedHoodie) {
        homeView.showProductDetails(selectedHoodie);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void navigateToCart() {
        CartView cartView = new CartView();
        CartController cartController = new CartController(loggedInUser);
        cartController.setCartView(cartView);
        Stage cartStage = new Stage();
        cartController.start(cartStage);
        homeView.close();
    }
    
   
    public void navigateToHistory() {
        HistoryView historyView = new HistoryView();
        HistoryController historyController = new HistoryController(new TransactionManager(), loggedInUser.getUserID(), loggedInUser);
        historyController.start();
        homeView.close();
    }

    
}