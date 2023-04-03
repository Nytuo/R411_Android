package fr.nytuo.theSithArchives.cart;

import java.util.ArrayList;

import fr.nytuo.theSithArchives.productsList.Product;

public class CartList extends ArrayList<Product> {
    private static CartList instance;

    public static CartList getInstance(){
        if (instance==null) {
            instance = new CartList();
        }
        return instance;
    }
    public CartList() {
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < this.size(); i++) {
            totalPrice += this.get(i).getPrice()*this.get(i).getQuantity();
        }
        return totalPrice;
    }
}

