package fr.nytuo.theSithArchives.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.nytuo.theSithArchives.R;

public class CartActivity extends AppCompatActivity implements CartAdapterListener {
    CartAdapter cartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        ListView listView = findViewById(R.id.cartListView);
        cartAdapter = new CartAdapter(this, CartList.getInstance());
        listView.setAdapter(cartAdapter);
        cartAdapter.addListener(this);

        updateTotalPrice();
    }

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public void onPlusClick(int position) {
        CartList.getInstance().get(position).setQuantity(CartList.getInstance().get(position).getQuantity() + 1);
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    @Override
    public void onMoinClick(int position) {
        if (CartList.getInstance().get(position).getQuantity() > 1) {
            CartList.getInstance().get(position).setQuantity(CartList.getInstance().get(position).getQuantity() - 1);
            cartAdapter.notifyDataSetChanged();
        }
        else {
            CartList.getInstance().remove(position);
            cartAdapter.notifyDataSetChanged();
        }
        updateTotalPrice();

    }
    private void updateTotalPrice() {
        TextView totalPrice = findViewById(R.id.textViewTotal);
        totalPrice.setText("Total : " + CartList.getInstance().getTotalPrice() + "€");
    }

}
