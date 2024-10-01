package view;

import controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.UserManager;

public class RegisterView {
    private TextField emailField;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField phoneNumberField;
    private ToggleGroup genderToggleGroup;
    private TextArea addressArea;
    private CheckBox agreeTermsCheckBox;
    private Button registerButton;
    private Stage stage;

    public void initialize(Stage primaryStage) {
        this.stage = primaryStage;

        primaryStage.setTitle("Register Form");

        BorderPane borderPane = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu registerMenu = new Menu("Register");
        MenuItem loginItem = new MenuItem("Login");
        registerMenu.getItems().add(loginItem);
        menuBar.getMenus().add(registerMenu);
        borderPane.setTop(menuBar);

        Label registerLabel = new Label("Register");
        registerLabel.setFont(Font.font("Arial", 40));

        Label emailLabel = new Label("Email:");
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label confirmPasswordLabel = new Label("Confirm Password:");
        Label phoneNumberLabel = new Label("Phone Number:");
        Label genderLabel = new Label("Gender:");
        Label addressLabel = new Label("Address:");

        emailField = new TextField();
        emailField.setPromptText("Enter your email");

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");

        phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Enter your phone number");

        RadioButton maleRadioButton = new RadioButton("Male");
        RadioButton femaleRadioButton = new RadioButton("Female");
        genderToggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);

        addressArea = new TextArea();
        addressArea.setPromptText("Enter your address");

        agreeTermsCheckBox = new CheckBox("Agree to Terms & Conditions");

        registerButton = new Button("Register");
        registerButton.setMinWidth(200);
        registerButton.setId("registerButton");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(8);
        grid.setHgap(10);

        grid.add(registerLabel, 0, 0, 2, 1);
        grid.add(emailLabel, 0, 1);
        grid.add(usernameLabel, 0, 2);
        grid.add(passwordLabel, 0, 3);
        grid.add(confirmPasswordLabel, 0, 4);
        grid.add(phoneNumberLabel, 0, 5);
        grid.add(genderLabel, 0, 6);
        grid.add(addressLabel, 0, 8);
        grid.add(emailField, 1, 1);
        grid.add(usernameField, 1, 2);
        grid.add(passwordField, 1, 3);
        grid.add(confirmPasswordField, 1, 4);
        grid.add(phoneNumberField, 1, 5);
        grid.add(maleRadioButton, 1, 6);
        grid.add(femaleRadioButton, 1, 7);
        grid.add(addressArea, 1, 8);
        grid.add(agreeTermsCheckBox, 1, 9);
        grid.add(registerButton, 1, 10);

        GridPane.setMargin(registerLabel, new Insets(0, 0, 20, 250));

        borderPane.setCenter(grid);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);

        loginItem.setOnAction(e -> handleLogin());
    }

    public TextField getEmailField() {
        return emailField;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public PasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public TextField getPhoneNumberField() {
        return phoneNumberField;
    }

    public ToggleGroup getGenderToggleGroup() {
        return genderToggleGroup;
    }

    public TextArea getAddressArea() {
        return addressArea;
    }

    public CheckBox getAgreeTermsCheckBox() {
        return agreeTermsCheckBox;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Stage getStage() {
        return stage;
    }

    private void handleLogin() {
        stage.close();

        LoginView loginView = new LoginView();
        LoginController loginController = new LoginController(new UserManager()); 
        Stage loginStage = new Stage();
        loginController.start(loginStage);
    }

}