package com.briang.lasergame;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.briang.lasergame.Connections.AsyncResponse;
import com.briang.lasergame.Connections.OkHttpPost;

import butterknife.ButterKnife;


public class Home extends Fragment implements AsyncResponse
{



    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice device;
    BluetoothSocket mSocket;
    String room;
    String pass;
    Button button;
    Button button1;
    Button connectBluetooth;

    String deviceId;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(getActivity());

        connectBluetooth = (Button) view.findViewById(R.id.connectBluetooth);


        deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        button = (Button) view.findViewById(R.id.button2);
        button.setVisibility(View.INVISIBLE);
        button.setActivated(false);



        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final View checkBoxView = View.inflate(getActivity(), R.layout.new_game, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Create Room")
                        .setView(checkBoxView)

                        .setCancelable(false)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                EditText roomName = (EditText) checkBoxView.findViewById(R.id.roomName);
                                EditText password = (EditText) checkBoxView.findViewById(R.id.password);

                                if(!roomName.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                                    room = roomName.getText().toString();
                                    pass = password.getText().toString();

                                    createGame();

                                    Intent intent = new Intent(getContext(), Lobby.class);
                                    intent.putExtra("roomName", room);
                                    intent.putExtra("device", device);
                                    startActivity(intent);


                                }
                                else{
                                    Log.d("Error", "Empty");

                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });


        button1 = (Button) view.findViewById(R.id.button3);
        button1.setVisibility(View.INVISIBLE);
        button1.setActivated(false);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Joinlobby.class);
                intent.putExtra("device", device);
                startActivity(intent);
            }
        });


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.d("Fuck you", "Fuck your bluetoothless phone you phony ass bitch. Buy a normal phone please");
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }


        Button test = (Button) view.findViewById(R.id.connectBluetooth);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DeviceList.class);
                startActivityForResult(intent, 2);
            }
        });

        return view;
    }


    @Override
    public void processFinish(String output) {
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                device = data.getParcelableExtra("device");
                if(device != null) {
                    button.setVisibility(View.VISIBLE);
                    button.setActivated(true);

                    button1.setVisibility(View.VISIBLE);
                    button1.setActivated(true);

                    connectBluetooth.setVisibility(View.INVISIBLE);
                    connectBluetooth.setActivated(false);
                }
            }
        }
    }

    private void createGame() {
        OkHttpPost okHttpPost = new OkHttpPost();
        okHttpPost.delegate = this;
        okHttpPost.execute(okHttpPost.createRoom(room,pass), okHttpPost.addPlayer(room,pass,deviceId));
    }

}