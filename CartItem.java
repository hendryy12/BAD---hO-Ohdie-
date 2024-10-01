package model;

public class CartItem {
    private String userID;
    private String hoodieID;
    private int quantity;

    private String hoodieName;
    private int hoodiePrice;

    public CartItem() {
    }

    public CartItem(String userID, String hoodieID, int quantity) {
        this.userID = userID;
        this.hoodieID = hoodieID;
        this.quantity = quantity;
    }

    public String getUserID() {
        return userID;
    }

    public String getHoodieID() {
        return hoodieID;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getHoodieName() {
        return hoodieName;
    }

    public int getHoodiePrice() {
        return hoodiePrice;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setHoodieID(String hoodieID) {
        this.hoodieID = hoodieID;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setHoodieName(String hoodieName) {
        this.hoodieName = hoodieName;
    }

    public void setHoodiePrice(int hoodiePrice) {
        this.hoodiePrice = hoodiePrice;
    }
    
    public double getTotalPrice() {
        return quantity * hoodiePrice;
    }
}
