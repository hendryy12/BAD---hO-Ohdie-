package model;

public class TransactionDetail {
    private String transactionID;
    private String hoodieID;
    private String hoodieName;
    private int quantity;
    private double totalPrice;
    private double hoodiePrice;
    
    public TransactionDetail() {

    }
    public TransactionDetail(String transactionID, String hoodieID, String hoodieName, int quantity, double totalPrice) {
        this.transactionID = transactionID;
        this.hoodieID = hoodieID;
        this.hoodieName = hoodieName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getHoodieID() {
        return hoodieID;
    }

    public void setHoodieID(String hoodieID) {
        this.hoodieID = hoodieID;
    }

    public String getHoodieName() {
        return hoodieName;
    }

    public void setHoodieName(String hoodieName) {
        this.hoodieName = hoodieName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public double getHoodiePrice() {
        return hoodiePrice;
    }

    public void setHoodiePrice(double hoodiePrice) {
        this.hoodiePrice = hoodiePrice;
    }
}
