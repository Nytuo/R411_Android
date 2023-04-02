package fr.nytuo.android411;

import android.content.Context;

/**
 * Interface pour écouter les évènements sur le nom d'une personne
 */
public interface ProductAdapterListener {
    Context getContext();

    void onElementClick(int position);
}