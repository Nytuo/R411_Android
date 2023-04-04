package fr.nytuo.thesitharchives.clickandcollect;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fr.nytuo.thesitharchives.R;
import fr.nytuo.thesitharchives.cart.CartActivity;
import fr.nytuo.thesitharchives.networking.HttpAsyncGet;
import fr.nytuo.thesitharchives.networking.PostExecuteActivity;
import fr.nytuo.thesitharchives.productslist.ProductsListActivity;

/**
 * Activité pour retrouver une commande d'après le numéro lors du passage de cette dernière
 */
public class RetreiveCommandActivity extends AppCompatActivity implements PostExecuteActivity<Commande> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retreive_command);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("commandNumber")) {
            String commandNumber = getIntent().getExtras().getString("commandNumber");
            if (commandNumber != null)
                Log.d("commandNumber", commandNumber);
            else Log.d("commandNumber", "null");
            new HttpAsyncGet<>("https://api.nytuo.fr/api/command/" + commandNumber, Commande.class, this, null);
        }
        Button searchForCommand = findViewById(R.id.searchForCommand);
        searchForCommand.setOnClickListener(v -> {
            EditText editText = findViewById(R.id.editTextCommand);
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
        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.textView2);
        TextView textView3 = findViewById(R.id.textView4);

        textView.setText(R.string.EnterCommandNumberHere);
        textView2.setEnabled(false);
        textView3.setEnabled(false);
    }

    @Override
    public void onPostExecute(List<Commande> itemList) {
        if (itemList != null && itemList.get(0) != null) {
            TextView textView = findViewById(R.id.textView);
            TextView textView2 = findViewById(R.id.textView2);
            TextView textView3 = findViewById(R.id.textView4);
            textView.setEnabled(true);
            textView2.setEnabled(true);
            textView3.setEnabled(true);
            textView.setText(String.format(RetreiveCommandActivity.this.getString(R.string.CommandNumber), itemList.get(0).getCommandNumber()));
            textView2.setText(String.format(RetreiveCommandActivity.this.getString(R.string.finishByEUR), Integer.parseInt(itemList.get(0).getPrice())));
            textView3.setText(String.format(RetreiveCommandActivity.this.getString(R.string.Books), itemList.get(0).getBooks()));
        } else {
            TextView textView = findViewById(R.id.textView);
            TextView textView2 = findViewById(R.id.textView2);
            TextView textView3 = findViewById(R.id.textView4);
            textView.setEnabled(true);
            textView2.setEnabled(true);
            textView3.setEnabled(true);
            textView.setText(R.string.NoCommandFound);
            textView2.setText("");
            textView3.setText("");
        }

    }

    @Override
    public void onError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("le serveur n'est pas joignable")
                .setTitle("Erreur");
        builder.setPositiveButton("OK", (dialog, id) -> {
            Intent intent = getIntent();
            startActivity(intent);
            finish();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
