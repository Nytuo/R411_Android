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

import fr.nytuo.theSithArchives.HttpAsyncGet;
import fr.nytuo.theSithArchives.PostExecuteActivity;
import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.cart.CartActivity;
import fr.nytuo.theSithArchives.cart.CartList;
import fr.nytuo.theSithArchives.product.ProductActivity;

public class ProductsListActivity extends AppCompatActivity implements PostExecuteActivity<Product>, ProductAdapterListener {


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        progressDialog = new ProgressDialog(this);
        HttpAsyncGet<Product> httpAsyncGet = new HttpAsyncGet<Product>("https://api.nytuo.fr/api/book/10", Product.class, this, null);
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
    }

    public void onsubmit(String query){
        this.progressDialog.show();
        query = Uri.parse("https://api.nytuo.fr/api/book/search/" + query).buildUpon().build().toString();
        HttpAsyncGet<Product> httpAsyncGet = new HttpAsyncGet<Product>(query, Product.class, ProductsListActivity.this, null);
    }

    @Override
    public void onPostExecutePokemons(List<Product> itemList) {
        ListView listProduits = findViewById(R.id.listView);
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
        listProduits.startAnimation(animation);
        ProductsList.getInstance().clear();
        ProductsList.getInstance().addAll(itemList);
        Animation animation2 = android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        listProduits.startAnimation(animation2);
        this.progressDialog.dismiss();
        ProductsAdapter productsAdapter = new ProductsAdapter(this, ProductsList.getInstance());
        FlexibleProductImageDonloaderTread.instance.addAdapter(productsAdapter, this);
        for (Product product : ProductsList.getInstance()){
            FlexibleProductImageDonloaderTread.instance.add(product);
        }
        this.runOnUiThread(() -> productsAdapter.notifyDataSetChanged());
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
        //on envois sure la vue ou l'on vois l'ariticle en grand
    }

    @Override
    public void onAddToCartClick(int position) {
        ProductsList.getInstance().get(position).setQuantity(1);
        CartList.getInstance().add(ProductsList.getInstance().get(position));
    }
}
