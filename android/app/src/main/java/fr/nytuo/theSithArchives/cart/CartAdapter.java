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

        Button plusButton = layoutItem.findViewById(R.id.buttonPlus);

        plusButton.setOnClickListener(v -> {
            for (CartAdapterListener listener : productListener) {
                listener.onPlusClick(position);
            }
        });
        plusButton.setBackgroundColor(activity.getContext().getResources().getColor(R.color.colorPrimary));
        plusButton.setTextColor(activity.getContext().getResources().getColor(R.color.White));
        plusButton.setText(items.get(position).getPrice() + "€");


        plusButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0);
        plusButton.setCompoundDrawableTintList(activity.getContext().getResources().getColorStateList(R.color.White));
        //make a left padding
        plusButton.setPadding(35, 0, 0, 0);
        Button minusButton = layoutItem.findViewById(R.id.buttonMoins);
        minusButton.setOnClickListener(v -> {
            for (CartAdapterListener listener : productListener) {
                listener.onMoinClick(position);
            }
        });
        minusButton.setBackgroundColor(activity.getContext().getResources().getColor(R.color.colorPrimary));
        minusButton.setTextColor(activity.getContext().getResources().getColor(R.color.White));
        minusButton.setText(items.get(position).getPrice() + "€");


        minusButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_remove_24, 0, 0, 0);
        minusButton.setCompoundDrawableTintList(activity.getContext().getResources().getColorStateList(R.color.White));
        //make a left padding
        minusButton.setPadding(35, 0, 0, 0);
        TextView displayQuantity = layoutItem.findViewById(R.id.quantity);

        displayQuantity.setText(items.get(position).getQuantity() + "");
     // set image
        ImageView displayImg = layoutItem.findViewById(R.id.productImage);
        items.get(position).subToGetImgBitmap(activity.getActivity(),displayImg);
//
//
        displayName.setTag(position);




        return layoutItem; //On retourne l'item créé.
    }

    public void addListener(CartAdapterListener listener) {
        productListener.add(listener);
    }

}


