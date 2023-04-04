package fr.nytuo.theSithArchives.networking;

public interface PostExecutePost {

    void onSucces();

    void onError(int responseCode);

    void runOnUiThread( Runnable runable);
}
