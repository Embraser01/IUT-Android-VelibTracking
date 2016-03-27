package com.embraser01.android.velibtracking;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Marc-Antoine on 23/03/2016.
 */
public class StationInfoMapAdapter extends View implements GoogleMap.InfoWindowAdapter{
    private View v;

    public StationInfoMapAdapter(Context context) {
        super(context);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
