package fr.nytuo.theSithArchives.productsList;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public interface ProductAdapterListener {
    Context getContext();

    void onElementClick(int position);

    void onAddToCartClick(int position);

    AppCompatActivity getActivity();
}