package fr.nytuo.theSithArchives.retraitMagasin;

public class Commande {

    String commandNumber;

    String price;

    String books;

    public Commande(){
    }

    public void setCommandNumber(String commandNumber) {
        this.commandNumber = commandNumber;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public String getCommandNumber() {
        return commandNumber;
    }

    public String getPrice() {
        return price;
    }

    public String getBooks() {
        return books;
    }
}
