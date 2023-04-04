package fr.nytuo.theSithArchives.networking;

public interface PostExecutePost {

    /**
     * Méthode appelée lorsque la requête est terminée
     */
    void onSucces();

    /**
     * Méthode appelée lorsque la requête est terminée avec une erreur
     *
     * @param responseCode Code de retour de la requête
     */
    void onError(int responseCode);

    /**
     * Exécute un Runnable sur le thread principal
     *
     * @param runable Runnable à exécuter sur le thread principal
     */
    void runOnUiThread(Runnable runable);
}
