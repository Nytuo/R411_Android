package fr.nytuo.thesitharchives.product;

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

import fr.nytuo.thesitharchives.R;
import fr.nytuo.thesitharchives.cart.CartActivity;
import fr.nytuo.thesitharchives.cart.CartList;
import fr.nytuo.thesitharchives.clickandcollect.RetreiveCommandActivity;
import fr.nytuo.thesitharchives.productslist.ProductsList;
import fr.nytuo.thesitharchives.productslist.ProductsListActivity;

/**
 * Activité du produit (détail)
 */
public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Android411);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);


        TextView name = findViewById(R.id.name);
        ImageView image = findViewById(R.id.imageView);
        TextView description = findViewById(R.id.description);
        ConstraintLayout constraintLayout = findViewById(R.id.background);
        Button buttonAddToCart = findViewById(R.id.buttonAddToCart);
        TextView price = findViewById(R.id.price);
        TextView author = findViewById(R.id.authors);
        TextView publisher = findViewById(R.id.publisher);
        TextView isbn = findViewById(R.id.isbn);
        TextView date = findViewById(R.id.date);
        ScrollView scrollView = findViewById(R.id.backgroundScroll);
        Button buttonContact = findViewById(R.id.buttonContact4);

        buttonContact.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, RetreiveCommandActivity.class);
            startActivity(intent1);
        });

        //On initialise les données
        name.setText(ProductsList.getInstance().get(position).getName());
        ProductsList.getInstance().get(position).subToGetImgBitmap(this, image);
        description.setText(ProductsList.getInstance().get(position).getDescription());
        price.setText(String.format(ProductActivity.this.getString(R.string.finishByEUR), ProductsList.getInstance().get(position).getPrice()));
        StringBuilder authors = new StringBuilder();
        if (ProductsList.getInstance().get(position).getAuthors().size() > 0) {
            for (String authorE : ProductsList.getInstance().get(position).getAuthors()) {
                if (authorE.equals(ProductsList.getInstance().get(position).getAuthors().get(ProductsList.getInstance().get(position).getAuthors().size() - 1))) {
                    authors.append(authorE);
                } else authors.append(authorE).append(", ");
            }
        } else {
            authors.append("Aucun auteur");
        }
        author.setText(String.format(ProductActivity.this.getString(R.string.auteurs), authors));
        publisher.setText(String.format(ProductActivity.this.getString(R.string.editeur), ProductsList.getInstance().get(position).getPublisher()));
        isbn.setText(String.format(ProductActivity.this.getString(R.string.isbn), ProductsList.getInstance().get(position).getIsbn()));
        date.setText(String.format(ProductActivity.this.getString(R.string.parution), ProductsList.getInstance().get(position).getDate()));

        // on récupère les couleurs de l'image pour les appliquer au background
        ProductsList.getInstance().get(position).subToGetImgBitmap(this, image, bitmap -> Palette.from(ProductsList.getInstance().get(position).getImgBitmap()).generate(p -> {
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
            buttonAddToCart.setBackgroundColor(muted);

            if (muted == darkVibrant || muted == darkMuted || muted == vibrant) {
                buttonAddToCart.setBackgroundColor(Color.CYAN);
            }

            // si la couleur est sombre, on change la couleur des textes
            if (isColorDark(darkVibrant)) {
                name.setTextColor(Color.WHITE);
                description.setTextColor(Color.WHITE);
                price.setTextColor(Color.WHITE);
                author.setTextColor(Color.WHITE);
                publisher.setTextColor(Color.WHITE);
                isbn.setTextColor(Color.WHITE);
                date.setTextColor(Color.WHITE);
            }

        }));

        // on ajoute le produit au panier
        buttonAddToCart.setOnClickListener(v -> {
            buttonAddToCart.setText(R.string.added);
            handleAddToCart(position);
        });

        // on retourne à la liste des produits
        Button buttonHome = findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(v -> {
            Intent intent1 = new Intent(ProductActivity.this, ProductsListActivity.class);
            startActivity(intent1);
        });

        // on retourne au panier
        Button buttonPanier = findViewById(R.id.buttonPanier);
        buttonPanier.setOnClickListener(v -> {
            Intent intent1 = new Intent(ProductActivity.this, CartActivity.class);
            startActivity(intent1);
        });

    }

    /**
     * Vérifie si la couleur est sombre
     *
     * @param color couleur
     * @return true si la couleur est sombre
     */
    private boolean isColorDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5 && ColorUtils.calculateContrast(Color.WHITE, color) > 1.5;
    }

    /**
     * Ajoute un produit à la liste du panier
     *
     * @param position position du produit dans la liste
     */
    private void handleAddToCart(int position) {
        CartList.getInstance().add(ProductsList.getInstance().get(position));
    }

}
