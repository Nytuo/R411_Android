package fr.nytuo.theSithArchives.productsList;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Interface permettant de gérer les évènements de l'adapter
 */
public interface ProductAdapterListener {
    /**
     * Récupère le contexte de l'activité
     *
     * @return Contexte de l'activité
     */
    Context getContext();

    /**
     * Appelé lors du clic sur un élément de la liste
     *
     * @param position Position de l'élément cliqué
     */
    void onElementClick(int position);

    /**
     * Appelé lors du clic sur le bouton "Ajouter au panier"
     *
     * @param position Position de l'élément cliqué
     */
    void onAddToCartClick(int position);

    /**
     * Appelé lors du clic sur le bouton "Ajouter au favoris"
     *
     * @return Activité
     */
    AppCompatActivity getActivity();
}