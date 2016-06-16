package com.briang.lasergame.Connections;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpGet extends AsyncTask<String, String, String> {

    public AsyncResponse delegate = null;

    OkHttpClient client = new OkHttpClient();
    String result;


    /**
     * Creates a getString to get all active games
     * @return String to get all games
     */
    public String getGames() {
        final String getGames = "http://laser-web.herokuapp.com/game";

        return getGames;
    }

    /**
     * Creates a getString to get all players in a game
     * @param room roomName
     * @return String to get all players in a game
     */
    public String getPlayers(String room) {

        final String getPlayers = "http://laser-web.herokuapp.com/game/"+room+"/players";
        return getPlayers;
    }




    @Override
    protected String doInBackground(String...url)  {
        {

            Request request = new Request.Builder()
                    .url(url[0])
                    .build();

           try {
               Response response = client.newCall(request).execute();
               result = response.body().string();

                return result;
            }catch (Exception e)
            {}
    }


        return result;
    }

    protected void onPostExecute(String result){
        delegate.processFinish(result);
    }



}

