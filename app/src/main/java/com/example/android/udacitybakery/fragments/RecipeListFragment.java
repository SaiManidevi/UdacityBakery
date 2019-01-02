package com.example.android.udacitybakery.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.udacitybakery.R;
import com.example.android.udacitybakery.adapters.IngredientAdapter;
import com.example.android.udacitybakery.adapters.RecipeStepsAdapter;
import com.example.android.udacitybakery.databinding.FragmentRecipeListBinding;
import com.example.android.udacitybakery.model.Bakery;

import java.util.List;

public class RecipeListFragment extends Fragment implements RecipeStepsAdapter.RecipeStepClickListener {
    private FragmentRecipeListBinding binding;
    List<Bakery.IngredientsBean> mIngredientsList;
    List<Bakery.StepsList> mStepsList;
    IngredientAdapter mIngredientAdapter;
    RecipeStepsAdapter mRecipeStepsAdapter;
    // Define a new interface OnButtonClickListener that triggers a callback in the host activity
    OnStepClickListener mCallback;
    OnAddWidgetButtonListener mAddWidgetCallBack;

    // OnStepClickListener and OnAddWidgetButtonListener interface,
    // calls a method in the host activity named onRecipeStepSelected and onAddWidget respectively
    public interface OnStepClickListener {
        void onRecipeStepSelected(Bakery.StepsList clickedRecipeStep);
    }

    public interface OnAddWidgetButtonListener {
        void onAddWidget();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
            mAddWidgetCallBack = (OnAddWidgetButtonListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnButtonClickListener");
        }
    }

    public RecipeListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false);
        View rootView = binding.getRoot();
        mIngredientAdapter = new IngredientAdapter();
        binding.recyclerviewIngredients.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new
                DividerItemDecoration(binding.recyclerviewIngredients.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_recyclerview));
        binding.recyclerviewIngredients.addItemDecoration(dividerItemDecoration);
        binding.recyclerviewIngredients.setAdapter(mIngredientAdapter);
        mIngredientAdapter.setRecipeIngredients(mIngredientsList);

        mRecipeStepsAdapter = new RecipeStepsAdapter(getActivity(), this);
        binding.recyclerviewSteps.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerviewSteps.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        binding.recyclerviewSteps.setAdapter(mRecipeStepsAdapter);
        mRecipeStepsAdapter.setRecipeSteps(mStepsList);

        binding.btnAddToWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddWidgetCallBack.onAddWidget();
            }
        });

        return rootView;
    }

    public void setIngredientsList(List<Bakery.IngredientsBean> ingredientsList, List<Bakery.StepsList> stepsList) {
        mIngredientsList = ingredientsList;
        mStepsList = stepsList;
    }

    @Override
    public void onRecipeStepClick(Bakery.StepsList clickedRecipeStep) {
        mCallback.onRecipeStepSelected(clickedRecipeStep);
    }
}
