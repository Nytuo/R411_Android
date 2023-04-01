package fr.nytuo.android411.productsList;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Product {
    private String name;
    private final ArrayList<Bitmap> imgBitmap=new ArrayList<Bitmap>();
    private ArrayList<String> imgURLs;
    private int price;
    private String description;
    private ArrayList<String> authors;
    private String publisher;
    private String isbn;
    private String date;

    public Product() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgURLs(ArrayList<String> imgURLs) {
        this.imgURLs = imgURLs;
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

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public ArrayList<String> getImgURLs() {
        return imgURLs;
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

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDate() {
        return date;
    }
}
