package fr.nytuo.theSithArchives.clickandcollect;

/**
 * Classe représentant une commande
 */
public class Commande {

    /**
     * Numéro de la commande
     */
    private String commandNumber;

    /**
     * Prix de la commande
     */
    private String price;

    /**
     * Livres de la commande
     */
    private String books;

    public Commande() {
        //Vide car singleton
    }

    /**
     * Récupère le numéro de la commande
     *
     * @return Numéro de la commande
     */
    public String getCommandNumber() {
        return commandNumber;
    }

    /**
     * Définit le numéro de la commande
     *
     * @param commandNumber Numéro de la commande
     */
    public void setCommandNumber(String commandNumber) {
        this.commandNumber = commandNumber;
    }

    /**
     * Récupère le prix de la commande
     *
     * @return Prix de la commande
     */
    public String getPrice() {
        return price;
    }

    /**
     * Définit le prix de la commande
     *
     * @param price Prix de la commande
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Récupère les livres de la commande
     *
     * @return Les livres de la commande
     */
    public String getBooks() {
        return books;
    }

    /**
     * Définit les livres de la commande
     *
     * @param books Livres de la commande
     */
    public void setBooks(String books) {
        this.books = books;
    }
}
