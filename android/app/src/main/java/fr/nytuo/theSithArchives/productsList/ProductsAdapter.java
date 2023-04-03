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

public class ProductsAdapter extends BaseAdapter {
    private final List<Product> items;
    private final LayoutInflater mInflater;
    private final ProductAdapterListener activity;
    private final ArrayList<ProductAdapterListener> productListener = new ArrayList<>();

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
        layoutItem = convertView == null ? mInflater.inflate(R.layout.product_layout, parent, false) : convertView;
        TextView displayName = layoutItem.findViewById(R.id.productName);
        displayName.setText(items.get(position).getName());
        ImageView displayImg = layoutItem.findViewById(R.id.productImage);
        items.get(position).subToGetImgBitmap(activity.getActivity(),displayImg);
        displayName.setTag(position);
        Button button = layoutItem.findViewById(R.id.buttonAjouter);

        button.setOnClickListener(v -> {
            button.setText(R.string.added);
            activity.onAddToCartClick(position);
        });

        button.setBackgroundColor(activity.getContext().getResources().getColor(R.color.colorPrimary));
        button.setTextColor(activity.getContext().getResources().getColor(R.color.White));
        button.setText(items.get(position).getPrice() + "€");


        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_add_shopping_cart_24, 0, 0, 0);
        button.setCompoundDrawableTintList(activity.getContext().getResources().getColorStateList(R.color.White));
        button.setPadding(50, 0, 0, 0);

        layoutItem.setOnClickListener(v -> {
            for (ProductAdapterListener listener : productListener) {
                listener.onElementClick(position);
            }
        });

        return layoutItem;
    }

    public void addListener(ProductAdapterListener listener) {
        productListener.add(listener);
    }

}


