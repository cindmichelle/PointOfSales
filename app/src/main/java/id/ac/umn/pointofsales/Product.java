package id.ac.umn.pointofsales;

import java.io.Serializable;

public class Product implements Serializable{
    private String id;
    private String name;
    private int price;
    private String imageUrl;
    private int qty;

    public Product(){}

    public Product(String name, String imageUrl, int price) {
        this.id = "";
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.qty = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void minusQty() {
        this.qty--;
    }

    public void plusQty(){
        this.qty++;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
