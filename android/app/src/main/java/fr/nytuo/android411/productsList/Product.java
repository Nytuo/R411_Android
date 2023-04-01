package fr.nytuo.android411.productsList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Product {
    private String name;
    private ArrayList<Bitmap> imgBitmap;
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
        // creat a new array for bitmap the same size as imgURLs
        imgBitmap = new ArrayList<Bitmap>();
        for (int i = 0; i < imgURLs.size(); i++) {
                //todo get image from url
                Bitmap image = null;
                try {
                    URL url = new URL(imgURLs.get(i));
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch(IOException e) {
                    e.printStackTrace();
                }
                imgBitmap.add(image);

        }
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

    public Bitmap getImgBitmapIndex(int index) {

        return imgBitmap.get(index);
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
