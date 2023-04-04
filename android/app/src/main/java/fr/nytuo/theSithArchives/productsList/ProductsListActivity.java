package fr.nytuo.theSithArchives.productsList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

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
        buttonHome.setOnClickListener(v -> {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });

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
        ProductsList.getInstance().get(position).setQuantity(1);
        CartList.getInstance().add(ProductsList.getInstance().get(position));
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }
}
