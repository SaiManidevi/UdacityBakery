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

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepAdapterViewHolder> {
    private List<Bakery.StepsList> mStepsList;
    private Context mContext;
    /*
     * An on-click handler to make it easy for an Activity to interface with
     * the RecyclerView
     */
    final private RecipeStepClickListener mOnRecipeStepClickListener;

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeStepClickListener {
        void onRecipeStepClick(Bakery.StepsList clickedRecipeStep);
    }

    public RecipeStepsAdapter(Context context, RecipeStepClickListener mOnRecipeStepClickListener) {
        this.mOnRecipeStepClickListener = mOnRecipeStepClickListener;
        mContext = context;
    }

    @NonNull
    @Override
    public RecipeStepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_step_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        return new RecipeStepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepAdapterViewHolder holder, int position) {
        Bakery.StepsList stepsList = mStepsList.get(position);
        String recipeStepShortDesc = stepsList.getShortDescription();
        holder.mStepShortDescription.setText(recipeStepShortDesc);
    }

    @Override
    public int getItemCount() {
        if (mStepsList == null)
            return 0;
        else
            return mStepsList.size();
    }

    public class RecipeStepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mStepShortDescription;

        public RecipeStepAdapterViewHolder(View itemView) {
            super(itemView);
            mStepShortDescription = (TextView) itemView.findViewById(R.id.tv_recipe_step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Bakery.StepsList currentRecipeStep = mStepsList.get(adapterPosition);
            mOnRecipeStepClickListener.onRecipeStepClick(currentRecipeStep);
        }
    }

    public void setRecipeSteps(List<Bakery.StepsList> recipeSteps) {
        mStepsList = recipeSteps;
        notifyDataSetChanged();
    }
}
