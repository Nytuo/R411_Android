package fr.nytuo.theSithArchives.productsList;

import java.util.ArrayList;

public class CardList extends ArrayList<Product> {
    private static CardList instance;

    public static CardList getInstance(){
        if (instance==null) {
            instance = new CardList();
        }
        return instance;
    }
    public CardList() {
    }
}

