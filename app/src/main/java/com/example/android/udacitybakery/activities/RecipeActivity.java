package com.example.android.udacitybakery.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.udacitybakery.R;
import com.example.android.udacitybakery.fragments.DetailStepFragment;
import com.example.android.udacitybakery.fragments.RecipeListFragment;
import com.example.android.udacitybakery.model.Bakery;
import com.example.android.udacitybakery.utilities.BakeryConstants;
import com.example.android.udacitybakery.model.RecipeStepsContainer;
import com.example.android.udacitybakery.widget.IngredientService;

import java.util.List;

public class RecipeActivity extends AppCompatActivity implements RecipeListFragment.OnStepClickListener, RecipeListFragment.OnAddWidgetButtonListener, DetailStepFragment.OnButtonClickListener {
    private static final String TAG = RecipeActivity.class.getSimpleName();
    private Bakery mSelectedBakeryRecipe;
    private List<Bakery.IngredientsBean> mSelectedRecipeIngredients;
    private List<Bakery.StepsList> mSelectedRecipeStepsList;
    private Bakery.StepsList mCurrentRecipeStep;
    private boolean isTwoPane;
    private boolean isFragmentCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                mSelectedBakeryRecipe = (Bakery) intent.getSerializableExtra(Intent.EXTRA_TEXT);
                mSelectedRecipeIngredients = mSelectedBakeryRecipe.getIngredients();
                mSelectedRecipeStepsList = mSelectedBakeryRecipe.getSteps();
                getSupportActionBar().setTitle(mSelectedBakeryRecipe.getName());
            }
        }

        RecipeListFragment recipeListFragment = new RecipeListFragment();
        recipeListFragment.setIngredientsList(mSelectedRecipeIngredients, mSelectedRecipeStepsList);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.ingredients_steps_container, recipeListFragment)
                .commit();
        // IMPORTANT: This step is required to ensure that on rotation the Fragment is NOT created AGAIN by this Activity
        // By default, the Fragment is restored by Android and Video position can be retrieved by the savedInstanceState
        // Creates fragment only if savedInstanceState is null
        if (findViewById(R.id.linear_layout_two_pane) != null) {
            isTwoPane = true;
            if (savedInstanceState == null) {
                Bakery.StepsList defaultStepDetail = mSelectedRecipeStepsList.get(0);
                DetailStepFragment detailStepFragment = new DetailStepFragment();
                detailStepFragment.setCurrentRecipeStep(defaultStepDetail);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.video_container, detailStepFragment)
                        .commit();
            }
        } else {
            isTwoPane = false;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onRecipeStepSelected(Bakery.StepsList clickedRecipeStep) {
        if (isTwoPane) {
            displayUpdatedFragment(clickedRecipeStep);
        } else {
            Intent intent = new Intent(this, DetailStepActivity.class);
            RecipeStepsContainer container = new RecipeStepsContainer(mSelectedRecipeStepsList, clickedRecipeStep);
            intent.putExtra(BakeryConstants.KEY_RECIPE_STEP_LIST, container);
            startActivity(intent);
        }
    }

    public void displayUpdatedFragment(Bakery.StepsList recipeStep) {
        DetailStepFragment detailStepFragment = new DetailStepFragment();
        detailStepFragment.setCurrentRecipeStep(recipeStep);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.video_container, detailStepFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNextButtonClicked(Bakery.StepsList clickedRecipeStep) {
        int mRecipeStepId = clickedRecipeStep.getId();
        if (mRecipeStepId < mSelectedRecipeStepsList.size() - 1) {
            mRecipeStepId = mRecipeStepId + 1;
            mCurrentRecipeStep = mSelectedRecipeStepsList.get(mRecipeStepId);
            displayUpdatedFragment(mCurrentRecipeStep);
        } else {
            Toast.makeText(this, R.string.no_more_step, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPreviousButtonClicked(Bakery.StepsList clickedRecipeStep) {
        int mRecipeStepId = clickedRecipeStep.getId();
        if (mRecipeStepId < mSelectedRecipeStepsList.size() && mRecipeStepId != 0) {
            mRecipeStepId = mRecipeStepId - 1;
            mCurrentRecipeStep = mSelectedRecipeStepsList.get(mRecipeStepId);
            displayUpdatedFragment(mCurrentRecipeStep);
        } else {
            Toast.makeText(this, R.string.this_is_step_one, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddWidget() {
        Toast.makeText(this,R.string.appwidget_created,Toast.LENGTH_SHORT).show();
        IngredientService.startActionAddIngredientWidget(this, mSelectedBakeryRecipe);
    }

    /**
     * This is done to ensure that the Fragment (on rotation) is not created twice.
     *
     * @param outState - the saved bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // The below boolean variable is just a dummy one to let this activity know that
        // a fragment is already created and the activity NEED NOT add or replace a new fragment
        isFragmentCreated = true;
        outState.putBoolean(BakeryConstants.STATE_OF_FRAGMENT, isFragmentCreated);
    }
}
