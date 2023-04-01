package fr.nytuo.android411.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import fr.nytuo.android411.R;
import fr.nytuo.android411.productsList.ProductsListActivity;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

    }
}
