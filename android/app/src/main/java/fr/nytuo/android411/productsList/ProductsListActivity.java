package fr.nytuo.android411.productsList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import fr.nytuo.android411.HttpAsyncGet;
import fr.nytuo.android411.PostExecuteActivity;
import fr.nytuo.android411.R;
import fr.nytuo.android411.product.ProductActivity;

public class ProductsListActivity extends AppCompatActivity implements PostExecuteActivity<Product>, ProductAdapterListener {

    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        HttpAsyncGet<Product> httpAsyncGet = new HttpAsyncGet<Product>("https://api.nytuo.fr/api/book/5", Product.class, this, null);

        SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    query = URLEncoder.encode(query, "UTF-8");
                    HttpAsyncGet<Product> httpAsyncGet = new HttpAsyncGet<Product>("https://api.nytuo.fr/api/book/search/" + query, Product.class, ProductsListActivity.this, null);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Override
    public void onPostExecutePokemons(List<Product> itemList) {
        ProductsList.getInstance().clear();
        ProductsList.getInstance().addAll(itemList);
        ProductsAdapter productsAdapter = new ProductsAdapter(this, ProductsList.getInstance());
        ListView listProduits = findViewById(R.id.listView);
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
}
