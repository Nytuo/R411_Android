package fr.nytuo.theSithArchives.cart;

import android.content.Context;

/**
 * Interface pour écouter les évènements sur le nom d'une personne
 */
public interface CartAdapterListener {
    Context getContext();

    void onPlusClick(int position);

    void onMoinClick(int position);
}