package com.briang.lasergame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.briang.lasergame.Connections.AsyncResponse;
import com.briang.lasergame.Connections.OkHttpGet;

public class Game extends AppCompatActivity implements AsyncResponse{

    TextView textView;
    TextView countDown;
    Toolbar toolbar;
    TextView title;
    Toast leave;
    String room;

    private Boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        toolbar = (Toolbar) findViewById(R.id.gameToolbar);
        title = (TextView) findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);

        room = getIntent().getStringExtra("roomName");
        title.setText(room);

        countDown = (TextView) findViewById(R.id.countDown);
        textView = (TextView) findViewById(R.id.textView);


        getHealth();

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setText(millisUntilFinished / 1000 +"");
            }

            public void onFinish() {
                textView.setVisibility(View.INVISIBLE);
                textView.setClickable(false);

                showHP(10);
            }

        }.start();


    }

    @Override
    public void onBackPressed() {
        if(firstTime)
        {
            leave = Toast.makeText(getApplicationContext(),"Are you sure you want to leave this Game?", Toast.LENGTH_LONG);
            leave.show();

            firstTime = false;
        }

        else {
            firstTime = true;
            leave.cancel();
            super.onBackPressed();
        }
    }


    public void getHealth()
    {
        OkHttpGet ok = new OkHttpGet();
        ok.delegate = this;
        ok.execute(ok.getHealth(room));

    }

    private void showHP(int hp)
    {
        countDown.setText("HP " + hp);
    }

    @Override
    public void processFinish(String output) {

    }
}

