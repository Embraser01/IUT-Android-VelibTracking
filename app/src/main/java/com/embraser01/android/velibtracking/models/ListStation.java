package com.embraser01.android.velibtracking.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Marc-Antoine on 15/03/2016.
 */
public class ListStation {

    private ArrayList<Station> stations = null;

    public ListStation() {

        stations = new ArrayList<>();
    }


    public void add(JSONArray jsonArray) {
        Station tmp;
        JSONObject tmp2;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                tmp2 = jsonArray.getJSONObject(i);

                tmp = new Station(tmp2.getInt("number"),
                        tmp2.getString("contract_name"),
                        tmp2.getString("name"),
                        tmp2.getString("address"),
                        tmp2.getJSONObject("position").getDouble("lat"),
                        tmp2.getJSONObject("position").getDouble("lng"),
                        tmp2.getBoolean("banking"),
                        tmp2.getBoolean("bonus"),
                        tmp2.getString("status"),
                        tmp2.getInt("bike_stands"),
                        tmp2.getInt("available_bike_stands"),
                        tmp2.getInt("available_bikes"),
                        Timestamp.valueOf(tmp2.getString("last_update"))
                );

                if(!this.stations.contains(tmp)){
                    this.stations.add(tmp);
                } else {
                    // TODO Handle update
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
