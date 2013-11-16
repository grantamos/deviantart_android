package com.grantamos.android.deviantart;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Grant on 7/22/13.
 */
public class AsyncJSONRequest extends AsyncTask<String, Void, String> {
    protected String requestUrl;
    JSONCallback callback;

    public AsyncJSONRequest(JSONCallback callback){
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        this.requestUrl = strings[0];

        String jsonString = "";

        try{
            URL url = new URL(requestUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            httpURLConnection.connect();
            int response = httpURLConnection.getResponseCode();
            InputStream inputStream = httpURLConnection.getInputStream();

            byte[] b = new byte[1024];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int len;
            while((len = inputStream.read(b)) != -1)
                outputStream.write(b, 0, len);

            jsonString = new String(outputStream.toByteArray());
        }catch (Exception e){
            Log.e("DeviantArt", e.toString());
        }

        return jsonString;
    }

    @Override
    public void onPostExecute(String json){
        callback.consumeJSON(json);
    }
}