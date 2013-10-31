package com.grant.amos.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Grant on 10/30/13.
 */
public class WebViewImageHTML {
    public static String html = "";

    public static void Init(Context context){
        AssetManager assetManager = context.getResources().getAssets();
        try {
            InputStream inputStream = assetManager.open("html/imageViewer.html");
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            html = new String(b);
            inputStream.close();
        } catch (IOException e) {
            Log.e("Error", "Couldn't open upgrade-alert.html", e);
        }
    }

    public static String GetHTMLTemplate(){
        return html;
    }

    public static String GetHTMLWithImageURL(String url){
        return html.replace("%s", url);
    }
}
