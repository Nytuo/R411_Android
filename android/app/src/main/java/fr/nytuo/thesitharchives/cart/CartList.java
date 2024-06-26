package fr.nytuo.thesitharchives.cart;

import java.util.ArrayList;
import java.util.Objects;

import fr.nytuo.thesitharchives.productslist.Product;

/**
 * Liste des produits du panier
 */
public class CartList extends ArrayList<Product> {
    /**
     * Instance de la liste
     */
    private static CartList instance;

    public CartList() {
        // Vide car singleton
    }

    /**
     * Retourne l'instance de la liste
     *
     * @return L'instance de la liste
     */
    public static CartList getInstance() {
        if (instance == null) {
            instance = new CartList();
        }
        return instance;
    }

    /**
     * Retourne le prix total de la liste
     *
     * @return Le prix total de la liste
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < this.size(); i++) {
            totalPrice += this.get(i).getPrice() * this.get(i).getQuantity();
        }
        return totalPrice;
    }

    public String getAllBooksName() {
        String allBooksName = "";
        for (int i = 0; i < this.size(); i++) {
            allBooksName += this.get(i).getName() + ", ";
        }
        return allBooksName;
    }

    @Override
    public boolean add(Product product) {
        product.setQuantity(product.getQuantity() + 1);
        for (int i = 0; i < this.size(); i++) {
            if (Objects.equals(this.get(i).getIsbn(), product.getIsbn())) {
                return true;
            }
        }
        return super.add(product);
    }
}

