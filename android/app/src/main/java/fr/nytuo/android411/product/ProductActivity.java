package fr.nytuo.android411.product;

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

import fr.nytuo.android411.R;
import fr.nytuo.android411.productsList.ProductsList;

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
        image.setImageBitmap(ProductsList.getInstance().get(position).getImgBitmapIndex(0));
        description.setText(ProductsList.getInstance().get(position).getDescription());
        price.setText(ProductsList.getInstance().get(position).getPrice() + "€");
        StringBuilder authors = new StringBuilder();
        for (String authorE : ProductsList.getInstance().get(position).getAuthors()) {
            if (authorE.equals(ProductsList.getInstance().get(position).getAuthors().get(ProductsList.getInstance().get(position).getAuthors().size() - 1))) {
                authors.append(authorE);
            } else
                authors.append(authorE).append(", ");
        }
        author.setText("Auteur: " + authors);
        publisher.setText("Éditeur: " + ProductsList.getInstance().get(position).getPublisher());
        isbn.setText("ISBN: " + ProductsList.getInstance().get(position).getIsbn());
        date.setText("Parution: " + ProductsList.getInstance().get(position).getDate());


        Palette.from(ProductsList.getInstance().get(position).getImgBitmapIndex(0)).generate(p -> {
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

        button.setOnClickListener(v -> handleAddToCart());


    }

    private boolean isColorDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5 && ColorUtils.calculateContrast(Color.WHITE, color) > 1.5;
    }

    private boolean isColorLight(int color) {
        return ColorUtils.calculateLuminance(color) > 0.5;
    }

    public void handleAddToCart() {
        //TODO
    }

}
