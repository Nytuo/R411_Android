package fr.nytuo.android411;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import fr.nytuo.android411.gps.MagasinSlectionActibity;
import fr.nytuo.android411.productsList.ProductsListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProductsListActivity.class);
            startActivity(intent);
        });


    }
}