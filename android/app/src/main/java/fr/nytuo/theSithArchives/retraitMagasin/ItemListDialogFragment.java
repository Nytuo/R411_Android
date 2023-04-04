package fr.nytuo.theSithArchives.retraitMagasin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.nytuo.theSithArchives.databinding.FragmentItemListDialogListDialogItemBinding;
import fr.nytuo.theSithArchives.databinding.FragmentItemListDialogListDialogBinding;


/**
 * Un fragment qui affiche une liste de choix dans un {@link BottomSheetDialogFragment}.
 */
public class ItemListDialogFragment extends BottomSheetDialogFragment {

    /**
     * Configuration du listener
     * @param onItemSelectedListener Listener de selection d'un item
     */
    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    /**
     * Interface pour la selection d'un item
     */
    public interface OnItemSelectedListener {
        /**
         * Appelé quand on clique sur un item
         * @param position Position de l'item dans la liste
         */
        void onItemSelected(int position);
    }

    /**
     * Listener de click sur un item
     */
    private OnItemSelectedListener onItemSelectedListener;

    private static final String ARG_ITEM_COUNT = "item_count";
    private FragmentItemListDialogListDialogBinding binding;

    private static List<Magasin> magasins;
    private int selectedPosition = -1;

    public static ItemListDialogFragment newInstance(int itemCount, List<Magasin> magasins) {
        final ItemListDialogFragment fragment = new ItemListDialogFragment();
        final Bundle args = new Bundle();
        //On initialise la liste des magasins
        ItemListDialogFragment.magasins = magasins;
        args.putInt(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentItemListDialogListDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Retourne l'item selectionné
     * @return L'item selectionné
     */
    public int getSelectedItem() {
        return selectedPosition;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        ViewHolder(FragmentItemListDialogListDialogItemBinding binding) {
            super(binding.getRoot());
            text = binding.text;

            //On ajoute un listener sur le click
            itemView.setOnClickListener(v -> {
                //On met à jour la position selectionnée
                selectedPosition = getAdapterPosition();
                //On appelle le listener
                onItemSelectedListener.onItemSelected(selectedPosition);
                //On ferme la fenêtre
                dismiss();
            });

        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;

        ItemAdapter(int itemCount) {
            mItemCount = itemCount;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(FragmentItemListDialogListDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //On affiche le nom du magasin et sa distance
            holder.text.setText(magasins.get(position).getName() + " (" + magasins.get(position).getDistance()/1000 + " km)");
        }

    }
}

