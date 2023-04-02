package fr.nytuo.android411.gps;

import android.content.Context;

public interface MagasinAdapterListener {

    Context getContext();
    void onElementClick(int position);
}
