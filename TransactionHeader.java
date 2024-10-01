package model;

import java.sql.Date;

public class TransactionHeader {
    private String transactionID;
    private String userID;
    private Date date;
    private int totalPrice;

    public TransactionHeader(String transactionID, String userID) {
        this.transactionID = transactionID;
        this.userID = userID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getUserID() {
        return userID;
    }
}
