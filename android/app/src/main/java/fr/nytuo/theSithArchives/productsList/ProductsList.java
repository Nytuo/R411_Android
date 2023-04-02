package fr.nytuo.theSithArchives.productsList;



import java.util.ArrayList;

public class ProductsList extends ArrayList<Product> {
    private static ProductsList instance;

    public static ProductsList getInstance(){
        if (instance==null) {
            instance = new ProductsList();
        }
        return instance;
    }
    public ProductsList() {
    }
}
