package fr.nytuo.theSithArchives.gps;

import android.content.Context;

public interface MagasinAdapterListener {

    Context getContext();
    void onElementClick(int position);
}
