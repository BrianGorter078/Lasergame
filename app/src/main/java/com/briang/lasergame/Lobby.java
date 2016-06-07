package com.briang.lasergame;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Lobby extends AppCompatActivity {


    @BindView(R.id.lobbyList)
    ListView lobbylist;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.include)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        String[] values = new String[] {};

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.custumlist,R.id.textView,values);

        lobbylist.setAdapter(itemsAdapter);

        title.setText("Lobby Name");
        title.setGravity(Gravity.CENTER_HORIZONTAL);
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


}
