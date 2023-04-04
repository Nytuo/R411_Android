package fr.nytuo.theSithArchives.productsList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Objects;

import fr.nytuo.theSithArchives.networking.HttpAsyncGet;
import fr.nytuo.theSithArchives.networking.PostExecuteActivity;
import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.cart.CartActivity;
import fr.nytuo.theSithArchives.cart.CartList;
import fr.nytuo.theSithArchives.product.ProductActivity;
import fr.nytuo.theSithArchives.retraitMagasin.RetreiveCommandActivity;

/**
 * Activité de la liste des produits (principal)
 */
public class ProductsListActivity extends AppCompatActivity implements PostExecuteActivity<Product>, ProductAdapterListener {
    /**
     * Pop-up de chargement
     */
    private ProgressDialog progressDialog;
    /**
     * Delay pour le multi-click
     */
    private static final int DELAY_MILLIS = 500;
    /**
     * Nombre de click max pour le multi-click
     */
    private static final int MAX_CLICK_COUNT = 3;

    /**
     * Media player pour la musique
     */
    MediaPlayer mediaPlayer;

    /**
     * Compteur sur le nombre de click
     */
    private int clickCount = 0;
    /**
     * CountDownTimer pour le multi-click
     */
    private CountDownTimer clickTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        progressDialog = new ProgressDialog(this);
        new HttpAsyncGet<>("https://api.nytuo.fr/api/book/10", Product.class, this, null);
        this.progressDialog.setMessage("Chargement...");
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.show();
        SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onsubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        Button buttonHome = findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(onClickListener);

        Button buttonPanier = findViewById(R.id.buttonPanier);
        buttonPanier.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        Button buttonInfoCommande = findViewById(R.id.buttonContact);
        buttonInfoCommande.setOnClickListener(v -> {
            Intent intent = new Intent(this, RetreiveCommandActivity.class);
            startActivity(intent);
        });

    }


    /**
     * Listener du bouton home
     * Permet de lancer la musique si on clique X fois dessus et de revenir à l'activité principale
     */
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clickCount++;

            if (clickCount == 1) {
                clickTimer = new CountDownTimer(DELAY_MILLIS, DELAY_MILLIS) {
                    @Override
                    public void onTick(long millisUntilFinished) {}

                    @Override
                    public void onFinish() {
                        startActivity(new Intent(ProductsListActivity.this, ProductsListActivity.class));
                    }
                }.start();
            } else if (clickCount == MAX_CLICK_COUNT) {
                clickTimer.cancel();
                playSong();
                clickCount = 0;
            }
        }
    };

    /**
     * Lance la musique
     */
    private void playSong() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.maintheme);
        }else{
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this, R.raw.maintheme);
        }
        mediaPlayer.start();
    }

    private void onsubmit(String query){
        this.progressDialog.show();
        query = Uri.parse("https://api.nytuo.fr/api/book/search/" + query).buildUpon().build().toString();
        new HttpAsyncGet<>(query, Product.class, ProductsListActivity.this, null);
    }

    @Override
    public void onPostExecute(List<Product> itemList) {
        ListView listProduits = findViewById(R.id.listView);
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
        listProduits.startAnimation(animation);
        ProductsList.getInstance().clear();
        ProductsList.getInstance().addAll(itemList);
        Animation animation2 = android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        listProduits.startAnimation(animation2);
        this.progressDialog.dismiss();
        ProductsAdapter productsAdapter = new ProductsAdapter(this, ProductsList.getInstance());
        FlexibleProductImageDownloaderThread.flexibleProductImageDownloaderThread.addAdapter(productsAdapter, this);
        for (Product product : ProductsList.getInstance()){
            FlexibleProductImageDownloaderThread.flexibleProductImageDownloaderThread.add(product);
        }
        this.runOnUiThread(productsAdapter::notifyDataSetChanged);
        listProduits.setAdapter(productsAdapter);
        productsAdapter.addListener(this);
    }

    @Override
    public void onError() {
        this.progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("le serveur n'est pas joignable")
                .setTitle("Erreur");
        builder.setPositiveButton("OK", (dialog, id) -> {
            Intent intent = getIntent();
            startActivity(intent);
            finish();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);

    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onElementClick(int position) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onAddToCartClick(int position) {
        CartList.getInstance().add(ProductsList.getInstance().get(position));
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

}
