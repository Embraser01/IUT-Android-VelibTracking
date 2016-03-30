package com.embraser01.android.velibtracking.net;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.embraser01.android.velibtracking.OnUpdateStationList;
import com.embraser01.android.velibtracking.SettingsActivity;
import com.embraser01.android.velibtracking.models.City;
import com.embraser01.android.velibtracking.models.ListStation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NetTask_Volley {


    public static void getStations(String contract, final Context context, final ListStation listStation, final OnUpdateStationList callback, Response.ErrorListener errorListener) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        JsonArrayRequest data = new JsonArrayRequest(Request.Method.GET,
                NetTask_Volley.getUri(contract),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        listStation.add(jsonArray, callback);
                    }
                },
                errorListener);

        // Add the request to the RequestQueue.
        queue.add(data);
        queue.start();
    }


    public static void getContract(final Context context, final ArrayList<City> contracts, Response.ErrorListener errorListener) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        JsonArrayRequest data = new JsonArrayRequest(Request.Method.GET,
                NetTask_Volley.getUriContracts(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        /*JSONObject tmp2;
                        JSONArray tmp3;
                        City tmp;
                        ArrayList<String> tmp_cities;


                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                tmp_cities = new ArrayList<>();
                                tmp2 = jsonArray.getJSONObject(i);

                                tmp3 = tmp2.getJSONArray("cities");

                                for (int j = 0; j < tmp3.length(); i++)
                                    tmp_cities.add(tmp3.getString(i));

                                tmp = new City(tmp_cities, tmp2.getString("commercial_name"), tmp2.getString("country_code"), tmp2.getString("name"));

                                contracts.add(tmp);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }*/

                        CharSequence data[] = new CharSequence[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                data[i] = jsonArray.getJSONObject(i).getString("name");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ((SettingsActivity) context).setContractList(data);
                    }
                },
                errorListener);

        // Add the request to the RequestQueue.
        queue.add(data);
        queue.start();
    }

    private static String getUri(String contract) {
        return new Uri.Builder()
                .scheme("https")
                .authority("api.jcdecaux.com")
                .path("vls/v1/stations")
                .appendQueryParameter("contract", contract)
                .appendQueryParameter("apiKey", "80ddc1cff88b645913acbfbfeeea8897a142b951")
                .build().toString();
    }

    private static String getUriContracts() {
        return new Uri.Builder()
                .scheme("https")
                .authority("api.jcdecaux.com")
                .path("vls/v1/contracts")
                .appendQueryParameter("apiKey", "80ddc1cff88b645913acbfbfeeea8897a142b951")
                .build().toString();
    }
}
