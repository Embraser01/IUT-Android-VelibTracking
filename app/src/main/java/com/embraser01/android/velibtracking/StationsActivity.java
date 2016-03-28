package com.embraser01.android.velibtracking;

import android.content.Intent;
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
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class StationsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float ZOOM_FACTOR = 12;
    private GoogleMap mMap;

    private ArrayList<Station> stations;

    private ClusterManager<StationItem> mClusterManager;

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

        setUpClusterer();

    }

    private void setUpClusterer() {

        // Position the map.
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom();

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        StationItem offsetItem = null;
        for (int i = 0; i < stations.size(); i++) {
            offsetItem = new StationItem(stations.get(i));
            mClusterManager.addItem(offsetItem);
            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<StationItem>() {
                @Override
                public boolean onClusterItemClick(StationItem stationItem) {

                    Intent intent = new Intent(StationsActivity.this, DetailsActivity.class);
                    intent.putExtra("station_detail", stationItem.mStation);
                    startActivity(intent);
                    return false;
                }
            });
        }

        new ItineraireTask(this,
                mMap,
                new LatLng(stations.get(1).getPosition_lat(), stations.get(1).getPosition_lng()),
                new LatLng(stations.get(0).getPosition_lat(), stations.get(0).getPosition_lng())
        ).execute();

        if (offsetItem != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(offsetItem.getPosition(), ZOOM_FACTOR));
        }
    }


    public class StationItem implements ClusterItem {
        private Station mStation;

        public StationItem(Station station) {
            mStation = station;
        }

        @Override
        public LatLng getPosition() {
            return new LatLng(mStation.getPosition_lat(), mStation.getPosition_lng());
        }
    }
}
