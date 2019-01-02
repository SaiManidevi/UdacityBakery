package com.example.android.udacitybakery.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.android.udacitybakery.R;
import com.example.android.udacitybakery.SimpleIdlingResource;
import com.example.android.udacitybakery.adapters.BakeryAdapter;
import com.example.android.udacitybakery.databinding.ActivityMainBinding;
import com.example.android.udacitybakery.model.Bakery;
import com.example.android.udacitybakery.utilities.BakeryViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity implements BakeryAdapter.BakeryItemClickListener,
        BakeryViewModel.BakeryCallBack {
    private BakeryAdapter mBakeryAdapter;
    private ActivityMainBinding binding;
    private GridLayoutManager gridLayoutManager;
    private BakeryViewModel mViewModel;
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean mTwoPane = false;
    private int columnNum;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.progressBar.setVisibility(View.VISIBLE);
        // Finds out if the app is displayed on a Tablet or on a Phone
        if (findViewById(R.id.tablet_layout) != null) {
            mTwoPane = true;
        }
        // Change the column number for the RecyclerView's GridLayoutManager based on the device
        if (mTwoPane)
            columnNum = 3;
        else
            columnNum = 1;

        gridLayoutManager = new GridLayoutManager(
                this, columnNum);
        mBakeryAdapter = new BakeryAdapter(this);
        binding.recyclerviewMaster.setLayoutManager(gridLayoutManager);
        binding.recyclerviewMaster.setAdapter(mBakeryAdapter);
        binding.recyclerviewMaster.setSaveEnabled(true);
        // Initialize the ViewModel class - BakeryViewModel
        mViewModel = ViewModelProviders.of(this).get(BakeryViewModel.class);
        // Initialize Idling Resource - (used only for testing)
        getIdlingResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getBakeryList(MainActivity.this, mIdlingResource);
    }

    /**
     * onClick method for the RecyclerView items i.e all the Recipes available
     * Clicking on a recipe sends an intent to open the RecipeActivity class
     * @param clickedBakeryRecipe - The recipe item that is being clicked by the user and sent as extra
     */
    @Override
    public void onBakeryItemClick(Bakery clickedBakeryRecipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, clickedBakeryRecipe);
        startActivity(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource(){
        if (mIdlingResource == null){
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    /**
     * This method gets the recipe data after making the network calls from the BakeryViewModel
     * After receiving the data, check if it is not null and set the adapter.
     * @param bakeryList - the list of Recipes fetched from the given url
     */
    @Override
    public void onDoneLoadingBakeryData(List<Bakery> bakeryList) {
        binding.progressBar.setVisibility(View.GONE);
        if (bakeryList == null){
            Toast.makeText(this, getString(R.string.unable_to_retrieve), Toast.LENGTH_SHORT).show();
        }
        else
        mBakeryAdapter.setBakeryRecipes(bakeryList);
    }
}
