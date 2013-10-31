package com.grant.amos.deviantart;

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
public class AsyncJSONRequest extends AsyncTask<String, Void, ArrayList<ImageData>> {
    protected String requestUrl;
    ListView listView;
    Activity activity;

    public AsyncJSONRequest(Activity activity, ListView listView){
        this.listView = listView;
        this.activity = activity;
    }

    @Override
    protected ArrayList<ImageData> doInBackground(String... strings) {
        this.requestUrl = strings[0];
        ArrayList<ImageData> images = new ArrayList<ImageData>();

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

            String jsonString = new String(outputStream.toByteArray());
            JSONArray jsonArray = new JSONArray(jsonString);

            Log.i("DADebug", jsonString);

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject jsonTemp;
                ImageData image = new ImageData();

                image.category = jsonObject.getString("category");
                image.username = jsonObject.getString("username");
                image.title = jsonObject.getString("title");

                jsonTemp = jsonObject.getJSONObject("thumb");
                if(jsonTemp.has("url"))
                    image.thumbURL = jsonTemp.getString("url");

                jsonTemp = jsonObject.getJSONObject("image");
                if(jsonTemp.has("url"))
                    image.imageURL = jsonTemp.getString("url");

                if(jsonObject.has("userIcon"))
                    image.userIcon = jsonObject.getString("userIcon");

                Log.i("DADebug", jsonObject.toString());
                Log.i("DADebug", image.thumbURL);

                images.add(image);
            }
        }catch (Exception e){
            Log.e("DeviantArt", e.toString());
        }

        return images;
    }

    @Override
    public void onPostExecute(ArrayList<ImageData> jsonArray){
        listView.setAdapter(new BrowseListAdapter(this.activity, 0, jsonArray));
    }
}
