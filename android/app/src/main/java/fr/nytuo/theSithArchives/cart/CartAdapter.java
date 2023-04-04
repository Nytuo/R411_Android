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
 * Adapter de la liste de produits du panier
 */
public class CartAdapter extends BaseAdapter {
    /**
     * Liste des produits
     */
    private final List<Product> products;
    /**
     * Inflater pour charger les layouts
     */
    private final LayoutInflater layoutInflater;
    /**
     * Listener de l'adapter
     */
    private final CartAdapterListener cartAdapterListener;
    /**
     * Liste des listeners de l'adapter
     */
    private final ArrayList<CartAdapterListener> cartAdapterListeners = new ArrayList<>();

    /**
     * @param cartAdapterListener Listener de l'adapter
     * @param products            Liste des produits
     */
    public CartAdapter(CartAdapterListener cartAdapterListener, List<Product> products) {
        this.cartAdapterListener = cartAdapterListener;
        this.products = products;
        layoutInflater = LayoutInflater.from(cartAdapterListener.getContext());
    }

    /**
     * Retourne le nombre de produits
     *
     * @return Le nombre de produits
     */
    public int getCount() {
        return products.size();
    }

    /**
     * @param position Position du produit dans la liste
     * @return Le produit à la position donnée
     */
    public Object getItem(int position) {
        return products.get(position);
    }

    /**
     * @param position Position du produit dans la liste
     * @return La position du produit dans la liste
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position    Position du produit dans la liste
     * @param convertView Vue à réutiliser
     * @param parent      Parent de la vue
     * @return La vue du produit à la position donnée
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View layoutItem;

        layoutItem = convertView == null ? layoutInflater.inflate(R.layout.cart_layout, parent, false) : convertView;

        // On récupère les éléments de la vue
        TextView productName = layoutItem.findViewById(R.id.productName);
        TextView productPrice = layoutItem.findViewById(R.id.productPrice);
        Button plusButton = layoutItem.findViewById(R.id.buttonPlus);
        Button minusButton = layoutItem.findViewById(R.id.buttonMoins);
        TextView productQuantity = layoutItem.findViewById(R.id.quantity);
        ImageView productImage = layoutItem.findViewById(R.id.productImage);

        productName.setText(products.get(position).getName());
        productPrice.setText(products.get(position).getPrice() + "€");

        //traitement du bouton +
        plusButton.setOnClickListener(v -> {
            for (CartAdapterListener listener : cartAdapterListeners) {
                listener.onPlusClick(position);
            }
        });
        plusButton.setBackgroundColor(cartAdapterListener.getContext().getResources().getColor(R.color.colorPrimary));
        plusButton.setTextColor(cartAdapterListener.getContext().getResources().getColor(R.color.White));
        plusButton.setText(products.get(position).getPrice() + "€");
        plusButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_24, 0, 0, 0);
        plusButton.setCompoundDrawableTintList(cartAdapterListener.getContext().getResources().getColorStateList(R.color.White));
        plusButton.setPadding(35, 0, 0, 0);

        //traitement du bouton -
        minusButton.setOnClickListener(v -> {
            for (CartAdapterListener listener : cartAdapterListeners) {
                listener.onMoinsClick(position);
            }
        });
        minusButton.setBackgroundColor(cartAdapterListener.getContext().getResources().getColor(R.color.colorPrimary));
        minusButton.setTextColor(cartAdapterListener.getContext().getResources().getColor(R.color.White));
        minusButton.setText(products.get(position).getPrice() + "€");
        minusButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_remove_24, 0, 0, 0);
        minusButton.setCompoundDrawableTintList(cartAdapterListener.getContext().getResources().getColorStateList(R.color.White));
        minusButton.setPadding(35, 0, 0, 0);

        productQuantity.setText(String.valueOf(products.get(position).getQuantity()));
        products.get(position).subToGetImgBitmap(cartAdapterListener.getActivity(), productImage);
        productName.setTag(position);
        return layoutItem;
    }

    /**
     * Ajoute un listener à l'adapter
     *
     * @param listener Listener à ajouter
     */
    public void addListener(CartAdapterListener listener) {
        cartAdapterListeners.add(listener);
    }

}


