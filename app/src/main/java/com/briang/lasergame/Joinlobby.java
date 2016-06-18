package com.briang.lasergame;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.briang.lasergame.Connections.AsyncResponse;
import com.briang.lasergame.Connections.OkHttpGet;
import com.briang.lasergame.Connections.OkHttpPost;
import com.briang.lasergame.Models.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Joinlobby extends AppCompatActivity implements AsyncResponse {


    ArrayList<JSONObject> roomList = new ArrayList<>();
    OkHttpPost okHttpPost = new OkHttpPost();
    String room;
    BluetoothDevice device;
    private String password = "123";
    private String deviceId = "";


    @BindView(R.id.joinlobbyView)
    ListView joinLobbylist;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.include2)
    Toolbar toolbar;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinlobby);

        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        device = getIntent().getParcelableExtra("device");
        title.setText("Active Games");
        okHttpPost.delegate = this;


        getRequest();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getRequest();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        postRequest("Remove");
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        getRequest();

        super.onResume();
    }



    @Override
    public void processFinish(String output) {

        try {
            JSONArray arr = new JSONArray(output);

            final ArrayList<Room> arrayOfUsers = new ArrayList<>();


            for(int i = 0; i < arr.length(); i++)
            {
                JSONObject obj = arr.getJSONObject(i);


                roomList.add(obj);

                Room r = new Room(roomList.get(i).getString("roomname"), roomList.get(i).getString("players"),roomList.get(i).getBoolean("state"));

                if(r.state)
                arrayOfUsers.add(r);

            }

            RoomsAdapter itemsAdapter = new RoomsAdapter(this, arrayOfUsers);
            joinLobbylist.setAdapter(itemsAdapter);

            joinLobbylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    room = arrayOfUsers.get(i).roomName;

                    Intent intent = new Intent(getApplicationContext(), Lobby.class);
                    intent.putExtra("roomName",room);
                    intent.putExtra("password",password);
                    intent.putExtra("device",device);

                    postRequest("Add");

                    startActivity(intent);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }


    /**
     * Executes a http getRequest from the requested URl
     */
    public void getRequest()
    {
        OkHttpGet ok = new OkHttpGet();
        ok.delegate = this;
        ok.execute(ok.getGames());

    }

    public void postRequest(String doing)
    {

        OkHttpPost post = new OkHttpPost();
        post.delegate = this;

        if(doing.equals("Add")){
            post.execute(post.addPlayer(room,password,deviceId));
        }
        else if(doing.equals("Remove")) {
            post.execute(post.removePlayer(room,password,deviceId));
        }

    }

}
