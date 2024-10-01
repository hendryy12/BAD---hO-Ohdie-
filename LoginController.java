package controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.User;
import model.UserManager;
import view.HomeView;
import view.LoginView;

public class LoginController {
    private LoginView loginView;
    private UserManager userManager;
    private User loggedInUser;

    public LoginController(UserManager userManager) {
        this.userManager = userManager;
    }
    
    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.userManager = new UserManager();
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Form");

        loginView = new LoginView();
        loginView.initialize(primaryStage, this);
        loginView.setLoginButtonHandler(this::validateCredentials);

        primaryStage.show();
    }

    private void validateCredentials() {
        String username = loginView.getUsernameField().getText();
        String password = loginView.getPasswordField().getText();

        boolean isValidCredentials = userManager.validateCredentials(username, password);

        if (isValidCredentials) {
            loggedInUser = userManager.getUserByUsername(username);
            String role = loggedInUser.getRole();

            if ("User".equals(role)) {
                openHomeScene();
            } else if ("Admin".equals(role)) {
                openEditProductScene();
            }
        } else {
            showAlert("Invalid Credentials", "Username or Password is incorrect.");
        }
    }

    private void openHomeScene() {
        HomeController homeController = new HomeController(loggedInUser);
        homeController.start(new Stage());
        loginView.close();
    }

    private void openEditProductScene() {
        EditProductController editProductController = new EditProductController(loggedInUser);
        editProductController.start(new Stage());
        loginView.close();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}