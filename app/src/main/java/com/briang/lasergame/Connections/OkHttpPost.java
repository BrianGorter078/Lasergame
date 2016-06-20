package com.briang.lasergame.Connections;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpPost extends AsyncTask<String, String, String> {

    public AsyncResponse delegate = null;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String result;


    @Override
    protected String doInBackground(String...url)  {
        {
            for (int i = 0; i < url.length; i++) {

                RequestBody body = RequestBody.create(JSON, "");
                Request request = new Request.Builder()
                        .url(url[i])
                        .post(body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();

                    Log.d("Result", result);

                } catch (Exception e) {
                }
            }


            return result;
        }
    }

    protected void onPostExecute(String result){
//        delegate.processFinish(result);

    }

    /**
     * Creates a PostString to add a player
     *
     * @param room roomname
     * @param password password of the room
     * @param deviceId Id of the device
     * @return PostString to add a player
     */
    public String addPlayer(String room, String password, String deviceId) {
        final String addPlayer = "http://laser-web.herokuapp.com/game/" + room + "/" + password + "/"+ deviceId;
        return addPlayer;
    }

    /**
     *
     * @param room
     * @param password
     * @param deviceId
     * @return
     */
    public String removePlayer(String room, String password, String deviceId){
        final String removePlayer = "http://laser-web.herokuapp.com/removeplayer/" + room + "/" + password + "/"+ deviceId;
        return removePlayer;
    }

    /**
     * Creates a PostString to create a room
     *
     * @param roomName roomname
     * @param password password of the rooom
     * @return PostString to create a room
     */
    public String createRoom(String roomName, String password)
    {
        final String createroom = "http://laser-web.herokuapp.com/newgame/" + roomName + "/" + password;

        return createroom;
    }

    public String startGame(String roomName){
        final String startGame = "http://laser-web.herokuapp.com/startgame/"+ roomName + "/123/true";

        return startGame;
    }

    public String removeHp(String room, String id)
    {
        final String removeHp = "http://laser-web.herokuapp.com/healthpoints/"+ room +"/" + id;
        return removeHp;
    }
}

