package com.briang.lasergame.Connections;

import android.os.AsyncTask;

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
            RequestBody body = RequestBody.create(JSON, "");
            Request request = new Request.Builder()
                    .url(url[0])
                    .post(body)
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

