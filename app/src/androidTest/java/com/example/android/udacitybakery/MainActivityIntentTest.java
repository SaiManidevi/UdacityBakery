package com.example.android.udacitybakery;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.udacitybakery.activities.DetailStepActivity;
import com.example.android.udacitybakery.activities.MainActivity;
import com.example.android.udacitybakery.activities.RecipeActivity;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {
    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule = new IntentsTestRule<>(MainActivity.class);
    public IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mainActivityIntentsTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    // Check if RecipeActivity opens when a specific Recipe item is clicked from MainActivity
    @Test
    public void clickRecyclerViewItemIntent_opensRecipeActivity() {
        onView(withId(R.id.recyclerview_master))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeActivity.class.getName()));
    }

    // Checks if ExoPlayer plays video when a specific Recipe Step item
    // (which has the video URL) is clicked.
    @Test
    public void checkIfExoPlayerWorks() {
        // Find the Master RecyclerView (Recipes) from MainActivity
        // and scroll to position 0 (Nutella Pie) and click that recipe item
        onView(withId(R.id.recyclerview_master))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        // Find the Steps RecyclerView (Selected Recipe Steps) from RecipeActivity
        onView(withId(R.id.linear_layout_recipe)).perform(swipeUp());
        onView(withId(R.id.recyclerview_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Check if clicking on a specific step ExoPlayer plays video (if url exist)
        onView(allOf(withId(R.id.exo_player_view),
                withClassName(is(SimpleExoPlayerView.class.getName()))));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
}
