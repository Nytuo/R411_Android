package fr.nytuo.android411.productsList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import fr.nytuo.android411.R;

public class ProductsListActivity extends AppCompatActivity implements ProductAdapterListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        ListView listProduits = (ListView) findViewById(R.id.listView);
        ProductsList pList = new ProductsList();


        ProductsAdapter adapter = new ProductsAdapter(this, pList);
        listProduits.setAdapter(adapter);
        adapter.addListener(this);

    }

    @Override
    public Context getContext() {
        return null;
    }
}
