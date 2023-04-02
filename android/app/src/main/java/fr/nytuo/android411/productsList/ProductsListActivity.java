package fr.nytuo.android411.productsList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fr.nytuo.android411.ProductAdapterListener;
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
    }

    

    @Override
    public void onPostExecutePokemons(List<Product> itemList) {
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
