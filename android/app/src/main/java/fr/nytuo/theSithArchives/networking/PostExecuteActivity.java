package fr.nytuo.theSithArchives.networking;

import java.util.List;

public interface PostExecuteActivity<T> {
    /**
     * Appelé après la récupération des éléments
     * @param itemList Liste des éléments récupérés
     */
    void onPostExecute(List<T> itemList);

    /**
     * Exécute un Runnable sur l'UI
     * @param runable Runnable à exécuter sur l'UI
     */
    void runOnUiThread( Runnable runable);

    void onError();
}
