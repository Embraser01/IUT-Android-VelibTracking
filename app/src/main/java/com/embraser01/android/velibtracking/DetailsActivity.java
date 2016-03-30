package com.embraser01.android.velibtracking;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.embraser01.android.velibtracking.models.Station;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Station mItem;
    private MenuItem item_go_to = null;

    public final static float ZOOM_FACTOR = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        if (appBarLayout != null) {
            appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                @Override
                public void onStateChanged(AppBarLayout appBarLayout, State state) {
                    if(item_go_to == null) return;

                    if(state.equals(State.COLLAPSED) && !item_go_to.isVisible()){
                        item_go_to.setVisible(true);
                    } else if(state.equals(State.EXPANDED) && item_go_to.isVisible()){
                        item_go_to.setVisible(false);
                    }
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchMapActivity();
                }
            });
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mItem = getIntent().getParcelableExtra("station_detail");

        TextView name = (TextView) findViewById(R.id.details_name);
        name.setText(mItem.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        item_go_to = menu.findItem(R.id.action_go_to);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_go_to){
            launchMapActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Add a marker in Sydney and move the camera
        LatLng station = new LatLng(mItem.getPosition_lat(), mItem.getPosition_lng());
        mMap.addMarker(new MarkerOptions().position(station).title(mItem.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(station, ZOOM_FACTOR));


    }

    public void launchMapActivity() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mItem.getPosition_lat() + "," + mItem.getPosition_lng() + "&mode=b");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
