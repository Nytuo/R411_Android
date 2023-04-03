package fr.nytuo.theSithArchives.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.productsList.Product;

/**
 * Created by frallo on 03/02/2020.
 */

public class CartAdapter extends BaseAdapter {
    private List<Product> items;
    private LayoutInflater mInflater;  //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private CartAdapterListener activity;
    private ArrayList<CartAdapterListener> productListener = new ArrayList<>();

    public CartAdapter(CartAdapterListener activity, List<Product> items) {
        this.activity = activity;
        this.items = items;
        mInflater = LayoutInflater.from(activity.getContext());
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View layoutItem;

        //TODO: Refaire l'adapter en fonction des boutons plus et moins

        //(1) : Réutilisation des layouts
        layoutItem = convertView == null ? mInflater.inflate(R.layout.cart_layout, parent, false) : convertView;

        //(2) : Récupération des TextView de notre layout
        TextView displayName = layoutItem.findViewById(R.id.productName);
        TextView displayPrice = layoutItem.findViewById(R.id.productPrice);
//
//        //(3) : Renseignement des valeurs
        displayName.setText(items.get(position).getName());
        displayPrice.setText(items.get(position).getPrice() + "€");
//
//
//        // set image
        ImageView displayImg = layoutItem.findViewById(R.id.productImage);
        displayImg.setImageBitmap(items.get(position).getImgBitmapIndex(0));
//
//
        displayName.setTag(position);
        Button button = layoutItem.findViewById(R.id.buttonAjouter);

        button.setOnClickListener(v -> {
            for (CartAdapterListener listener : productListener) {
                listener.onElementClick(position);
            }
        });


        layoutItem.setOnClickListener(v -> {
            for (CartAdapterListener listener : productListener) {
                listener.onElementClick(position);
            }
        });



        return layoutItem; //On retourne l'item créé.
    }

    public void addListener(CartAdapterListener listener) {
        productListener.add(listener);
    }

}


