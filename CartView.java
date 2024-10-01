package view;

import controller.CartController;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.CartItem;
import model.User;

public class CartView {
    private TableView<CartItem> cartTable;
    private Label usernameLabel;
    private Label hoodieDetailLabel;
    private Label hoodieIdLabel;
    private Label hoodieNameLabel;
    private Label hoodiePriceLabel;
    private Label quantityLabel;
    private Label totalPriceLabel;
    private Button removeFromCartButton;
    private Label contactInformationLabel;
    private Label emailLabel;
    private Label phoneLabel;
    private Label addressLabel;
    private Label cartTotalPriceLabel;
    private User loggedInUser;
    private double totalCartPrice = 0.0;
    private Stage primaryStage;


    public void initialize(Stage primaryStage, CartController cartController, User loggedInUser) {
    	this.primaryStage = primaryStage; 
        this.loggedInUser = loggedInUser;
        primaryStage.setTitle("Cart View");

        BorderPane borderPane = new BorderPane();

        MenuBar menuBar = createMenuBar(primaryStage, cartController);
        
        usernameLabel = new Label("Username: " + loggedInUser.getUsername());
        usernameLabel.setFont(Font.font("Arial", 16));

        cartTable = new TableView<>();
        TableColumn<CartItem, String> idColumn = new TableColumn<>("Hoodie ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("hoodieID"));

        TableColumn<CartItem, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoodieName"));

        TableColumn<CartItem, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<CartItem, Double> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        cartTable.getColumns().addAll(idColumn, nameColumn, quantityColumn, totalPriceColumn);

        cartTable.setOnMouseClicked(e -> {
            CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
            cartController.showCartItemDetails(selectedItem);
        });

        hoodieDetailLabel = new Label("");
        hoodieIdLabel = new Label("");
        hoodieNameLabel = new Label("");
        hoodiePriceLabel = new Label("");
        quantityLabel = new Label("");
        totalPriceLabel = new Label("");
        removeFromCartButton = new Button("Remove from Cart");
        removeFromCartButton.setOnAction(e -> {
            CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                cartController.removeFromCart(selectedItem);
                clearCartItemDetails();
            }
        });
        
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(e -> cartController.checkoutAction());

        hoodieDetailLabel.setText("Hoodie's Detail:");
        Font detailFont = Font.font("Arial", FontWeight.BOLD, 18);
        hoodieDetailLabel.setFont(detailFont);
        hoodieIdLabel.setText("");
        hoodieNameLabel.setText("");
        hoodiePriceLabel.setText("");
        quantityLabel.setText("");
        totalPriceLabel.setText("");

        contactInformationLabel = new Label("Contact Information:");
        Font contactFont = Font.font("Arial", FontWeight.BOLD, 18);
        contactInformationLabel.setFont(contactFont);
        emailLabel = new Label("");
        phoneLabel = new Label("");
        addressLabel = new Label("");

        cartTotalPriceLabel = new Label("Cart Total Price: $0.0");
        Font totalFont = Font.font("Arial", FontWeight.BOLD, 20);
        cartTotalPriceLabel.setFont(totalFont);

        VBox hoodieDetailsVBox = new VBox(10,
                hoodieDetailLabel, hoodieIdLabel, hoodieNameLabel,
                hoodiePriceLabel, quantityLabel, totalPriceLabel, removeFromCartButton);

        VBox contactInfoVBox = new VBox(10, contactInformationLabel, emailLabel, phoneLabel, addressLabel, checkoutButton);

        VBox cartTotalPriceVBox = new VBox(10, cartTotalPriceLabel);

        HBox detailsAndContactHBox = new HBox(20, hoodieDetailsVBox, contactInfoVBox);

        VBox mainVBox = new VBox(20, detailsAndContactHBox, cartTotalPriceVBox, cartTable);

        VBox.setMargin(usernameLabel, new Insets(0, 0, 20, 0));

        borderPane.setTop(menuBar);
        borderPane.setCenter(mainVBox);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    private MenuBar createMenuBar(Stage primaryStage, CartController cartController) {
        MenuBar menuBar = new MenuBar();
        Menu accountMenu = new Menu("Account");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> cartController.logout());
        accountMenu.getItems().add(logoutItem);
        Menu cartMenu = new Menu("User");
        MenuItem homeItem = new MenuItem("Home");
        homeItem.setOnAction(e -> cartController.navigateToHome());
        cartMenu.getItems().add(homeItem);
        MenuItem cartItem = new MenuItem("Cart");
        cartMenu.getItems().add(cartItem);
        cartItem.setDisable(true);
        MenuItem historyItem = new MenuItem("History");
        cartMenu.getItems().add(historyItem);
        historyItem.setOnAction(event -> cartController.navigateToHistory(loggedInUser));

        menuBar.getMenus().addAll(accountMenu, cartMenu);

        return menuBar;
    }

    public void setCartData(ObservableList<CartItem> data) {
        cartTable.setItems(data);
        updateCartTotalPrice();
    }

    public void showCartItemDetails(CartItem selectedItem) {
        hoodieIdLabel.setText("Hoodie ID: " + selectedItem.getHoodieID());
        hoodieNameLabel.setText("Name: " + selectedItem.getHoodieName());
        hoodiePriceLabel.setText("Price: " + selectedItem.getHoodiePrice());
        quantityLabel.setText("Quantity: " + selectedItem.getQuantity());
        totalPriceLabel.setText("Total Price: " + selectedItem.getTotalPrice());
        removeFromCartButton.setDisable(false);
    }

    public void clearCartItemDetails() {
        hoodieIdLabel.setText("");
        hoodieNameLabel.setText("");
        hoodiePriceLabel.setText("");
        quantityLabel.setText("");
        totalPriceLabel.setText("");
        removeFromCartButton.setDisable(true);
        updateCartTotalPrice();
    }

    public void setContactInformation(String email, String phone, String address) {
        emailLabel.setText("Email: " + email);
        phoneLabel.setText("Phone: " + phone);
        addressLabel.setText("Address: " + address);
    }

    public void setCartTotalPrice(double totalPrice) {
        totalCartPrice = totalPrice;
        cartTotalPriceLabel.setText("Cart Total Price: $" + totalCartPrice);
    }

    public void setCartItems(ObservableList<CartItem> data) {
        cartTable.setItems(data);
    }

    public ObservableList<CartItem> getCartItems() {
        return cartTable.getItems();
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;

        if (usernameLabel != null) {
            usernameLabel.setText("Username: " + loggedInUser.getUsername());
        }
    }
    
    private void updateCartTotalPrice() {
        ObservableList<CartItem> cartItems = getCartItems();
        double totalPrice = 0.0;

        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getTotalPrice();
        }

        setCartTotalPrice(totalPrice);
    }
    
    public void close() {
        Stage stage = (Stage) cartTable.getScene().getWindow();
        stage.close();
    }

}
