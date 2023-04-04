package fr.nytuo.theSithArchives.cart;

import java.util.ArrayList;

import fr.nytuo.theSithArchives.productsList.Product;

/**
 * Liste des produits du panier
 */
public class CartList extends ArrayList<Product> {
    /**
     * Instance de la liste
     */
    private static CartList instance;

    /**
     * Retourne l'instance de la liste
     * @return L'instance de la liste
     */
    public static CartList getInstance(){
        if (instance==null) {
            instance = new CartList();
        }
        return instance;
    }

    public CartList() {
    }

    /**
     * Retourne le prix total de la liste
     * @return Le prix total de la liste
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < this.size(); i++) {
            totalPrice += this.get(i).getPrice()*this.get(i).getQuantity();
        }
        return totalPrice;
    }
}

