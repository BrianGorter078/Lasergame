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

    }


    @Override
    protected void onPause() {
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
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void processFinish(String output) {
        Log.d("players", output);


        if(!output.contains("<html>"))
        try {
            JSONArray arr = new JSONArray(output);
            String[] players = new String[arr.length()];



            for(int i = 0; i < arr.length(); i++)
            {

                JSONObject obj = arr.getJSONObject(i);
                players[i] =  obj.getString("playerid");

            }

                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.playerlist, R.id.playerName, players);


                itemsAdapter.setNotifyOnChange(true);
                playerlist.setAdapter(itemsAdapter);

                firstRun = false;




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getRequest(){
        OkHttpGet okHttpGet = new OkHttpGet();
        okHttpGet.delegate = this;
        okHttpGet.execute(okHttpGet.getPlayers(roomName));
    }

    public void postRequest(){
        OkHttpPost okHttpPost = new OkHttpPost();
        okHttpPost.delegate = this;
        okHttpPost.execute(okHttpPost.removePlayer(roomName,password,deviceId));
    }
}
