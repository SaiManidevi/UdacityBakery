package com.example.android.udacitybakery.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.udacitybakery.R;
import com.example.android.udacitybakery.model.Bakery;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BakeryAdapter extends RecyclerView.Adapter<BakeryAdapter.BakeryAdapterViewHolder> {
    private List<Bakery> mBakeryRecipes;
    /*
     * An on-click handler to make it easy for an Activity to interface with
     * the RecyclerView
     */
    final private BakeryItemClickListener mOnClickListener;

    /**
     * The interface that receives onClick messages.
     */
    public interface BakeryItemClickListener {
        void onBakeryItemClick(Bakery clickedBakeryRecipe);
    }

    public BakeryAdapter(BakeryItemClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public BakeryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        return new BakeryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BakeryAdapterViewHolder holder, int position) {
        Bakery bakeryItem = mBakeryRecipes.get(position);
        String bakeryItemName = bakeryItem.getName();
        holder.mMasterListTextView.setText(bakeryItemName);
        int bakeryItemServings = bakeryItem.getServings();
        holder.mServings.setText(Integer.toString(bakeryItemServings));
        int id = bakeryItem.getRecipieId();
        switch (id) {
            case 1:
                Picasso.get().load(R.drawable.nutella_pie).into(holder.mMasterListImageView);
                break;
            case 2:
                Picasso.get().load(R.drawable.brownies).into(holder.mMasterListImageView);
                break;
            case 3:
                Picasso.get().load(R.drawable.yellow_cake).into(holder.mMasterListImageView);
                break;
            case 4:
                Picasso.get().load(R.drawable.cheesecake).into(holder.mMasterListImageView);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mBakeryRecipes == null)
            return 0;
        else
            return mBakeryRecipes.size();
    }

    public class BakeryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMasterListImageView;
        public final TextView mMasterListTextView;
        public final TextView mServings;

        public BakeryAdapterViewHolder(View itemView) {
            super(itemView);
            mMasterListImageView = itemView.findViewById(R.id.iv_master_image);
            mMasterListTextView = itemView.findViewById(R.id.tv_master_text);
            mServings = itemView.findViewById(R.id.tv_servings);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Bakery currentBakeryRecipe = mBakeryRecipes.get(adapterPosition);
            mOnClickListener.onBakeryItemClick(currentBakeryRecipe);
        }
    }

    public void setBakeryRecipes(List<Bakery> bakeryRecipes) {
        mBakeryRecipes = bakeryRecipes;
        notifyDataSetChanged();
    }

}
