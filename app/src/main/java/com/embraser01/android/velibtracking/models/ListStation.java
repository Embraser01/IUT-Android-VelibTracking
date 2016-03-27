package com.embraser01.android.velibtracking.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.embraser01.android.velibtracking.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class ListStation implements Parcelable {

    private ArrayList<Station> stations = null;
    private Context context;

    public ListStation(Context context) {

        this.context = context;
        stations = new ArrayList<>();
    }

    public ListStation(Parcel in) {
        int size = in.readInt();
        this.stations = new ArrayList<>(size);
        for(int i = 0; i < size; i++) this.stations.add(in.<Station>readParcelable(ListStation.class.getClassLoader()));

        this.context = null;
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
                        new Timestamp(tmp2.getLong("last_update"))
                );

                if (!this.stations.contains(tmp)) {
                    this.stations.add(tmp);
                } else {
                    // TODO Handle update
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(context != null) ((MainActivity) context).updateData();
    }


    public ArrayList<Station> getStations() {
        return stations;
    }

    public int count() {
        return stations.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.stations.size());
        for(int i = 0; i < stations.size(); i++) dest.writeParcelable(this.stations.get(i), flags);
    }

    public static final Parcelable.Creator<ListStation> CREATOR = new Parcelable.Creator<ListStation>() {
        public ListStation createFromParcel(Parcel in) {
            return new ListStation(in);
        }

        public ListStation[] newArray(int size) {
            return new ListStation[size];
        }

    };

    public void setStations(List<Station> stations) {
        this.stations = new ArrayList<>(stations);
    }
}
