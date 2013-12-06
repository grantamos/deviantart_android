package com.grantamos.android.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by granta on 12/6/13.
 */
public class VolleyHelper {

    private static VolleyHelper mInstance;

    private static RequestQueue mRequestQueue;

    private static ImageLoader mImageLoader;

    public static VolleyHelper getInstance(Context context) {

        if(mInstance == null)
            mInstance = new VolleyHelper(context);

        return mInstance;
    }

    private VolleyHelper(Context context) {

        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache((int) Runtime.getRuntime().maxMemory() / 8));
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return this.mImageLoader;
    }
}
