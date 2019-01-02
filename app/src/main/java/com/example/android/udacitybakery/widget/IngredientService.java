package com.example.android.udacitybakery.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.udacitybakery.R;
import com.example.android.udacitybakery.model.Bakery;

public class IngredientService extends IntentService {
    public static final String ACTION_UPDATE_WIDGET = "com.example.android.udacitybakery.action.update_widget";
    public static final String ACTION_ADD_INGREDIENT_WIDGET = "com.example.android.udacitybakery.action.add_ingredient_widget";
    public static final String EXTRA_RECIPE_ID = "com.example.android.udacitybakery.extra.RECIPE_ID";
    private static final String TAG = IngredientService.class.getSimpleName();
    public Bakery mBakeryRecipe;

    /**
     * Creates an IntentService. Invoked by the subclass's constructor.
     */
    public IngredientService() {
        super("IngredientService");
    }

    public static void startActionAddIngredientWidget(Context context, Bakery bakeryRecipe) {
        Intent intent = new Intent(context, IngredientService.class);
        intent.setAction(ACTION_ADD_INGREDIENT_WIDGET);
        intent.putExtra(EXTRA_RECIPE_ID, bakeryRecipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdateWidget();
            } else if (ACTION_ADD_INGREDIENT_WIDGET.equals(action)) {
                if (intent.hasExtra(EXTRA_RECIPE_ID)) {
                    mBakeryRecipe = (Bakery) intent.getSerializableExtra(EXTRA_RECIPE_ID);
                }
                handleActionAddIngredientWidget();
            }
        }
    }

    private void handleActionUpdateWidget() {
        handleActionAddIngredientWidget();
    }

    private void handleActionAddIngredientWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        IngredientsWidgetProvider.addIngredientWidget(this, appWidgetManager, appWidgetIds, mBakeryRecipe);
        // Ensure that the data on widget is reloaded whenever the data is changed
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
    }
}
