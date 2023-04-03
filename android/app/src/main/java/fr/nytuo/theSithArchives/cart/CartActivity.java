package fr.nytuo.theSithArchives.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.gps.MagasinSelectionActivity;
import fr.nytuo.theSithArchives.productsList.ProductsListActivity;

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

        Button button = findViewById(R.id.buttonPanier);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        Button button2 = findViewById(R.id.buttonHome);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductsListActivity.class);
            startActivity(intent);
        });

        Button button3 = findViewById(R.id.buttonConfirmer);
        button3.setOnClickListener(v -> {
            CartList.getInstance().clear();
            Intent intent = new Intent(this, MagasinSelectionActivity.class);
            startActivity(intent);
        });
        button3.setEnabled(!CartList.getInstance().isEmpty());

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
    public void onMoinsClick(int position) {
        if (CartList.getInstance().get(position).getQuantity() > 1) {
            CartList.getInstance().get(position).setQuantity(CartList.getInstance().get(position).getQuantity() - 1);
            cartAdapter.notifyDataSetChanged();
        }
        else {
            CartList.getInstance().remove(position);
            cartAdapter.notifyDataSetChanged();
        }
        updateTotalPrice();

        if (CartList.getInstance().isEmpty()) {
            Button button3 = findViewById(R.id.buttonConfirmer);
            button3.setEnabled(false);
        }

    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    private void updateTotalPrice() {
        TextView totalPrice = findViewById(R.id.textViewTotal);
        totalPrice.setText(getString(R.string.total) + CartList.getInstance().getTotalPrice() + "€");
    }

}
