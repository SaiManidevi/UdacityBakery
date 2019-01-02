package com.example.android.udacitybakery;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.example.android.udacitybakery.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {
    private static final String RECIPE_NUTELLA_PIE = "Nutella Pie";
    private static final String RECIPE_BROWNIES = "Brownies";
    private static final String RECIPE_YELLOW_CAKE = "Yellow Cake";
    private static final String RECIPE_CHEESECAKE = "Cheesecake";
    private static final String RECIPE_BROWNIES_INGREDIENT = "unsalted butter";


    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mainActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    //Checks if all the RecyclerView items have the correct recipes.
    @Test
    public void checkRecyclerView_hasAllRecipes() {
        onView(withId(R.id.recyclerview_master))
                .perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(RECIPE_NUTELLA_PIE))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_master))
                .perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText(RECIPE_BROWNIES))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_master))
                .perform(RecyclerViewActions.scrollToPosition(2));
        onView(withText(RECIPE_YELLOW_CAKE))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_master))
                .perform(RecyclerViewActions.scrollToPosition(3));
        onView(withText(RECIPE_CHEESECAKE))
                .check(matches(isDisplayed()));
    }

    @Test
    public void onRecipeClick_hasIngredients() {
        // Find RecyclerView Item at position 1 (i.e Brownies) and
        // perform action click
        onView(withId(R.id.recyclerview_master))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        // Find Item 1 ingredient (Brownies' Ingredients) and
        // check for an ingredient at some position
        onView(withId(R.id.recyclerview_ingredients))
                .perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText(RECIPE_BROWNIES_INGREDIENT)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
}
