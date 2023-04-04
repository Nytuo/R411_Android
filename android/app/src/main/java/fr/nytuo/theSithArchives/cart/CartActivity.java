package fr.nytuo.theSithArchives.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.retraitMagasin.MagasinSelectionActivity;
import fr.nytuo.theSithArchives.productsList.ProductsListActivity;

/**
 * Activité du panier
 * Récupère la liste des produits du panier et l'affiche, calcule le prix total et permet de passer à la commande
 */
public class CartActivity extends AppCompatActivity implements CartAdapterListener {
    /**
     * Adapter de la liste de produits
     */
    private CartAdapter cartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Récupération des éléments de l'interface
        ListView cartListView = findViewById(R.id.cartListView);
        Button buttonPanier = findViewById(R.id.buttonPanier);
        Button buttonHome = findViewById(R.id.buttonHome);
        Button buttonConfirmer = findViewById(R.id.buttonConfirmer);

        // Initialisation de l'adapter
        cartAdapter = new CartAdapter(this, CartList.getInstance());
        cartListView.setAdapter(cartAdapter);
        cartAdapter.addListener(this);

        // Gestion des boutons

        // Bouton panier
        buttonPanier.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        // Bouton home
        buttonHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductsListActivity.class);
            startActivity(intent);
        });

        // Bouton confirmer
        buttonConfirmer.setOnClickListener(v -> {
            CartList.getInstance().clear();
            Intent intent = new Intent(this, MagasinSelectionActivity.class);
            startActivity(intent);
        });
        buttonConfirmer.setEnabled(!CartList.getInstance().isEmpty());

        // Mise à jour du prix total
        updateTotalPrice();
    }

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public void onPlusClick(int position) {
        // On augmente la quantité du produit, notifie l'adapter et met à jour le prix total
        CartList.getInstance().get(position).setQuantity(CartList.getInstance().get(position).getQuantity() + 1);
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    @Override
    public void onMoinsClick(int position) {
        // On diminue la quantité du produit, notifie l'adapter et met à jour le prix total
        //si la quantité est à 1, on supprime le produit, sinon on diminue la quantité

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
            Button buttonConfirmer = findViewById(R.id.buttonConfirmer);
            buttonConfirmer.setEnabled(false);
        }

    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    /**
     * Met à jour le prix total
     */
    private void updateTotalPrice() {
        TextView totalPrice = findViewById(R.id.textViewTotal);
        totalPrice.setText(getString(R.string.total) + CartList.getInstance().getTotalPrice() + "€");
    }

}
