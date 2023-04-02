package fr.nytuo.android411.card;

import android.content.Context;

/**
 * Interface pour écouter les évènements sur le nom d'une personne
 */
public interface CardAdapterListener {
    Context getContext();

    void onElementClick(int position);

    void onAddToCartClick(int position);
}