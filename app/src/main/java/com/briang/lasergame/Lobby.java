package com.briang.lasergame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.briang.lasergame.Connections.AsyncResponse;
import com.briang.lasergame.Connections.OkHttpGet;
import com.briang.lasergame.Models.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Lobby extends AppCompatActivity implements AsyncResponse {


    OkHttpGet okHttpGet = new OkHttpGet();



    @BindView(R.id.playerlist)
    ListView playerlist;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.include)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        ButterKnife.bind(this);



        Intent intent = getIntent();
        String roomName = intent.getExtras().getString("roomName");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        title.setText(roomName);
        title.setGravity(Gravity.CENTER_HORIZONTAL);


        okHttpGet.delegate = this;
        okHttpGet.execute("http://laser-web.herokuapp.com/game/"+roomName+"/players");


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // TODO: 09-06-16 Add backpressed logic like
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

        try {
            JSONArray arr = new JSONArray(output);
            String[] players = new String[arr.length()];



            for(int i = 0; i < arr.length(); i++)
            {

                JSONObject obj = arr.getJSONObject(i);
                players[i] =  obj.getString("playerid");

            }

            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.playerlist,R.id.playerName,players);

            playerlist.setAdapter(itemsAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
