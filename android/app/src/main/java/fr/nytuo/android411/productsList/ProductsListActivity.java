package fr.nytuo.android411.productsList;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.nytuo.android411.Gps.PositionMagasin;
import fr.nytuo.android411.HttpAsyncGet;
import fr.nytuo.android411.PostExecuteActivity;
import fr.nytuo.android411.R;

public class ProductsListActivity extends AppCompatActivity implements PostExecuteActivity,ProductAdapterListener {

    Product[] products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_layout);

        HttpAsyncGet<Product> httpAsyncGet = new HttpAsyncGet<Product>("https://api.jsonserve.com/ZjoC6h", Product.class, this, null);
    }


    @Override
    public void onPostExecutePokemons(List itemList) {
        products = (Product[]) itemList.toArray();
        ProductsAdapter productsAdapter = new ProductsAdapter(this, new ArrayList<Product>(Arrays.asList(products)));

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public Context getContext() {
        return null;
    }
}
