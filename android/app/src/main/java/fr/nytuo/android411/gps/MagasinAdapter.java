package fr.nytuo.android411.gps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.nytuo.android411.R;
import fr.nytuo.android411.ProductAdapterListener;

/**
 * Created by frallo on 03/02/2020.
 */

public class MagasinAdapter extends BaseAdapter {
    private List<PositionMagasin> items;
    private LayoutInflater mInflater;  //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private ProductAdapterListener activity;
    private ArrayList<ProductAdapterListener> listener = new ArrayList<>();

    public MagasinAdapter(ProductAdapterListener activity, List<PositionMagasin> items) {
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
        layoutItem = convertView == null ? mInflater.inflate(R.layout.magasin_layout, parent, false) : convertView;

        //(2) : Récupération des TextView de notre layout
        TextView displayName = layoutItem.findViewById(R.id.magasinName);
        TextView displaPrice = layoutItem.findViewById(R.id.magasinDistance);
//
//        //(3) : Renseignement des valeurs
        displayName.setText(items.get(position).getName());
        if (items.get(position).getDistance()==-1){
            displaPrice.setText("");
        }else {
            displaPrice.setText(String.valueOf("distance "+items.get(position).getDistance()));
        }

        displayName.setTag(position);

        layoutItem.setOnClickListener(v -> {
            for (ProductAdapterListener listener : listener) {
                listener.onElementClick(position);
            }
        });



        return layoutItem; //On retourne l'item créé.
    }

    public void addListener(ProductAdapterListener listener) {
        this.listener.add(listener);
    }

}

