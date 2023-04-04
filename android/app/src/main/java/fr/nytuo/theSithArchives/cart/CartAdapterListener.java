package fr.nytuo.theSithArchives.cart;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public interface CartAdapterListener {
    /**
     * Récupère le contexte de l'activité
     * @return Le contexte de l'activité
     */
    Context getContext();

    /**
     * Appelé quand on clique sur le bouton +
     * @param position Position du produit dans la liste
     */
    void onPlusClick(int position);

    /**
     * Appelé quand on clique sur le bouton -
     * @param position Position du produit dans la liste
     */
    void onMoinsClick(int position);

    /**
     * Récupère l'activité
     * @return L'activité
     */
    AppCompatActivity getActivity();
}