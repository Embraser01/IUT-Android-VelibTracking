package com.embraser01.android.velibtracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.embraser01.android.recyclerview.DividerItemDecoration;
import com.embraser01.android.recyclerview.LinearLayoutManager;
import com.embraser01.android.recyclerview.OnListFragmentInteractionListener;
import com.embraser01.android.velibtracking.models.ListStation;
import com.embraser01.android.velibtracking.models.Station;
import com.embraser01.android.velibtracking.net.NetTask_Volley;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements OnListFragmentInteractionListener, SearchView.OnQueryTextListener {


    private ListStation listStation;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private StationListViewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, StationsActivity.class);
                intent.putExtra("map_stations", listStation);
                startActivity(intent);
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.station_list);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)
        loadStations();

        mAdapter = new StationListViewAdapter(this, listStation.getStations(), this);

        mRecyclerView.setAdapter(mAdapter);
    }

    public void loadStations() {
        listStation = new ListStation(this);

        NetTask_Volley.getStations(null, this, listStation);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateData() {
        mAdapter.updateModels(listStation.getStations());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListFragmentInteraction(Station mItem) {

        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("station_detail", mItem);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        final List<Station> filteredModelList = filter(listStation.getStations(), newText);
        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    private List<Station> filter(List<Station> models, String query) {
        query = query.toLowerCase();

        final List<Station> filteredModelList = new ArrayList<>();
        for (Station model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
