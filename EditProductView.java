package view;

import controller.EditProductController;
import controller.LoginController;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Hoodie;

public class EditProductView {
    private TableView<Hoodie> productTable;
    private TextField priceTextField;
    private TextField nameTextField;
    private Button updateButton;
    private Button deleteButton;
    private Label insertLabel;
    private Label nameLabelInsert;
    private Label priceLabelInsert;
    private TextField insertNameField;
    private TextField insertPriceField;
    private Button insertButton;
    private TextField hoodieIdTextField;

    public void initialize(Stage primaryStage, EditProductController controller) {
        primaryStage.setTitle("Edit Product");

        BorderPane borderPane = new BorderPane();

        Label editProductLabel = new Label("Edit Product");
        editProductLabel.setFont(Font.font("Arial", 20));

        Label updateDeleteLabel = new Label("Update & Delete Hoodie(s):");
        Label hoodieIdLabel = new Label("Hoodie ID:");
        nameLabelInsert = new Label("Name:"); 
        priceLabelInsert = new Label("Price:"); 

        hoodieIdTextField = new TextField();
        hoodieIdTextField.setDisable(true);
        nameTextField = new TextField();
        priceTextField = new TextField();

        updateButton = new Button("Update Price");
        deleteButton = new Button("Delete Hoodie");

        GridPane updateDeleteGrid = new GridPane();
        updateDeleteGrid.setAlignment(Pos.CENTER);
        updateDeleteGrid.setPadding(new Insets(20, 20, 20, 20));
        updateDeleteGrid.setVgap(8);
        updateDeleteGrid.setHgap(10);

        updateDeleteGrid.add(updateDeleteLabel, 0, 0, 2, 1);
        updateDeleteGrid.add(hoodieIdLabel, 0, 1);
        updateDeleteGrid.add(hoodieIdTextField, 1, 1);
        updateDeleteGrid.add(nameLabelInsert, 0, 2); 
        updateDeleteGrid.add(nameTextField, 1, 2);
        updateDeleteGrid.add(priceLabelInsert, 0, 3); 
        updateDeleteGrid.add(priceTextField, 1, 3);
        updateDeleteGrid.add(updateButton, 0, 4);
        updateDeleteGrid.add(deleteButton, 1, 4);

        VBox insertVBox = new VBox();
        insertVBox.setAlignment(Pos.CENTER);
        insertVBox.setPadding(new Insets(20, 20, 20, 20));
        insertVBox.setSpacing(8);

        insertLabel = new Label("Insert Hoodie:");
        nameLabelInsert = new Label("Name:"); 
        priceLabelInsert = new Label("Price:"); 

        insertNameField = new TextField();
        insertPriceField = new TextField();

        insertButton = new Button("Insert");

        GridPane insertGrid = new GridPane();
        insertGrid.setAlignment(Pos.CENTER);
        insertGrid.setVgap(8);
        insertGrid.setHgap(10);

        insertGrid.add(insertLabel, 0, 0, 2, 1);
        insertGrid.add(nameLabelInsert, 0, 1); 
        insertGrid.add(insertNameField, 1, 1);
        insertGrid.add(priceLabelInsert, 0, 2);
        insertGrid.add(insertPriceField, 1, 2);
        insertGrid.add(insertButton, 1, 3);

        insertButton.setOnAction(e -> controller.handleInsert());

        insertVBox.getChildren().addAll(insertGrid);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(20);
        hbox.getChildren().addAll(updateDeleteGrid, insertVBox);

        productTable = new TableView<>();
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Hoodie, String> idColumn = new TableColumn<>("Hoodie ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("hoodieID"));

        TableColumn<Hoodie, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoodieName"));

        TableColumn<Hoodie, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.getColumns().addAll(idColumn, nameColumn, priceColumn);

        productTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showProductDetails(newValue);
            } else {
                clearProductDetails();
            }
        });

        GridPane.setMargin(editProductLabel, new Insets(0, 0, 20, 0));

        borderPane.setTop(createMenuBar(primaryStage, controller));
        borderPane.setCenter(hbox);
        borderPane.setBottom(productTable);

        Scene scene = new Scene(borderPane, 1000, 600);
        primaryStage.setScene(scene);
    }

    private MenuBar createMenuBar(Stage primaryStage, EditProductController controller) {
        MenuBar menuBar = new MenuBar();
        Menu accountMenu = new Menu("Account");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> {
            Stage currentStage = (Stage) productTable.getScene().getWindow();
            currentStage.close();
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView);
            loginController.start(new Stage());
        });
        accountMenu.getItems().add(logoutItem);
        Menu adminMenu = new Menu("Admin");
        MenuItem editProductItem = new MenuItem("Edit Product");
        adminMenu.getItems().add(editProductItem);
        menuBar.getMenus().addAll(accountMenu, adminMenu);
        return menuBar;
    }

    private void showProductDetails(Hoodie selectedHoodie) {
        nameTextField.setText(selectedHoodie.getHoodieName());
        priceTextField.setText(String.valueOf(selectedHoodie.getPrice()));
        hoodieIdTextField.setText(selectedHoodie.getHoodieID());
        hoodieIdTextField.setDisable(true);
        nameTextField.setDisable(true);
    }

    public void clearProductDetails() {
        nameTextField.clear();
        priceTextField.clear();
    }

    public void setProductData(ObservableList<Hoodie> data) {
        productTable.setItems(data);
    }

    public String getNameInput() {
        return nameTextField.getText();
    }

    public int getPriceInput() {
        try {
            return Integer.parseInt(priceTextField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Hoodie getSelectedHoodie() {
        return productTable.getSelectionModel().getSelectedItem();
    }

    public void setUpdateButtonHandler(Runnable handler) {
        updateButton.setOnAction(e -> handler.run());
    }

    public void setDeleteButtonHandler(Runnable handler) {
        deleteButton.setOnAction(e -> handler.run());
    }

    public void close() {
        Stage stage = (Stage) productTable.getScene().getWindow();
        stage.close();
    }

    public String getInsertNameInput() {
        return insertNameField.getText();
    }

    public int getInsertPriceInput() {
        try {
            return Integer.parseInt(insertPriceField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void clearInsertFields() {
        insertNameField.clear();
        insertPriceField.clear();
    }
}