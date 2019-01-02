package com.example.android.udacitybakery.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.udacitybakery.R;
import com.example.android.udacitybakery.model.Bakery;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {
    private List<Bakery.IngredientsBean> mIngredientsList;

    @NonNull
    @Override
    public IngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredient_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        return new IngredientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapterViewHolder holder, int position) {
        Bakery.IngredientsBean ingredients = mIngredientsList.get(position);
        String ingredientName = ingredients.getIngredient();
        String measure = ingredients.getQuantity() + " " + ingredients.getMeasure();

        holder.mIngredientTextView.setText(ingredientName);
        holder.mMeasureTextView.setText(measure);
    }

    @Override
    public int getItemCount() {
        if (mIngredientsList == null)
            return 0;
        else
            return mIngredientsList.size();
    }


    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIngredientTextView;
        public final TextView mMeasureTextView;

        public IngredientAdapterViewHolder(View itemView) {
            super(itemView);
            mIngredientTextView = itemView.findViewById(R.id.tv_ingredient);
            mMeasureTextView = itemView.findViewById(R.id.tv_measure);
        }
    }

    public void setRecipeIngredients(List<Bakery.IngredientsBean> ingredientsList) {
        mIngredientsList = ingredientsList;
        notifyDataSetChanged();
    }

}
