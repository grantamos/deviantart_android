package com.grant.amos.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.grant.amos.deviantart.AsyncImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by Grant on 9/11/13.
 */
public class AsyncImageTask extends AsyncTask<String, Void, Bitmap> {
    String url;
    WeakReference<ImageView> imageViewReference;

    public String getURL() {return url;}

    public AsyncImageTask(ImageView imageView){
        this.imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        url = strings[0];
        Bitmap image = ImageCache.getImage(url);
        if(image == null)
            image = downloadBitmap(url);

        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        ImageCache.addImage(url, bitmap);

        if(isCancelled()){
            bitmap = null;
        }

        if(imageViewReference != null && bitmap != null){
            ImageView imageView = imageViewReference.get();
            if(AsyncImageView.getAsyncImageTask(imageView) == this){
                imageView.setImageBitmap(bitmap);
                imageView.setAdjustViewBounds(true);
            }
        }
    }

    public static Bitmap downloadBitmap(String url){
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url, e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }
}
