package com.embraser01.android.velibtracking;

import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.embraser01.android.velibtracking.models.City;

import java.util.ArrayList;

/**
 * Created by Marc-Antoine on 28/03/2016.
 */
public class CitiesAdapter implements ListAdapter, DialogInterface.OnClickListener {

    private ArrayList<City> cities;

    private City selected = null;

    public CitiesAdapter(ArrayList<City> cities) {
        this.cities = cities;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int i) {
        return cities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return cities.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        selected = cities.get(i);
    }


    public City getSelected(){
        return selected;
    }
}
