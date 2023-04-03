package fr.nytuo.theSithArchives.gps;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.cart.CartList;

public class MagasinAdapter extends BaseAdapter {
    private final List<PositionMagasin> items;
    private final LayoutInflater mInflater;
    private MagasinAdapterListener activity;
    private final ArrayList<MagasinAdapterListener> listener = new ArrayList<>();

    public MagasinAdapter(MagasinAdapterListener activity, List<PositionMagasin> items) {
        this.activity = activity;
        this.items = items;
        this.items.sort((o1, o2) -> {
            if (o1.getDistance() == -1) {
                return 1;
            } else if (o2.getDistance() == -1) {
                return -1;
            } else {
                return (int) (o1.getDistance() - o2.getDistance());
            }
        });
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
        layoutItem = convertView == null ? mInflater.inflate(R.layout.magasin_layout, parent, false) : convertView;
        TextView displayName = layoutItem.findViewById(R.id.magasinName);
        TextView displaPrice = layoutItem.findViewById(R.id.magasinPrice);
        displayName.setText(items.get(position).getName());
        if (items.get(position).getDistance()==-1){
            displaPrice.setText("");
        }else {
            displaPrice.setText("distance " + items.get(position).getDistance() / 1000 + " km");
        }
        displayName.setTag(position);
        return layoutItem;
    }

    public void addListener(MagasinAdapterListener listener) {
        this.listener.add(listener);
    }

}


