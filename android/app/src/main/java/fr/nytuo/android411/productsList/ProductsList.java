package fr.nytuo.android411.productsList;



import android.content.res.Resources;

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
    public void setResources(Resources r,String packageName){
        //todo get list produc from api
    }
}
