package fr.nytuo.thesitharchives.productslist;

import android.graphics.Bitmap;

/**
 * Interface permettant de gérer l'évènement après le chargement d'une image
 */
public interface EventAfterImageLoad {
    /**
     * Appelé après le chargement d'une image
     *
     * @param image Image chargée
     */
    void onImageLoad(Bitmap image);
}
