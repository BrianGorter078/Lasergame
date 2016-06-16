package com.briang.lasergame;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class game extends AppCompatActivity {

    TextView textView;
    TextView countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        countDown = (TextView) findViewById(R.id.countDown);
        textView = (TextView) findViewById(R.id.textView);

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

    public void showHP(int hp)
    {
        countDown.setText("HP " + hp);
    }
    }

