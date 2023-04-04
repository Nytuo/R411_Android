package fr.nytuo.theSithArchives.productsList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.BaseAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class FlexibleProductImageDownloaderThread implements Runnable{

    private final HashMap<BaseAdapter,AppCompatActivity> adapters= new HashMap<>();
    private final BlockingQueue<Product> blockingQueue = new LinkedBlockingDeque<>();

    public static FlexibleProductImageDownloaderThread instance = new FlexibleProductImageDownloaderThread();

    public FlexibleProductImageDownloaderThread() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
    public void addAdapter(BaseAdapter adapter, AppCompatActivity activity) {
        adapters.put(adapter, activity);
    }
    public void removeAdapter(BaseAdapter adapter) {
        adapters.remove(adapter);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Product product = blockingQueue.take();

                    Bitmap image = null;
                    try {
                        URL url = new URL(product.getImgURLs().get(0));
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch(IOException e) {
                        e.printStackTrace();
                    }

                product.resiveImgBitmap(image);
                adapters.forEach((key, value) -> value.runOnUiThread(key::notifyDataSetChanged));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void add(Product product) {
        blockingQueue.add(product);
    }


}