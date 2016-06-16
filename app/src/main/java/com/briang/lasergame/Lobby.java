package com.briang.lasergame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.briang.lasergame.Connections.AsyncResponse;
import com.briang.lasergame.Connections.OkHttpGet;
import com.briang.lasergame.Connections.OkHttpPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Lobby extends AppCompatActivity implements AsyncResponse {


    private Boolean firstRun = true;
    private String roomName;
    private String password;
    private String deviceId;
<<<<<<< HEAD
    private String[] players;
    private Timer waitingTimer;

=======
>>>>>>> parent of c13c9b7... push

    @BindView(R.id.playerlist)
    ListView playerlist;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.include)
    Toolbar toolbar;
    @BindView(R.id.refresh)
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        roomName = intent.getExtras().getString("roomName");
        password = intent.getExtras().getString("password");
        deviceId = intent.getExtras().getString("deviceId");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


<<<<<<< HEAD
        title.setText(roomName);
=======

>>>>>>> parent of c13c9b7... push

        title.setText(roomName);
        

        getRequest();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                getRequest();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                swipe.setRefreshing(false);
            }
        });

<<<<<<< HEAD
        playerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), game.class);
                intent.putExtra("Roomname", players[i]);


            }
        });
        runInBackground();

=======
>>>>>>> parent of c13c9b7... push
    }


    private void runInBackground() {
        waitingTimer = new Timer();
        waitingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        getState();
                    }
                });
            }
        }, 0, 1000);
    }

    private void cancelRunInBackground() {
        waitingTimer.cancel();
    }

    @Override
    protected void onPause() {
        cancelRunInBackground();
        Log.d("remove", "playerRemoved");
        postRequest();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        cancelRunInBackground();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void processFinish(String output) {
        Log.d("players", output);


<<<<<<< HEAD
        if (!output.contains("<html>"))
            try {
                JSONArray arr = new JSONArray(output);
                players = new String[arr.length()];
=======
        if(!output.contains("<html>"))
        try {
            JSONArray arr = new JSONArray(output);
            String[] players = new String[arr.length()];
>>>>>>> parent of c13c9b7... push


                for (int i = 0; i < arr.length(); i++) {

                    JSONObject obj = arr.getJSONObject(i);
                    players[i] = obj.getString("playerid");

                }

                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.playerlist, R.id.playerName, players);


                itemsAdapter.setNotifyOnChange(true);
                playerlist.setAdapter(itemsAdapter);

<<<<<<< HEAD

            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
=======
                firstRun = false;
>>>>>>> parent of c13c9b7... push


    public void getRequest() {
        OkHttpGet okHttpGet = new OkHttpGet();
        okHttpGet.delegate = this;
        okHttpGet.execute(okHttpGet.getPlayers(roomName));
    }

    public void getState() {
        OkHttpGet okHttpGet = new OkHttpGet();
        okHttpGet.delegate = this;
//        okHttpGet.execute(okHttpGet.getState(roomName));
    }

    public void postRequest() {
        OkHttpPost okHttpPost = new OkHttpPost();
        okHttpPost.delegate = this;
        okHttpPost.execute(okHttpPost.removePlayer(roomName, password, deviceId));
    }
}
