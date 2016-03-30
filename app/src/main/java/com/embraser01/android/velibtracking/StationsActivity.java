package com.embraser01.android.velibtracking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.embraser01.android.velibtracking.models.ListStation;
import com.embraser01.android.velibtracking.models.Station;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;

public class StationsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float ZOOM_FACTOR = 12;
    private GoogleMap mMap;

    private ArrayList<Station> stations;
    private Station startStation = null;
    private Station endStation = null;

    private ClusterManager<StationItem> mClusterManager;

    private StationItem clickedClusterItem = null;
    private Cluster<StationItem> clickedCluster = null;

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

        startStation = getIntent().getParcelableExtra("start_station");
        endStation = getIntent().getParcelableExtra("end_station");

        if ((startStation == null && endStation != null) || (startStation != null && endStation == null)) {
            startStation = null;
            endStation = null;
        }
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
        setUpClusterer();
    }

    private void setUpClusterer() {

        mClusterManager = new ClusterManager<>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new StationItemAdapter());
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new ClusterAdapter());

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<StationItem>() {
            @Override
            public boolean onClusterItemClick(StationItem stationItem) {
                if(stationItem == clickedClusterItem){
                    Intent intent = new Intent(StationsActivity.this, DetailsActivity.class);
                    intent.putExtra("station_detail", clickedClusterItem.mStation);
                    startActivity(intent);
                } else {
                    clickedClusterItem = stationItem;
                    clickedCluster = null;
                }
                return false;
            }
        });

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<StationItem>() {
            @Override
            public boolean onClusterClick(Cluster<StationItem> cluster) {
                clickedCluster = cluster;
                clickedClusterItem = null;
                return false;
            }
        });

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        StationItem offsetItem = null;
        for (int i = 0; i < stations.size(); i++) {
            offsetItem = new StationItem(stations.get(i));
            mClusterManager.addItem(offsetItem);
        }

        mClusterManager.cluster();

        if (startStation != null) {
            new ItineraireTask(this,
                    mMap,
                    new LatLng(startStation.getPosition_lat(), startStation.getPosition_lng()),
                    new LatLng(endStation.getPosition_lat(), endStation.getPosition_lng())
            ).execute();
        } else if (offsetItem != null) {
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


    public class StationItemAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mView;

        public StationItemAdapter() {
            mView = getLayoutInflater().inflate(R.layout.map_info_window_dialog, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            if (clickedClusterItem == null) return null;

            TextView name = (TextView) mView.findViewById(R.id.map_info_name);
            TextView plus = (TextView) mView.findViewById(R.id.map_info_plus);

            name.setText(clickedClusterItem.mStation.getName());
            plus.setText(clickedClusterItem.mStation.getStatus() + " - " + clickedClusterItem.mStation.getAvailable_bikes() + " / " + clickedClusterItem.mStation.getBike_stands());

            mView.setClickable(true);
            mView.setEnabled(true);
            mView.setFocusable(true);
            mView.setFocusableInTouchMode(true);

            return mView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    public class ClusterAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mView;

        public ClusterAdapter() {
            mView = getLayoutInflater().inflate(R.layout.map_info_window_dialog, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            if (clickedCluster == null) return null;

            TextView name = (TextView) mView.findViewById(R.id.map_info_name);
            TextView plus = (TextView) mView.findViewById(R.id.map_info_plus);


            name.setText(clickedCluster.getSize() + " " + getResources().getString(R.string.title_activity_stations));

            int nb_open = 0;
            int nb_bikes = 0;
            int nb_available_bikes = 0;

            for(StationItem item : clickedCluster.getItems()){
                nb_bikes += item.mStation.getBike_stands();
                nb_available_bikes += item.mStation.getAvailable_bikes();

                if(item.mStation.getStatus().equals("OPEN")) nb_open++;
            }

            plus.setText(nb_open + " OPEN  -  " + nb_available_bikes + "/" + nb_bikes);
            return mView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}
