package view;

import controller.HomeController;
import controller.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Hoodie;

import java.util.List;

public class HomeView {
    private TableView<Hoodie> productList;
    private Spinner<Integer> quantitySpinner;
    private Button addToCartButton;
    private Stage stage;
    private Hoodie selectedHoodie;
    private Label hoodieDetailLabel;
    private Label hoodieIdLabel;
    private Label hoodieNameLabel;
    private Label hoodiePriceLabel;

    public void initialize(Stage primaryStage, HomeController homeController) {
        this.stage = primaryStage;
        primaryStage.setTitle("Home View");
        BorderPane borderPane = new BorderPane();
        MenuBar menuBar = createMenuBar(primaryStage, homeController);
        hoodieDetailLabel = new Label("Hoodie's Detail:");
        hoodieDetailLabel.setFont(Font.font("Arial", 20));
        hoodieIdLabel = new Label("Hoodie ID: ");
        hoodieNameLabel = new Label("Name: ");
        hoodiePriceLabel = new Label("Price: ");
        quantitySpinner = new Spinner<>(1, 10, 1);

        addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(e -> homeController.addToCart(selectedHoodie, getQuantitySpinner().getValue()));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(hoodieDetailLabel, 0, 0, 2, 1);
        grid.add(new Label("Hoodie ID:"), 0, 1);
        grid.add(hoodieIdLabel, 1, 1);
        grid.add(new Label("Name:"), 0, 2);
        grid.add(hoodieNameLabel, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(hoodiePriceLabel, 1, 3);
        grid.add(new Label("Quantity:"), 0, 4);
        grid.add(quantitySpinner, 1, 4);
        grid.add(addToCartButton, 0, 5, 2, 1);

        productList = new TableView<>();
        TableColumn<Hoodie, String> idColumn = new TableColumn<>("Hoodie ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("hoodieID"));

        TableColumn<Hoodie, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoodieName"));

        productList.getColumns().addAll(idColumn, nameColumn);

        productList.setOnMouseClicked(e -> {
            selectedHoodie = productList.getSelectionModel().getSelectedItem();
            homeController.showProductDetails(selectedHoodie);
        });

        GridPane.setMargin(hoodieDetailLabel, new Insets(0, 0, 20, 0));

        borderPane.setTop(menuBar);
        borderPane.setLeft(productList);
        borderPane.setCenter(grid);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
    }

    private MenuBar createMenuBar(Stage primaryStage, HomeController homeController) {
        MenuBar menuBar = new MenuBar();
        Menu accountMenu = new Menu("Account");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> {
            Stage currentStage = (Stage) productList.getScene().getWindow();
            currentStage.close();
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView);
            loginController.start(new Stage());
        });
        accountMenu.getItems().add(logoutItem);
        Menu userMenu = new Menu("User");

        MenuItem homeItem = new MenuItem("Home");
        homeItem.setDisable(true);
        homeItem.setOnAction(e -> {
         });

        MenuItem cartItem = new MenuItem("Cart");
        cartItem.setOnAction(e -> homeController.navigateToCart());

        MenuItem historyItem = new MenuItem("History");
        historyItem.setOnAction(event -> homeController.navigateToHistory());

        userMenu.getItems().addAll(homeItem, cartItem, historyItem);
        menuBar.getMenus().addAll(accountMenu, userMenu);
        return menuBar;
    }

    public void setProductListData(List<Hoodie> data) {
        ObservableList<Hoodie> observableData = FXCollections.observableArrayList(data);
        productList.setItems(observableData);
    }

    public Spinner<Integer> getQuantitySpinner() {
        return quantitySpinner;
    }

    public void close() {
        stage.close();
    }

    public void showProductDetails(Hoodie selectedHoodie) {
        if (selectedHoodie != null) {
            hoodieIdLabel.setText(selectedHoodie.getHoodieID());
            hoodieNameLabel.setText(selectedHoodie.getHoodieName());
            hoodiePriceLabel.setText("$" + selectedHoodie.getPrice());
        } else {
            hoodieIdLabel.setText("");
            hoodieNameLabel.setText("");
            hoodiePriceLabel.setText("");
        }
    }
}