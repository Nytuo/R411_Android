package fr.nytuo.theSithArchives.product;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;

import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.cart.CartActivity;
import fr.nytuo.theSithArchives.cart.CartList;
import fr.nytuo.theSithArchives.productsList.ProductsList;
import fr.nytuo.theSithArchives.productsList.ProductsListActivity;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Android411);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullview);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);


        TextView name = findViewById(R.id.name);
        ImageView image = findViewById(R.id.imageView);
        TextView description = findViewById(R.id.description);
        ConstraintLayout constraintLayout = findViewById(R.id.background);
        Button button = findViewById(R.id.button);
        TextView price = findViewById(R.id.price);
        TextView author = findViewById(R.id.authors);
        TextView publisher = findViewById(R.id.publisher);
        TextView isbn = findViewById(R.id.isbn);
        TextView date = findViewById(R.id.date);
        ScrollView scrollView = (ScrollView) findViewById(R.id.backgroundScroll);


        name.setText(ProductsList.getInstance().get(position).getName());
        ProductsList.getInstance().get(position).subToGetImgBitmap(this,image);

        description.setText(ProductsList.getInstance().get(position).getDescription());
        price.setText(ProductsList.getInstance().get(position).getPrice() + "€");
        StringBuilder authors = new StringBuilder();
        if (ProductsList.getInstance().get(position).getAuthors().size() == 1)
            authors.append(ProductsList.getInstance().get(position).getAuthors().get(0));
        else if (ProductsList.getInstance().get(position).getAuthors().size() == 2)
            authors.append(ProductsList.getInstance().get(position).getAuthors().get(0)).append(" et ").append(ProductsList.getInstance().get(position).getAuthors().get(1));
        else if (ProductsList.getInstance().get(position).getAuthors().size() > 2) {
            for (String authorE : ProductsList.getInstance().get(position).getAuthors()) {
                if (authorE.equals(ProductsList.getInstance().get(position).getAuthors().get(ProductsList.getInstance().get(position).getAuthors().size() - 1))) {
                    authors.append(authorE);
                } else
                    authors.append(authorE).append(", ");
            }
        }else{
            authors.append("Aucun auteur");
        }
        author.setText("Auteur: " + authors);
        publisher.setText("Éditeur: " + ProductsList.getInstance().get(position).getPublisher());
        isbn.setText("ISBN: " + ProductsList.getInstance().get(position).getIsbn());
        date.setText("Parution: " + ProductsList.getInstance().get(position).getDate());

        ProductsList.getInstance().get(position).subToGetImgBitmap(this, image, bitmap -> {
            Palette.from(ProductsList.getInstance().get(position).getImgBitmap()).generate(p -> {
                assert p != null;
                int vibrant = p.getVibrantColor(0x000000);
                int darkVibrant = p.getDarkVibrantColor(0x000000);
                int muted = p.getMutedColor(0x000000);
                int darkMuted = p.getDarkMutedColor(0x000000);
                int lightMuted = p.getLightMutedColor(0x000000);
                if (darkVibrant == Color.TRANSPARENT || darkVibrant == Color.WHITE) {
                    darkVibrant = Color.BLACK;
                }
                constraintLayout.setBackgroundColor(darkVibrant);
                scrollView.setBackgroundColor(darkVibrant);
                button.setBackgroundColor(muted);

                if (muted == darkVibrant || muted == darkMuted || muted == vibrant) {
                    button.setBackgroundColor(Color.CYAN);
                }

                if (isColorDark(darkVibrant)){
                    name.setTextColor(Color.WHITE);
                    description.setTextColor(Color.WHITE);
                    price.setTextColor(Color.WHITE);
                    author.setTextColor(Color.WHITE);
                    publisher.setTextColor(Color.WHITE);
                    isbn.setTextColor(Color.WHITE);
                    date.setTextColor(Color.WHITE);
                }

            });
        });

        button.setOnClickListener(v -> {
            button.setText("Ajouté");
            handleAddToCart(position);
        });

        Button button4 = findViewById(R.id.buttonHome);
        button4.setOnClickListener(v ->{
            Intent intent1 = new Intent(ProductActivity.this, ProductsListActivity.class);
            startActivity(intent1);
        });

        Button button3 = findViewById(R.id.buttonPanier);
        button3.setOnClickListener(v ->{
            Intent intent1 = new Intent(ProductActivity.this, CartActivity.class);
            startActivity(intent1);
        });

    }

    private boolean isColorDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5 && ColorUtils.calculateContrast(Color.WHITE, color) > 1.5;
    }

    private boolean isColorLight(int color) {
        return ColorUtils.calculateLuminance(color) > 0.5;
    }

    public void handleAddToCart(int position) {
        ProductsList.getInstance().get(position).setQuantity(1);
        CartList.getInstance().add(ProductsList.getInstance().get(position));
    }

}
