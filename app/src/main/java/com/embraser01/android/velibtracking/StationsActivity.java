package com.embraser01.android.velibtracking;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.embraser01.android.velibtracking.models.ListStation;
import com.embraser01.android.velibtracking.models.Station;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StationsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    private ArrayList<Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        stations = ((ListStation) getIntent().getParcelableExtra("map_stations")).getStations();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng pos = null;
        String snippet;
        for (int i = 0; i < stations.size(); i++) {
            pos = new LatLng(stations.get(i).getPosition_lat(), stations.get(i).getPosition_lng());
            snippet = getResources().getString(R.string.stations_map_info) + stations.get(i).getAvailable_bikes();
            mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(stations.get(i).getName())
                    .snippet(snippet));
            mMap.setOnInfoWindowClickListener(this);

        }
        if(pos != null) mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for(int i = 0; i < stations.size(); i++){
            if(stations.get(i).getName().equals(marker.getTitle())){
                Intent intent = new Intent(StationsActivity.this, DetailsActivity.class);
                intent.putExtra("station_detail", stations.get(i));
                startActivity(intent);
            }
        }
    }
}
