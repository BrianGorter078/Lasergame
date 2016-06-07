package com.briang.lasergame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.briang.lasergame.Connections.AsyncResponse;
import com.briang.lasergame.Connections.OkHttpGet;
import com.briang.lasergame.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Joinlobby extends AppCompatActivity implements AsyncResponse {

    OkHttpGet okHttpGet = new OkHttpGet();

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
        getRequest("http://laser-web.herokuapp.com/game");
    }

    @Override
    public void processFinish(String output) {

        String[] values = new String[] {output};

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.custumlist,R.id.textView,values);

        joinLobbylist.setAdapter(itemsAdapter);
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
