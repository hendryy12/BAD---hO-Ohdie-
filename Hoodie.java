package model;

public class Hoodie {
    private String hoodieID;
    private String hoodieName;
    private int price;

    public Hoodie() {
    }

    public Hoodie(String hoodieID, String hoodieName, int price) {
        this.hoodieID = hoodieID;
        this.hoodieName = hoodieName;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Hoodie{" +
                "hoodieID='" + hoodieID + '\'' +
                ", hoodieName='" + hoodieName + '\'' +
                ", price=" + price +
                '}';
    }
}