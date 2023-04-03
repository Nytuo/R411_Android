package fr.nytuo.theSithArchives.cart;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public interface CartAdapterListener {
    Context getContext();

    void onPlusClick(int position);

    void onMoinsClick(int position);

    AppCompatActivity getActivity();
}