package Solaris;

import java.util.Date;

public class Product {
    private int prodID;
    private String prodName;
    private String prodDesc;
    private String prodPrice;
    private String prodQuantity;
    private String prodCategory;
    private String manufacturer;
    private Date date;

    private String output;
    private String[] imgURLs;

    public Product(int id, String name, String desc, String price, String quantity, String cat, String prodManufacturer, String prodOutput) {
        prodID = id;
        prodName = name;
        prodDesc = desc;
        prodPrice = price;
        prodQuantity = quantity;
        prodCategory = cat;
        manufacturer = prodManufacturer;
        output = prodOutput;
    }

    public Product(int id, String name, double price, Date date) {
        prodID = id;
        prodName = name;
        prodPrice = String.valueOf(price);
        this.date = date;
    }

    public void setImgURLs(String[] path) {
        imgURLs = path;
    }

    public int getProdID() {
        return prodID;
    }

    public String getProdName() {
        return prodName;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public String getProdQuantity() {
        return prodQuantity;
    }

    public String getProdCategory() {
        return prodCategory;
    }

    public String getOutput() {
        return output;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String[] getImgURLs() {
        return imgURLs;
    }
}
