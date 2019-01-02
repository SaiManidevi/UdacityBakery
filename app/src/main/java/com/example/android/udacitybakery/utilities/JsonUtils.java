package com.example.android.udacitybakery.utilities;

import android.util.Log;

import com.example.android.udacitybakery.model.Bakery;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    /**
     * This method receives the String Json Response from BakeryViewModel and
     * parses the Json data using the GSON Library into a List<Bakery> items
     *
     * @param JsonResponse - The Response data retrieved from the given Url
     * @return - List of Bakery items i.e the parsed Json Array having elements of type Bakery
     * @throws - JSONException
     */
    public static List<Bakery> getBakeryList(String JsonResponse) throws JSONException {
        List<Bakery> bakeryList = new ArrayList<>();
        Gson gson = new Gson();
        JSONArray results = new JSONArray(JsonResponse);

        if (results.length() > 0) {
            Type bakeryType = new TypeToken<ArrayList<Bakery>>() {
            }.getType();
            bakeryList = gson.fromJson(results.toString(), bakeryType);
        }
        return bakeryList;
    }
}
