package fr.nytuo.android411.productsList;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Product {
    private String name;
    private final ArrayList<Bitmap> imgBitmap=new ArrayList<Bitmap>();
    private ArrayList<String> imgURL;
    private int price;
    private String description;
    private ArrayList<String> authors;
    private ArrayList<String> publishers;
    private String ISBN;
    private String date;

    public Product() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgURL(ArrayList<String> imgURL) {
        this.imgURL = imgURL;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setPublishers(ArrayList<String> publishers) {
        this.publishers = publishers;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Bitmap> getImgBitmap() {
        return imgBitmap;
    }

    public ArrayList<String> getImgURL() {
        return imgURL;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public ArrayList<String> getPublishers() {
        return publishers;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getDate() {
        return date;
    }
}
