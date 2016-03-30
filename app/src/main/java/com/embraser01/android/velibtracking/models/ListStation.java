package com.embraser01.android.velibtracking.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.embraser01.android.velibtracking.MainActivity;
import com.embraser01.android.velibtracking.OnUpdateStationList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class ListStation implements Parcelable {

    private ArrayList<Station> stations = null;
    private Context context;
    private Set<String> fav_list;

    public ListStation() {
        this.context = null;
        stations = new ArrayList<>();
    }

    public ListStation(Context context) {

        this.context = context;
        stations = new ArrayList<>();
    }

    public ListStation(Parcel in) {
        int size = in.readInt();
        this.stations = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            this.stations.add(in.<Station>readParcelable(ListStation.class.getClassLoader()));

        this.context = null;
    }

    public void loadFavPref(String contract) {
        if (context == null) return;

        fav_list = context.getSharedPreferences(MainActivity.PREF_FILE, MainActivity.MODE_PRIVATE).getStringSet("fav_list_" + contract, null);

        if (fav_list != null) {
            for (String item : fav_list) {
                int num = Integer.parseInt(item);

                for (Station tmp : stations) {
                    if (tmp.getNumber() == num) {
                        tmp.switchFav();
                        break;
                    }
                }
            }
        }
    }

    public void saveFavPref(String contract) {
        if (context == null) return;

        SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.PREF_FILE, MainActivity.MODE_PRIVATE).edit();
        editor.remove("fav_list_" + contract);

        fav_list = new HashSet<>();
        for (Station tmp : stations) if (tmp.isFav()) fav_list.add(Integer.toString(tmp.getNumber()));

        editor.putStringSet("fav_list_" + contract, fav_list).apply();

    }

    public Set<String> getFavList() {
        return fav_list;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void add(JSONArray jsonArray, OnUpdateStationList callback) {
        Station tmp;
        JSONObject tmp2;

        this.stations = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                tmp2 = jsonArray.getJSONObject(i);

                tmp = new Station(tmp2.getInt("number"),
                        tmp2.getString("contract_name"),
                        tmp2.getString("name"), //REGEX POUR LYON... .replaceAll("^(\\S*\\s){2}",""),
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

                this.stations.add(tmp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (callback != null) callback.updateData();
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
        for (int i = 0; i < stations.size(); i++) dest.writeParcelable(this.stations.get(i), flags);
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
