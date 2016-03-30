package com.embraser01.android.velibtracking;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.embraser01.android.velibtracking.models.ListStation;
import com.embraser01.android.velibtracking.net.NetTask_Volley;


public class SplashScreenActivity extends AppCompatActivity {

    private final static int SPLASH_TIME_OUT = 2000;
    private ListStation listStation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                if(listStation != null) i.putExtra("load_data", listStation);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

        loadFirstTimeData();
    }

    private void loadFirstTimeData() {

        if (!isConnected()) return;

        String contract = getSharedPreferences(MainActivity.PREF_FILE, MODE_PRIVATE).getString("contract_list", null);

        if (contract == null) contract = "Lyon";

        listStation = new ListStation();

        NetTask_Volley.getStations(contract, this, listStation, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listStation = null;
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
