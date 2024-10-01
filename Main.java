package application;

import javafx.application.Application;
import javafx.stage.Stage;
import model.UserManager;
import controller.LoginController;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            UserManager userManager = new UserManager();
            LoginController loginController = new LoginController(userManager);
            loginController.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}