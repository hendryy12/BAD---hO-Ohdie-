package view;

import controller.LoginController;
import controller.RegisterController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginView {
    private TextField usernameField;
    private PasswordField passwordField;
    private Scene scene;

    public void initialize(Stage primaryStage, LoginController loginController) {
        primaryStage.setTitle("Login Form");

        BorderPane borderPane = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu loginMenu = new Menu("Login");
        MenuItem registerItem = new MenuItem("Register");
        loginMenu.getItems().add(registerItem);
        menuBar.getMenus().add(loginMenu);
        borderPane.setTop(menuBar);

        Label loginLabel = new Label("Login");
        loginLabel.setFont(Font.font("Arial", 40));

        Label usernameLabel = new Label("Username:");

        Label passwordLabel = new Label("Password:");

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Button loginButton = new Button("Login");
        loginButton.setMinWidth(200);
        loginButton.setId("loginButton");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); 
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(8);
        grid.setHgap(10);

        grid.add(loginLabel, 0, 0, 2, 1); 
        grid.add(usernameLabel, 0, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(usernameField, 1, 1);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);

        GridPane.setMargin(loginLabel, new Insets(0, 0, 20, 100));

        borderPane.setCenter(grid);

        scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);

        registerItem.setOnAction(e -> handleRegister());
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Scene getScene() {
        return scene;
    }

    public void setLoginButtonHandler(Runnable handler) {
        Button loginButton = (Button) scene.lookup("#loginButton");
        loginButton.setOnAction(e -> handler.run());
    }

    private void handleRegister() {
        Stage currentStage = (Stage) usernameField.getScene().getWindow();
        currentStage.close();
        RegisterView registerView = new RegisterView();
        RegisterController registerController = new RegisterController(registerView);
        registerController.start(new Stage());
    }
    
    public void close() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

}