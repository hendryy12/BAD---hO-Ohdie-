package view;

import controller.CartController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PaymentConfirmationPopup {

    public static boolean display(String confirmationText) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Payment Confirmation");

        Label confirmationLabel = new Label(confirmationText);

        Button makePaymentButton = new Button("Make Payment");
        boolean[] result = { false };

        makePaymentButton.setOnAction(e -> {
            result[0] = true;
            popupStage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> popupStage.close());

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(confirmationLabel, makePaymentButton, cancelButton);

        Scene scene = new Scene(layout, 300, 200);
        popupStage.setScene(scene);
        popupStage.showAndWait();

        return result[0]; 
    }
}
