package com.briang.lasergame;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.briang.lasergame.Connections.AsyncResponse;
import com.briang.lasergame.Connections.OkHttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Game extends AppCompatActivity implements AsyncResponse{

    TextView textView;
    TextView countDown;
    Toolbar toolbar;
    TextView title;
    Toast leave;
    String room;
    int hp = 10;
    private Timer waitingTimer;
    String[] players;
    String deviceId;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mDevice;
    BluetoothSocket mSocket;
    ConnectThread mConnectThread;
    ConnectedThread mConnectedThread;

    public  BluetoothSocket mmSocket;
    public  BluetoothDevice mmDevice;
    public UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private Boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

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
                countDown.setText("HP " + hp);
                runInBackground();

            }

        }.start();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        BluetoothDevice device = getIntent().getParcelableExtra("device");
        Log.d("device", device + "");
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();


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

    private void runInBackground() {
        Log.d("run" , "run");
        waitingTimer = new Timer();
        waitingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        getPlayersHp();
                    }
                });
            }
        }, 0, 1000);
    }

    public void getHealth()
    {
        OkHttpGet ok = new OkHttpGet();
        ok.delegate = this;
        ok.execute(ok.getHealth(room));

    }

    private void showHP()
    {
        hp = hp - 1;
        countDown.setText("Hp: " + hp);
    }


    @Override
    public void processFinish(String output) {
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


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int)msg.arg1;
            int end = (int)msg.arg2;

            switch(msg.what) {
                case 1:
                    String writeMessage = new String(writeBuf);
                    writeMessage = writeMessage.substring(begin, end);
                    Log.d("Numbers", writeMessage);
                    showHP();
                    break;
            }
        }
    };

    private class ConnectThread extends Thread {

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }
        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
               
            }

            mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            while (true) {
                try {
                    bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
                    for(int i = begin; i < bytes; i++) {
                        if(buffer[i] == "#".getBytes()[0]) {
                            mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
                            begin = i + 1;
                            if(i == bytes - 1) {
                                bytes = 0;
                                begin = 0;
                            }
                        }
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


    public void getPlayersHp() {
        OkHttpGet okHttpGet = new OkHttpGet();
        okHttpGet.delegate = this;
        okHttpGet.execute(okHttpGet.getHealth(room));
    }
}

