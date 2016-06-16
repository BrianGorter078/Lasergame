package com.briang.lasergame;


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
import android.widget.TextView;

import com.briang.lasergame.Connections.AsyncResponse;
import com.briang.lasergame.Connections.OkHttpGet;
import com.briang.lasergame.Connections.OkHttpPost;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Home extends Fragment implements AsyncResponse
{

    OkHttpPost okHttpPost = new OkHttpPost();

    String room;
    String pass;
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

        okHttpPost.delegate = this;

        final String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Button button = (Button) view.findViewById(R.id.button2);
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



                                    okHttpPost.execute(okHttpPost.createRoom(room,pass), okHttpPost.addPlayer(room,pass,deviceId));


                                    Intent intent = new Intent(getContext(), Lobby.class);
                                    intent.putExtra("roomName", room);
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


        Button button1 = (Button) view.findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Joinlobby.class);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void processFinish(String output) {
    }
}