package com.example.android.udacitybakery.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.udacitybakery.R;
import com.example.android.udacitybakery.model.Bakery;


import java.util.ArrayList;
import java.util.List;

import static com.example.android.udacitybakery.widget.IngredientsWidgetProvider.recipe;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    private Bakery mBakeryRecipe;
    private List<Bakery.IngredientsBean> mSelectedRecipeIngredients;
    private ArrayList<String> mIngredientsList = new ArrayList<String>();
    private static final String TAG = "ListRemoteViewsFactory";

    public ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        //remove old ingredients and re-initialize the ingredients
        mIngredientsList.clear();
        mBakeryRecipe = recipe;
        mSelectedRecipeIngredients = mBakeryRecipe.getIngredients();
        for (Bakery.IngredientsBean ingredient : mSelectedRecipeIngredients) {
            mIngredientsList.add(ingredient.getQuantity() +
                    " " + ingredient.getMeasure() +
                    " of " + ingredient.getIngredient());
        }
        Log.v(TAG, "Ingredients List: " + mIngredientsList);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mSelectedRecipeIngredients == null)
            return 0;
        else
            return mSelectedRecipeIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget_item);
        views.setTextViewText(R.id.tv_widget_item, mIngredientsList.get(position));
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

