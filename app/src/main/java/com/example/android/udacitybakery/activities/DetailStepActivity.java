package com.example.android.udacitybakery.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.udacitybakery.R;
import com.example.android.udacitybakery.fragments.DetailStepFragment;
import com.example.android.udacitybakery.model.Bakery;
import com.example.android.udacitybakery.utilities.BakeryConstants;
import com.example.android.udacitybakery.model.RecipeStepsContainer;

import java.util.List;

public class DetailStepActivity extends AppCompatActivity implements DetailStepFragment.OnButtonClickListener {
    private List<Bakery.StepsList> stepsLists;
    private Bakery.StepsList currentRecipeStep;
    private Intent recipeActivityIntent;
    private int mRecipeStepId;
    DetailStepFragment detailStepFragment;
    private boolean isFragmentCreated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_step);
        recipeActivityIntent = getIntent();
        // Check if the intent received from RecipeActivty is not null and has the Extra with the required KEY
        if (recipeActivityIntent != null) {
            if (recipeActivityIntent.hasExtra(BakeryConstants.KEY_RECIPE_STEP_LIST)) {
                // get the List of all steps for the selected Recipe as well as the clicked Recipe Step
                // This is achieved with the helper class - RecipeContainer (which contains the List of Steps & ClickedStep)
                RecipeStepsContainer container = (RecipeStepsContainer) recipeActivityIntent
                        .getSerializableExtra(BakeryConstants.KEY_RECIPE_STEP_LIST);
                stepsLists = container.getClickedRecipeStepsList();
                currentRecipeStep = container.getClickedStep();
                // set the actionbar's title with the current recipe step's short description
                getSupportActionBar().setTitle(currentRecipeStep.getShortDescription());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        // IMPORTANT: This step is required to ensure that on rotation the Fragment is NOT created AGAIN by this Activity
        // By default, the Fragment is restored by Android and Video position can be retrieved by the savedInstanceState

        // Creates fragment only if savedInstanceState is null
        if (savedInstanceState == null)
            displayFragment(false);
        else {
            detailStepFragment = new DetailStepFragment();
            detailStepFragment.setCurrentRecipeStep(currentRecipeStep);
        }
    }

    /**
     * Checks if the call to this method is from onCreate i.e (indicating that the fragment is created for the first time)
     * Or if the call is from the Next or Previous button. If from button, instead of adding a new fragment,
     * we will replace the existing fragment
     *
     * @param fromButton - boolean variable to determine from where the call to this method is made
     */
    public void displayFragment(boolean fromButton) {
        detailStepFragment = new DetailStepFragment();
        detailStepFragment.setCurrentRecipeStep(currentRecipeStep);
        if (!fromButton) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.video_container, detailStepFragment)
                    .commit();

        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.video_container, detailStepFragment)
                    .commit();
        }
        getSupportActionBar().setTitle(currentRecipeStep.getShortDescription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method that works for the Next button's functionality
     *
     * @param clickedRecipeStep - The current recipe step from where the button is clicked
     */
    @Override
    public void onNextButtonClicked(Bakery.StepsList clickedRecipeStep) {
        mRecipeStepId = clickedRecipeStep.getId();
        if (mRecipeStepId < stepsLists.size() - 1) {
            mRecipeStepId = mRecipeStepId + 1;
            currentRecipeStep = stepsLists.get(mRecipeStepId);
            displayFragment(true);
        } else {
            Toast.makeText(this, R.string.no_more_step, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Helper method that works for the Previous button's functionality
     *
     * @param clickedRecipeStep - The current recipe step from where the button is clicked
     */
    @Override
    public void onPreviousButtonClicked(Bakery.StepsList clickedRecipeStep) {
        mRecipeStepId = clickedRecipeStep.getId();
        if (mRecipeStepId < stepsLists.size() && mRecipeStepId != 0) {
            mRecipeStepId = mRecipeStepId - 1;
            currentRecipeStep = stepsLists.get(mRecipeStepId);
            displayFragment(true);
        } else {
            Toast.makeText(this, R.string.this_is_step_one, Toast.LENGTH_SHORT).show();
        }
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
