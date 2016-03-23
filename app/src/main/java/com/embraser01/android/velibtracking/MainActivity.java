package com.embraser01.android.velibtracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.embraser01.android.recyclerview.DividerItemDecoration;
import com.embraser01.android.recyclerview.LinearLayoutManager;
import com.embraser01.android.recyclerview.OnListFragmentInteractionListener;
import com.embraser01.android.velibtracking.models.ListStation;
import com.embraser01.android.velibtracking.models.Station;
import com.embraser01.android.velibtracking.net.NetTask_Volley;

public class MainActivity extends AppCompatActivity
        implements OnListFragmentInteractionListener {


    private ListStation listStation;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;


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

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
//                listStation.deleteComputer(((StationListViewAdapter.ViewHolder) viewHolder).mItem);
//                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//
//                Snackbar.make(viewHolder.itemView, "You swipe to the " + ((direction == ItemTouchHelper.LEFT) ? "left" : "right"), Snackbar.LENGTH_LONG).setAction(R.string.main_undo_remove, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mAdapter.notifyItemInserted(listStation.undo());
//                    }
//                }).setCallback(new Snackbar.Callback() {
//                    @Override
//                    public void onDismissed(Snackbar snackbar, int event) {
//                        listStation.clearPending();
//                    }
//                }).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);

        itemTouchHelper.attachToRecyclerView(this.mRecyclerView);

        mAdapter = new StationListViewAdapter(this, listStation, this);
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
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListFragmentInteraction(Station mItem) {

        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("station_detail", mItem);
        startActivity(intent);
    }
}
