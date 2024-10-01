package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;
import model.UserManager;
import view.LoginView;
import view.RegisterView;
import javafx.scene.control.RadioButton;

public class RegisterController {
    private RegisterView registerView;
    private UserManager userManager;

    public RegisterController(RegisterView registerView) {
        this.registerView = registerView;
        this.userManager = new UserManager(); 
    }

    public void start(Stage primaryStage) {
        registerView.initialize(primaryStage);
        registerView.getRegisterButton().setOnAction(e -> validateRegistration());
        primaryStage.show();
    }

    private void validateRegistration() {
        String email = registerView.getEmailField().getText();
        String username = registerView.getUsernameField().getText();
        String password = registerView.getPasswordField().getText();
        String confirmPassword = registerView.getConfirmPasswordField().getText();
        String phoneNumber = registerView.getPhoneNumberField().getText();
        String gender = null;
        Toggle selectedToggle = registerView.getGenderToggleGroup().getSelectedToggle();

        if (selectedToggle instanceof RadioButton) {
            gender = ((RadioButton) selectedToggle).getText();
        }

        String address = registerView.getAddressArea().getText();
        boolean agreeTerms = registerView.getAgreeTermsCheckBox().isSelected();

        if (email.isEmpty() || !email.endsWith("@hoohdie.com")) {
            showAlert("Error", "Invalid email format. Please use '@hoohdie.com'");
            return;
        }

        if (username.isEmpty()) {
            showAlert("Error", "Username cannot be empty");
            return;
        }

        if (password.length() < 5) {
            showAlert("Error", "Password must contain at least 5 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match");
            return;
        }

        if (phoneNumber.length() != 14 || !phoneNumber.startsWith("+62")) {
            showAlert("Error", "Invalid phone number format. It must be +62XXXXXXXXXXXX");
            return;
        }

        if (gender == null) {
            showAlert("Error", "Please select your gender");
            return;
        }

        if (address.isEmpty()) {
            showAlert("Error", "Address cannot be empty");
            return;
        }

        if (!agreeTerms) {
            showAlert("Error", "Please agree to the terms and conditions");
            return;
        }

        if (email.isEmpty() || !email.endsWith("@hoohdie.com") || username.isEmpty() || password.length() < 5
                || !password.equals(confirmPassword) || phoneNumber.length() != 14 || !phoneNumber.startsWith("+62")
                || gender == null || address.isEmpty() || !agreeTerms) {
            return;
        }

        String generatedUserId = generateUserId();

        boolean registrationSuccess = userManager.saveUser(generatedUserId, username, password, email, gender, phoneNumber, address, "User");

        if (registrationSuccess) {
            showRegistrationSuccessAlert();
            registerView.getStage().close();
            showLoginScene();
        } else {
            showAlert("Error", "Registration failed. Please try again.");
        }
    }

    
    private String generateUserId() {
        int userIndex = userManager.getUserCount() + 1;
        return "US" + String.format("%03d", userIndex);
    }

    private void showLoginScene() {
        LoginView loginView = new LoginView();
        LoginController loginController = new LoginController(new UserManager()); 
        Stage loginStage = new Stage();
        loginController.start(loginStage);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showRegistrationSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("Registration successful!");
        alert.showAndWait();
    }
}