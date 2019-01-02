package com.example.android.udacitybakery.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.example.android.udacitybakery.activities.MainActivity;
import com.example.android.udacitybakery.R;
import com.example.android.udacitybakery.activities.RecipeActivity;
import com.example.android.udacitybakery.model.Bakery;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {
    private static final String TAG = IngredientsWidgetProvider.class.getSimpleName();
    static Bakery recipe;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Bakery bakeryRecipe) {
        recipe = bakeryRecipe;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);

        Intent appIntent;
        if (recipe != null) {
            Intent ingredientIntent = new Intent(context, ListWidgetService.class);
            views.setTextViewText(R.id.widget_recipe_title, recipe.getName());
            views.setRemoteAdapter(R.id.widget_list_view, ingredientIntent);
            appIntent = new Intent(context, RecipeActivity.class);
            appIntent.putExtra(Intent.EXTRA_TEXT, recipe);

        } else {
            appIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_layout_parent, pendingIntent);
        }
        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        IngredientService.startActionAddIngredientWidget(context, recipe);
    }

    public static void addIngredientWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Bakery recipeIngredients) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeIngredients);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

