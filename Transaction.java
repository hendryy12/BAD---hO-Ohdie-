// Transaction.java
package model;

public class Transaction {
    private String transactionID;
    private String userID;

    public Transaction() {
    }

    public Transaction(String transactionID, String userID) {
        this.transactionID = transactionID;
        this.userID = userID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
