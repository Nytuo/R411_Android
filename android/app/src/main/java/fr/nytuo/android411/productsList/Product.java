package fr.nytuo.android411.productsList;

public class Product {
    private String name;
    private int img;
    private int price;



    public Product(String name, int img, int price) {
        this.name = name;
        this.img = img;
        this.price = price;
    }



    public String getName() {
        return name;
    }

    public int getImg() {
        return img;
    }

    public double getPrice() {
        return price;
    }





}
