package fr.nytuo.theSithArchives.productsList;

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

/**
 * Adapter permettant de gérer la liste des produits
 */
public class ProductsAdapter extends BaseAdapter {
    /**
     * Liste des produits
     */
    private final List<Product> productList;
    /**
     * Layout inflater
     */
    private final LayoutInflater layoutInflater;
    /**
     * Listener de l'activité
     */
    private final ProductAdapterListener productAdapterListener;
    /**
     *  Liste des listeners
     */
    private final ArrayList<ProductAdapterListener> productAdapterListeners = new ArrayList<>();

    /**
     * Constructeur de l'adapter
     * @param productAdapterListener Listener de l'activité
     * @param productList Liste des produits
     */
    public ProductsAdapter(ProductAdapterListener productAdapterListener, List<Product> productList) {
        this.productAdapterListener = productAdapterListener;
        this.productList = productList;
        layoutInflater = LayoutInflater.from(productAdapterListener.getContext());
    }

    /**
     * Récupère le nombre d'éléments dans la liste
     * @return Nombre d'éléments dans la liste
     */
    public int getCount() {
        return productList.size();
    }

    /**
     * Récupère l'élément à la position donnée
     * @param position Position de l'élément
     * @return L'élément à la position donnée
     */
    public Object getItem(int position) {
        return productList.get(position);
    }

    /**
     * Récupère l'ID de l'élément à la position donnée
     * @param position Position de l'élément
     * @return L'ID de l'élément à la position donnée
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Récupère la vue de l'élément à la position donnée
     * @param position Position de l'élément
     * @param convertView Vue de l'élément
     * @param parent Parent de l'élément
     * @return Vue de l'élément
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View layoutItem;
        layoutItem = convertView == null ? layoutInflater.inflate(R.layout.product_layout, parent, false) : convertView;
        TextView displayName = layoutItem.findViewById(R.id.productName);
        displayName.setText(productList.get(position).getName());
        ImageView displayImg = layoutItem.findViewById(R.id.productImage);
        productList.get(position).subToGetImgBitmap(productAdapterListener.getActivity(),displayImg);
        displayName.setTag(position);
        Button buttonAJouter = layoutItem.findViewById(R.id.buttonAjouter);

        buttonAJouter.setOnClickListener(v -> {
            buttonAJouter.setText(R.string.added);
            productAdapterListener.onAddToCartClick(position);
        });

        buttonAJouter.setBackgroundColor(productAdapterListener.getContext().getResources().getColor(R.color.colorPrimary));
        buttonAJouter.setTextColor(productAdapterListener.getContext().getResources().getColor(R.color.White));
        buttonAJouter.setText(productList.get(position).getPrice() + "€");
        buttonAJouter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_shopping_cart_24, 0, 0, 0);
        buttonAJouter.setCompoundDrawableTintList(productAdapterListener.getContext().getResources().getColorStateList(R.color.White));
        buttonAJouter.setPadding(50, 0, 0, 0);

        layoutItem.setOnClickListener(v -> {
            for (ProductAdapterListener listener : productAdapterListeners) {
                listener.onElementClick(position);
            }
        });

        return layoutItem;
    }

    /**
     * Ajoute un listener à l'adapter
     * @param listener Listener de l'activité
     */
    public void addListener(ProductAdapterListener listener) {
        productAdapterListeners.add(listener);
    }

}


