package fr.nytuo.android411.productsList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.nytuo.android411.ProductAdapterListener;
import fr.nytuo.android411.R;

/**
 * Created by frallo on 03/02/2020.
 */

public class ProductsAdapter extends BaseAdapter {
    private List<Product> items;
    private LayoutInflater mInflater;  //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private ProductAdapterListener activity;
    private ArrayList<ProductAdapterListener> productListener = new ArrayList<>();

    public ProductsAdapter(ProductAdapterListener activity, List<Product> items) {
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

        //(1) : Réutilisation des layouts
        layoutItem = convertView == null ? mInflater.inflate(R.layout.product_layout, parent, false) : convertView;

        //(2) : Récupération des TextView de notre layout
        TextView displayName = layoutItem.findViewById(R.id.productName);
        TextView displaPrice = layoutItem.findViewById(R.id.productPrice);
//
//        //(3) : Renseignement des valeurs
        displayName.setText(items.get(position).getName());
        displaPrice.setText(items.get(position).getPrice() + "€");
//
//
//        // set image
        ImageView displayImg = layoutItem.findViewById(R.id.productImage);
        displayImg.setImageBitmap(items.get(position).getImgBitmapIndex(0));
//
//
        displayName.setTag(position);

        layoutItem.setOnClickListener(v -> {
            for (ProductAdapterListener listener : productListener) {
                listener.onElementClick(position);
            }
        });



        return layoutItem; //On retourne l'item créé.
    }

    public void addListener(ProductAdapterListener listener) {
        productListener.add(listener);
    }

}


