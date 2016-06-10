package com.briang.lasergame;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.briang.lasergame.Connections.AsyncResponse;
import com.briang.lasergame.Connections.OkHttpGet;
import com.briang.lasergame.Connections.OkHttpPost;
import com.briang.lasergame.Models.Room;
import com.briang.lasergame.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Joinlobby extends AppCompatActivity implements AsyncResponse {

    OkHttpGet okHttpGet = new OkHttpGet();
    ArrayList<JSONObject> roomList = new ArrayList<>();
    OkHttpPost okHttpPost = new OkHttpPost();
    String room;

    @BindView(R.id.joinlobbyView)
    ListView joinLobbylist;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.include2)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinlobby);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title.setText("Lobbies");
        okHttpGet.delegate = this;
        okHttpPost.delegate = this;

        getRequest("http://laser-web.herokuapp.com/game");
    }

    @Override
    public void processFinish(String output) {

        final String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        try {
            JSONArray arr = new JSONArray(output);

            final ArrayList<Room> arrayOfUsers = new ArrayList<>();


            for(int i = 0; i < arr.length(); i++)
            {
                JSONObject obj = arr.getJSONObject(i);
                roomList.add(obj);
                Room r = new Room(roomList.get(i).getString("roomname"), roomList.get(i).getString("players"));
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


                    String addPlayer = "http://laser-web.herokuapp.com/game/" +room + "/123/"+ deviceId;
                    okHttpPost.execute(addPlayer);


                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }


    /**
     * Executes a http getRequest from the requested URl
     * @param url
     */
    public void getRequest(String url)
    {
        okHttpGet.execute(url);
    }
}
