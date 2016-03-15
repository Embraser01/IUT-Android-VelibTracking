package com.embraser01.android.velibtracking.net;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.embraser01.android.velibtracking.models.ListStation;

import org.json.JSONArray;


public class NetTask_Volley {

    public static void getStations(String contract, Context context, final ListStation listStation) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        JsonArrayRequest data = new JsonArrayRequest(Request.Method.GET,
                NetTask_Volley.getUri(contract),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        listStation.add(jsonArray);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Log.e("VOLLEY", error.toString());

                    }
                });

        // Add the request to the RequestQueue.
        queue.add(data);
        queue.start();
    }

    private static String getUri(String contract) {
        return new Uri.Builder()
                .scheme("https")
                .authority("api.jcdecaux.com")
                .path("vls/v1/stations")
                .appendQueryParameter("contract", (contract == null) ? "Lyon" : contract)
                .appendQueryParameter("apiKey", "80ddc1cff88b645913acbfbfeeea8897a142b951")
                .build().toString();
    }
}
