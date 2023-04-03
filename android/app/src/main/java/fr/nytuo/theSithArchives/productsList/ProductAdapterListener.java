package fr.nytuo.theSithArchives.productsList;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Interface pour écouter les évènements sur le nom d'une personne
 */
public interface ProductAdapterListener {
    Context getContext();

    void onElementClick(int position);

    void onAddToCartClick(int position);

    AppCompatActivity getActivity();
}