package com.briang.lasergame;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpGet extends AsyncTask<String, String, String> {

    public AsyncResponse delegate = null;

    OkHttpClient client = new OkHttpClient();
    String result;


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

