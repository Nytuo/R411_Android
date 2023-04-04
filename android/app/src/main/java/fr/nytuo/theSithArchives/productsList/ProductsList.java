package fr.nytuo.theSithArchives.productsList;

import java.util.ArrayList;

/**
 * Classe représentant une liste de produits
 */
public class ProductsList extends ArrayList<Product> {
    /**
     * Instance de la liste de produits
     */
    private static ProductsList instance;

    public ProductsList() {
    }

    /**
     * Récupère l'instance de la liste de produits
     *
     * @return Instance de la liste de produits
     */
    public static ProductsList getInstance() {
        if (instance == null) {
            instance = new ProductsList();
        }
        return instance;
    }
}
