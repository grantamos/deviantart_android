package com.grantamos.android.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by Grant on 9/11/13.
 */
public class DownloadableImageDrawable extends BitmapDrawable {
    private final WeakReference<AsyncImageTask> asyncImageTaskWeakReference;

    public DownloadableImageDrawable(Resources res, Bitmap bitmap, AsyncImageTask asyncImageTaskWeakReference) {
        super(res, bitmap);
        this.asyncImageTaskWeakReference = new WeakReference<AsyncImageTask>(asyncImageTaskWeakReference);
    }

    public AsyncImageTask getBitmapDownloaderTask() {
        return asyncImageTaskWeakReference.get();
    }
}
