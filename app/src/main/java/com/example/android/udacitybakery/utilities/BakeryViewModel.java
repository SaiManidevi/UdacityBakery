package com.example.android.udacitybakery.utilities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.udacitybakery.SimpleIdlingResource;
import com.example.android.udacitybakery.model.Bakery;

import org.json.JSONException;

import java.util.List;

public class BakeryViewModel extends AndroidViewModel {
    private static List<Bakery> mBakeryList;
    private static final String TAG = "BakeryViewModel";

    public BakeryViewModel(@NonNull Application application) {
        super(application);
    }

    public interface BakeryCallBack {
        void onDoneLoadingBakeryData(List<Bakery> bakeryList);
    }

    /**
     * This method makes the Network call for the given Url with the help of Volley Library
     * The response data received is then sent to JsonUtils to convert it into a List<Bakery> items
     *
     * @param callBack       - (for testing)
     * @param idlingResource - (for testing)
     */
    public void getBakeryList(final BakeryCallBack callBack, @Nullable final SimpleIdlingResource idlingResource) {
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
        StringRequest request = new StringRequest(Request.Method.GET, BakeryConstants.BAKERY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.v(TAG, "Received response: ---> " + response);
                            mBakeryList = JsonUtils.getBakeryList(response);
                            if (callBack != null) {
                                callBack.onDoneLoadingBakeryData(mBakeryList);
                                if (idlingResource != null) {
                                    idlingResource.setIdleState(true);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(TAG, "Volley - onErrorResponse: " + error);
            }
        });

        queue.add(request);
    }
}
