package fr.nytuo.theSithArchives.productsList;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.cart.CartAdapterListener;

public class Product {
    private String name;
    private Bitmap imgBitmap;
    private ArrayList<String> imgURLs;
    private int price;
    private String description;
    private ArrayList<String> authors;
    private String publisher;
    private String isbn;
    private String date;

    private AppCompatActivity listener;
    private ImageView imageView;

    private int quantity = 0;
    private EventAfterImageLoad eventAfterImageLoad;

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

    public void subToGetImgBitmap(AppCompatActivity listener, ImageView imageView) {
        if (imgBitmap == null) {
            imageView.setImageResource(R.drawable.no_cover);
            this.listener = listener;
            this.imageView = imageView;
        } else {
            imageView.setImageBitmap(imgBitmap);
            if (eventAfterImageLoad != null) {
                eventAfterImageLoad.onImageLoad(imgBitmap);
            }
        }
    }
    public void subToGetImgBitmap(AppCompatActivity listener, ImageView imageView, EventAfterImageLoad event) {
        this.eventAfterImageLoad = event;
        subToGetImgBitmap(listener, imageView);
    }

    protected void resiveImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
        if (listener != null) {
            listener.runOnUiThread(() -> {
                imageView.setImageBitmap(imgBitmap);
                if (eventAfterImageLoad != null) {
                    eventAfterImageLoad.onImageLoad(imgBitmap);
                }
            }
            );
        }
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

    public void setImgBitmap(Bitmap s) {
        imgBitmap = s;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }
}
