package fr.nytuo.android411.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import fr.nytuo.android411.R;
import fr.nytuo.android411.productsList.ProductsList;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        author.setText("Auteur(s): " + authors);
        publisher.setText("Éditeur: " + ProductsList.getInstance().get(position).getPublisher());
        isbn.setText("ISBN: "+ProductsList.getInstance().get(position).getIsbn());
        date.setText("Parution: "+ProductsList.getInstance().get(position).getDate());



        Palette.from(ProductsList.getInstance().get(position).getImgBitmapIndex(0)).generate(p -> {
            int vibrant = p.getVibrantColor(0x000000);
            int darkVibrant = p.getDarkVibrantColor(0x000000);
            int muted = p.getMutedColor(0x000000);
            int darkMuted = p.getDarkMutedColor(0x000000);
            int lightMuted = p.getLightMutedColor(0x000000);
            constraintLayout.setBackgroundColor(darkVibrant);
            button.setBackgroundColor(muted);

            if (darkVibrant == muted) {
                System.out.println("darkVibrant == muted");
                button.setBackgroundColor(lightMuted);
            }

            if (darkVibrant == 0) {
                name.setTextColor(0xFF000000);
                description.setTextColor(0xFF000000);
                price.setTextColor(0xFF000000);
                author.setTextColor(0xFF000000);
                publisher.setTextColor(0xFF000000);
                isbn.setTextColor(0xFF000000);
                date.setTextColor(0xFF000000);
            }else if (darkVibrant == 0xFF000000) {
                name.setTextColor(0xFFFFFFFF);
                description.setTextColor(0xFFFFFFFF);
                price.setTextColor(0xFFFFFFFF);
                author.setTextColor(0xFFFFFFFF);
                publisher.setTextColor(0xFFFFFFFF);
                isbn.setTextColor(0xFFFFFFFF);
                date.setTextColor(0xFFFFFFFF);
            }


        });

        button.setOnClickListener(v -> handleAddToCart());


    }

    public void handleAddToCart() {
        //TODO
    }

}
