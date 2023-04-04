package fr.nytuo.theSithArchives.retraitMagasin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.cart.CartActivity;
import fr.nytuo.theSithArchives.networking.HttpAsyncGet;
import fr.nytuo.theSithArchives.networking.PostExecuteActivity;
import fr.nytuo.theSithArchives.productsList.ProductsListActivity;

public class RetreiveCommandActivity extends AppCompatActivity implements PostExecuteActivity<Commande> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retreive_command);

        Button searchForCommand = findViewById(R.id.searchForCommand);
        searchForCommand.setOnClickListener(v -> {
            EditText editText = findViewById(R.id.editTextTextPersonName);
            String commandNumber = editText.getText().toString();
            new HttpAsyncGet<>("https://api.nytuo.fr/api/command/" + commandNumber, Commande.class, this, null);
        });

        Button buttonMagasinHome2 = findViewById(R.id.buttonHome_magasin2);
        Button buttonMagasinPanier2 = findViewById(R.id.buttonPanier_magasin2);
        Button buttonContact5 = findViewById(R.id.buttonContact5);
        buttonMagasinHome2.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductsListActivity.class);
            startActivity(intent);
        });

        buttonMagasinPanier2.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        buttonContact5.setOnClickListener(v -> {
            Intent intent = new Intent(this, RetreiveCommandActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public void onPostExecute(List<Commande> itemList) {
        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.textView2);
        TextView textView3 = findViewById(R.id.textView4);

        textView.setText(itemList.get(0).getCommandNumber());
        textView2.setText(itemList.get(0).getPrice());
        textView3.setText(itemList.get(0).getBooks());
    }
}
