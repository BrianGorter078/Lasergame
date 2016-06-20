package com.briang.lasergame;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.briang.lasergame.Connections.AsyncResponse;
import com.briang.lasergame.Connections.OkHttpGet;
import com.briang.lasergame.Connections.OkHttpPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Lobby extends AppCompatActivity implements AsyncResponse {

    private String roomName;
    private String password;
    private String deviceId;
    private String[] players;
    private Timer waitingTimer;
    BluetoothDevice device;


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

        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        device = getIntent().getParcelableExtra("device");

        title.setText(roomName);


        getPlayers();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                swipe.animate();
                getPlayers();
                swipe.setRefreshing(false);
            }
        });

        playerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), Game.class);
                intent.putExtra("Roomname", players[i]);



            }
        });
        runInBackground();

    }


    private void runInBackground() {
        Log.d("run" , "run");
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
        Log.d("cancel", "cancel");
        waitingTimer.cancel();
    }


    @Override
    protected void onStop() {
        Log.d("OnStop" , "Onstop");
        cancelRunInBackground();
        postRequest();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d("OnRestart" , "Onrestart");
        runInBackground();
        postRequest();
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        cancelRunInBackground();
        super.onBackPressed();
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

        if (output.equals("true") || output.equals("false") )
        {
            if(output.equals("false"))
            {
                startGame();
            }
            else{}
        }

        else if (!output.contains("<html>"))
            try {
                JSONArray arr = new JSONArray(output);
                players = new String[arr.length()];


                for (int i = 0; i < arr.length(); i++) {

                    JSONObject obj = arr.getJSONObject(i);
                    players[i] = obj.getString("playerid");

                    if (players[i].contains(deviceId))
                    {
                        players[i] = "You";
                    }
                }

                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.playerlist, R.id.playerName, players);
                playerlist.setAdapter(itemsAdapter);


            } catch (JSONException e) {

            }
    }


    public void getPlayers() {
        OkHttpGet okHttpGet = new OkHttpGet();
        okHttpGet.delegate = this;
        okHttpGet.execute(okHttpGet.getPlayers(roomName));
    }

    public void getState() {
        OkHttpGet okHttpGet = new OkHttpGet();
        okHttpGet.delegate = this;
        okHttpGet.execute(okHttpGet.getState(roomName));
    }

    public void postRequest() {
        OkHttpPost okHttpPost = new OkHttpPost();
        okHttpPost.delegate = this;
        okHttpPost.execute(okHttpPost.removePlayer(roomName, password, deviceId));
    }

    public void postStartGame(){
        OkHttpPost okHttpPost = new OkHttpPost();
        okHttpPost.delegate = this;
        okHttpPost.execute(okHttpPost.startGame(roomName));
    }

    public void StartGame(View v) {
     startGame();

    }

    public void startGame(){

        postStartGame();

        Intent intent = new Intent(this, Game.class);
        intent.putExtra("roomName", roomName);
        intent.putExtra("device", device);
        startActivity(intent);
        finish();
        cancelRunInBackground();
    }
}