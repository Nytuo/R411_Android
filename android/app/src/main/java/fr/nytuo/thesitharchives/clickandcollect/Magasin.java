package fr.nytuo.thesitharchives.clickandcollect;

/**
 * Classe représentant un magasin
 */
public class Magasin {
    /**
     * Latitude du magasin
     */
    private double latitude;
    /**
     * Longitude du magasin
     */
    private double longitude;

    /**
     * Distance entre le magasin et l'utilisateur
     */
    private double distance = -1;
    /**
     * Nom du magasin
     */
    private String name;

    Magasin() {
    }

    /**
     * Récupère la latitude du magasin
     *
     * @return Latitude du magasin
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Définit la latitude du magasin
     *
     * @param latitude Latitude du magasin
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Récupère la longitude du magasin
     *
     * @return Longitude du magasin
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Définit la longitude du magasin
     *
     * @param longitude Longitude du magasin
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Récupère le nom du magasin
     *
     * @return le nom du magasin
     */
    public String getName() {
        return name;
    }

    /**
     * Définit le nom du magasin
     *
     * @param name le nom du magasin
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Récupère la distance entre le magasin et l'utilisateur
     *
     * @return La distance entre le magasin et l'utilisateur
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Définit la distance entre le magasin et l'utilisateur
     *
     * @param distance La distance entre le magasin et l'utilisateur
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }
}
