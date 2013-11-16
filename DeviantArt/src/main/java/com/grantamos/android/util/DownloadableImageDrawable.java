package com.grantamos.android.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by Grant on 9/11/13.
 */
public class DownloadableImageDrawable extends ColorDrawable {
    private final WeakReference<AsyncImageTask> asyncImageTaskWeakReference;

    public DownloadableImageDrawable(AsyncImageTask asyncImageTask, Resources res) {
        super(Color.BLACK);
        asyncImageTaskWeakReference = new WeakReference<AsyncImageTask>(asyncImageTask);
    }

    public AsyncImageTask getBitmapDownloaderTask() {
        return asyncImageTaskWeakReference.get();
    }
}
