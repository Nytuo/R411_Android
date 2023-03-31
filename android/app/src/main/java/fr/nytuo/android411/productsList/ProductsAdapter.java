package fr.nytuo.android411.productsList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import fr.nytuo.android411.R;

/**
 * Created by frallo on 03/02/2020.
 */
public class ProductsAdapter extends BaseAdapter {
    private ProductsList items;
    private LayoutInflater mInflater;  //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private ProductAdapterListener activity;

    public ProductsAdapter(ProductAdapterListener activity, ProductsList items) {
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
        TextView displayName = layoutItem.findViewById(R.id.name);
        TextView displaPrice = layoutItem.findViewById(R.id.price);

        //(3) : Renseignement des valeurs
        displayName.setText(items.get(position).getName());
        displaPrice.setText("100");


        // set image
        ImageView displayImg = layoutItem.findViewById(R.id.image2);
        displayImg.setImageBitmap(items.get(position).getImgBitmap().get(0));


        displayName.setTag(position);



        return layoutItem; //On retourne l'item créé.
    }



}


