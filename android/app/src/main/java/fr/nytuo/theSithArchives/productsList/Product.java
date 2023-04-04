package fr.nytuo.theSithArchives.productsList;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fr.nytuo.theSithArchives.R;

/**
 * Classe représentant un produit
 */
public class Product {
    /**
     * Nom du produit
     */
    private String name;
    /**
     * Image du produit
     */
    private Bitmap imgBitmap;
    /**
     * Liste des URL des images du produit
     */
    private ArrayList<String> imgURLs;
    /**
     * Prix du produit
     */
    private int price;
    /**
     * Description du produit
     */
    private String description;
    /**
     * Liste des auteurs du produit
     */
    private ArrayList<String> authors;
    /**
     * Editeur du produit
     */
    private String publisher;
    /**
     * ISBN du produit
     */
    private String isbn;
    /**
     * Date de parution du produit
     */
    private String date;

    /**
     * Listener de l'activité
     */
    private AppCompatActivity listener;
    /**
     * ImageView de l'activité
     */
    private ImageView imageView;

    /**
     * Quantité du produit dans le panier
     */
    private int quantity = 0;
    /**
     * Event appelé après le chargement de l'image
     */
    private EventAfterImageLoad eventAfterImageLoad;

    public Product() {
    }

    /**
     * Définit le nom du produit
     * @param name Nom du produit
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Définit l'image du produit
     * @param imgURLs Liste des URL des images du produit
     */
    public void setImgURLs(ArrayList<String> imgURLs) {
        this.imgURLs = imgURLs;
    }

    /**
     * Définit le prix du produit
     * @param price Prix du produit
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Définit la description du produit
     * @param description Description du produit
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Définit la liste des auteurs du produit
     * @param authors   Liste des auteurs du produit
     */
    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    /**
     * Définit l'editeur du produit
     * @param publisher Editeur du produit
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Définit l'ISBN du produit
     * @param isbn ISBN du produit
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Définit la date de parution du produit
     * @param date Date de parution du produit
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Récupère la liste des auteurs du produit
     * @return Nom du produit
     */
    public String getName() {
        return name;
    }

    /**
     * Récupère l'image du produit
     * @param listener Listener de l'activité
     * @param imageView ImageView de l'activité
     */
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

    /**
     * Récupère l'image du produit
     * @param listener Listener de l'activité
     * @param imageView ImageView de l'activité
     * @param event Event appelé après le chargement de l'image
     */
    public void subToGetImgBitmap(AppCompatActivity listener, ImageView imageView, EventAfterImageLoad event) {
        this.eventAfterImageLoad = event;
        subToGetImgBitmap(listener, imageView);
    }

    /**
     * Récupère l'image du produit en bitmap
     * @param imgBitmap Image du produit
     */
    protected void receiveImgBitmap(Bitmap imgBitmap) {
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

    /**
     * Récupère la liste des URL des images du produit
     * @return Liste des URL des images du produit
     */
    public ArrayList<String> getImgURLs() {
        return imgURLs;
    }

    /**
     * Récupère le prix du produit
     * @return Prix du produit
     */
    public int getPrice() {
        return price;
    }

    /**
     * Récupère la description du produit
     * @return Description du produit
     */
    public String getDescription() {
        return description;
    }

    /**
     * Récupère la liste des auteurs du produit
     * @return Liste des auteurs du produit
     */
    public ArrayList<String> getAuthors() {
        return authors;
    }

    /**
     * Récupère l'editeur du produit
     * @return Editeur du produit
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Récupère l'ISBN du produit
     * @return ISBN du produit
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Récupère la date de parution du produit
     * @return Date de parution du produit
     */
    public String getDate() {
        return date;
    }

    /**
     * Définit l'image du produit
     * @param bitmap Image du produit
     */
    public void setImgBitmap(Bitmap bitmap) {
        imgBitmap = bitmap;
    }

    /**
     * Récupère la quantité du produit dans le panier
     * @return  Quantité du produit dans le panier
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Définit la quantité du produit dans le panier
     * @param quantity  Quantité du produit dans le panier
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Récupère l'image du produit
     * @return L'image du produit
     */
    public Bitmap getImgBitmap() {
        return imgBitmap;
    }
}
