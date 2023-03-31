package fr.nytuo.android411;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import fr.nytuo.android411.Gps.MagasinSlectionActibity;
import fr.nytuo.android411.productsList.ProductsListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MagasinSlectionActibity.class);
        startActivity(intent);
    }
}