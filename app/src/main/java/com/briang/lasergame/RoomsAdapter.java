package com.briang.lasergame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.briang.lasergame.Models.Room;

import java.util.ArrayList;

public class RoomsAdapter extends ArrayAdapter<Room> {
    public RoomsAdapter(Context context, ArrayList<Room> rooms) {
        super(context, 0, rooms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Room room = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lobbylist, parent, false);
        }


        // Lookup view for data population
        TextView playersInRoom = (TextView) convertView.findViewById(R.id.playersInRoom);
        TextView roomName = (TextView) convertView.findViewById(R.id.roomName);
        // Populate the data into the template view using the data object
        playersInRoom.setText(room.playerCount);
        roomName.setText(room.roomName);
        // Return the completed view to render on screen
        return convertView;
    }
}
