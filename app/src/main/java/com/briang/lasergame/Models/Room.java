package com.briang.lasergame.Models;

public class Room {
    public String roomName;
    public String playerCount;
    public boolean state;

    public Room(String roomName, String playerCount, Boolean started) {
        this.roomName = roomName;
        this.playerCount = playerCount;
        this.state = started;
    }
}