package fr.nytuo.theSithArchives.productslist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.BaseAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Thread permettant de télécharger les images des produits
 */
public class FlexibleProductImageDownloaderThread implements Runnable {

    /**
     * Instance du thread
     */
    public static FlexibleProductImageDownloaderThread flexibleProductImageDownloaderThread = new FlexibleProductImageDownloaderThread();
    /**
     * Liste des adapters à mettre à jour
     */
    private final HashMap<BaseAdapter, AppCompatActivity> baseAdapterAppCompatActivityHashMap = new HashMap<>();
    /**
     * File d'attente des produits à télécharger
     */
    private final BlockingQueue<Product> blockingQueue = new LinkedBlockingDeque<>();

    /**
     * Constructeur du thread
     */
    public FlexibleProductImageDownloaderThread() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Ajoute un adapter à mettre à jour
     *
     * @param adapter  Adapter à mettre à jour
     * @param activity Activité
     */
    public void addAdapter(BaseAdapter adapter, AppCompatActivity activity) {
        baseAdapterAppCompatActivityHashMap.put(adapter, activity);
    }

    /**
     * Supprime un adapter
     *
     * @param adapter Adapter à supprimer
     */
    public void removeAdapter(BaseAdapter adapter) {
        baseAdapterAppCompatActivityHashMap.remove(adapter);
    }

    /**
     * Boucle du thread permettant de télécharger les images
     */
    @Override
    public void run() {
        boolean interrupted = false;
        while (!interrupted) {
            try {
                Product product = blockingQueue.take();
                Bitmap image = null;
                try {
                    // Téléchargement de l'image
                    URL url = new URL(product.getImgURLs().get(0));
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    Log.e("FlexibleProductImageDownloaderThread", "Erreur lors du téléchargement de l'image", e);
                }
                // Mise à jour de l'image
                product.receiveImgBitmap(image);
                baseAdapterAppCompatActivityHashMap.forEach((key, value) -> value.runOnUiThread(key::notifyDataSetChanged));
            } catch (InterruptedException e) {
                interrupted = true;
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Ajoute un produit à télécharger
     *
     * @param product Produit à télécharger
     */
    public void add(Product product) {
        blockingQueue.add(product);
    }


}
