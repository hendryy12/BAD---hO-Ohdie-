package controller;

import javafx.stage.Stage;
import model.Hoodie;
import model.HoodieManager;
import model.User;
import view.EditProductView;
import java.util.List;
import javafx.collections.FXCollections;

public class EditProductController {
    private EditProductView editProductView;
    private User loggedInUser;
    private HoodieManager hoodieManager;

    public EditProductController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.hoodieManager = new HoodieManager();
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Edit Product Form");

        editProductView = new EditProductView();
        editProductView.initialize(primaryStage, this);
        setButtonHandlers();

        updateProductTable();

        primaryStage.show();
    }

    private void setButtonHandlers() {
        editProductView.setUpdateButtonHandler(() -> handleUpdate());
        editProductView.setDeleteButtonHandler(() -> handleDelete());
    }
    
    public void handleInsert() {
        String name = editProductView.getInsertNameInput();
        int price = editProductView.getInsertPriceInput();

        Hoodie newHoodie = new Hoodie();
        newHoodie.setHoodieName(name);
        newHoodie.setPrice(price);

        boolean success = addHoodieToDatabase(newHoodie);

        if (success) {
            updateProductTable();
            editProductView.clearInsertFields();
        } else {
            System.out.println("Failed to add hoodie to the database.");
        }
    }

    private boolean addHoodieToDatabase(Hoodie hoodie) {
        return hoodieManager.addHoodieToDatabase(hoodie);
    }

    private void updateProductTable() {
        List<Hoodie> allHoodies = hoodieManager.getAllHoodies();
        editProductView.setProductData(FXCollections.observableArrayList(allHoodies));
    }

    public void handleUpdate() {
        Hoodie selectedHoodie = editProductView.getSelectedHoodie();
        int newPrice = editProductView.getPriceInput();

        boolean success = updateHoodie(selectedHoodie, newPrice);

        if (success) {
            updateProductTable();
            editProductView.clearProductDetails();
        } else {
            System.out.println("Failed to update hoodie price.");
        }
    }

    public void handleDelete() {
        Hoodie selectedHoodie = editProductView.getSelectedHoodie();

        boolean success = deleteHoodie(selectedHoodie);

        if (success) {
            updateProductTable();
            editProductView.clearProductDetails();
        } else {
            System.out.println("Failed to delete hoodie.");
        }
    }

    private boolean updateHoodie(Hoodie hoodie, int newPrice) {
        return hoodieManager.updateHoodie(hoodie, newPrice);
    }

    private boolean deleteHoodie(Hoodie hoodie) {
        return hoodieManager.deleteHoodie(hoodie);
    }
    
    
}