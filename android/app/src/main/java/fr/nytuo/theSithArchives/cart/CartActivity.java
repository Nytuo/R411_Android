package fr.nytuo.theSithArchives.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.productsList.CartList;

public class CartActivity extends AppCompatActivity implements CartAdapterListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();

        ListView listView = findViewById(R.id.cartListView);
        CartAdapter cartAdapter = new CartAdapter(this, CartList.getInstance());
        listView.setAdapter(cartAdapter);

        TextView totalPrice = findViewById(R.id.textViewTotal);
        totalPrice.setText("Total : " + CartList.getInstance().getTotalPrice() + "€");
    }

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public void onElementClick(int position) {

    }

    @Override
    public void onAddToCartClick(int position) {

    }
}
