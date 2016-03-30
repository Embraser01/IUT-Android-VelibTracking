package com.embraser01.android.velibtracking;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.embraser01.android.recyclerview.DividerItemDecoration;
import com.embraser01.android.recyclerview.LinearLayoutManager;
import com.embraser01.android.recyclerview.OnListFragmentInteractionListener;
import com.embraser01.android.velibtracking.models.ListStation;
import com.embraser01.android.velibtracking.models.Station;
import com.embraser01.android.velibtracking.net.NetTask_Volley;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener, SearchView.OnQueryTextListener {

    public final static String PREF_FILE = "com.embraser01.android.velibtracking_preferences";

    private ListStation listStation;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private StationListViewAdapter mAdapter;
    private SwipeRefreshLayout mRefresh;

    private ProgressBar progressBar;
    private String currentContract = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, StationsActivity.class);
                intent.putExtra("map_stations", listStation);
                startActivity(intent);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRefresh.setColorSchemeColors(
                ColorGenerator.MATERIAL.getColor(0),
                ColorGenerator.MATERIAL.getColor(1),
                ColorGenerator.MATERIAL.getColor(2),
                ColorGenerator.MATERIAL.getColor(3));

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkConnection()) updateItems();
            }
        });

        initRecyclerView();

    }

    private boolean checkConnection() {
        if (!isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle(R.string.dialog_connection)
                    .setMessage(R.string.dialog_connection_msg)
                    .setPositiveButton("OK", null)
                    .show();

            mRefresh.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            return false;
        } else {
            return true;
        }
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public void updateContract() {
        currentContract = getSharedPreferences(PREF_FILE, MODE_PRIVATE).getString("contract_list", null);

        if (currentContract == null) currentContract = "Lyon";
    }


    private void updateItems() {
        updateContract();

        listStation = new ListStation(this);

        NetTask_Volley.getStations(currentContract, this, listStation, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(MainActivity.this.mRecyclerView, R.string.error + " " + volleyError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.station_list);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        listStation = getIntent().getParcelableExtra("load_data");
        if (listStation != null) {
            updateContract();
            listStation.setContext(this);
            progressBar.setVisibility(View.GONE);
            listStation.loadFavPref(currentContract);
        } else if (checkConnection()) {
            updateItems();
        } else {
            listStation = new ListStation(this);
        }

        mAdapter = new StationListViewAdapter(this, listStation.getStations(), this, listStation.getFavList());

        mRecyclerView.setAdapter(mAdapter);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1337 && resultCode == RESULT_OK) {
            this.mRefresh.setRefreshing(true);
            this.updateItems();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, 1337);
            return true;
        }
        if (id == R.id.action_filter) {
            item.setChecked(!item.isChecked());
            final List<Station> filteredModelList = filterFav(listStation.getStations(), item.isChecked());
            mAdapter.animateTo(filteredModelList);
            mRecyclerView.scrollToPosition(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateData() {
        progressBar.setVisibility(View.INVISIBLE);
        listStation.loadFavPref(currentContract);
        mAdapter.updateModels(listStation.getStations());
        mAdapter.notifyDataSetChanged();
        mRefresh.setRefreshing(false);
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

    private List<Station> filterFav(List<Station> models, boolean restrictToFav) {

        if (!restrictToFav) return listStation.getStations();

        final List<Station> filteredModelList = new ArrayList<>();

        for (Station model : models) if (model.isFav()) filteredModelList.add(model);

        return filteredModelList;
    }

    public void saveFavList() {
        listStation.saveFavPref(currentContract);
    }
}
