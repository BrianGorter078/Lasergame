package com.briang.lasergame;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;

public class game extends AppCompatActivity {

    @BindView(R.id.countDown)
    TextView countDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                countDown.setVisibility(View.INVISIBLE);
                showHP(10);
            }

        }.start();




    }

    public void showHP(int hp)
    {
        countDown.setText("HP Left: " + hp);
    }
}

