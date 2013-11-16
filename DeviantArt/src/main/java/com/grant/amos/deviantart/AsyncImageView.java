package com.grant.amos.deviantart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.grant.amos.util.AsyncImageTask;
import com.grant.amos.util.DownloadableImageDrawable;
import com.grant.amos.util.ImageCache;
import com.grant.amos.util.ScalableImageView;

/**
 * Created by Grant on 9/11/13.
 */
public class AsyncImageView extends ScalableImageView {
    String imageURL = null;

    public AsyncImageView(Context context) {
        super(context);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setURL(String url){
        this.imageURL = url;
    }

    public void downloadImage(){
        Bitmap image = ImageCache.getImage(imageURL);
        if(image != null){
            this.setImageBitmap(image);
            return;
        }

        if(downloadAllowed()){
            AsyncImageTask asyncImageTask = new AsyncImageTask(this);
            DownloadableImageDrawable imageDrawable = new DownloadableImageDrawable(asyncImageTask, this.getResources());
            this.setImageDrawable(imageDrawable);
            asyncImageTask.execute(imageURL);
        }
    }

    public boolean downloadAllowed(){
        AsyncImageTask asyncImageTask = getAsyncImageTask(this);

        if(asyncImageTask != null){
            String urlDownloading = asyncImageTask.getURL();
            if(urlDownloading == null || !urlDownloading.equals(this.imageURL))
                asyncImageTask.cancel(true);
            else
                return false;
        }

        return true;
    }

    public static AsyncImageTask getAsyncImageTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadableImageDrawable) {
                DownloadableImageDrawable downloadedDrawable = (DownloadableImageDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }
}