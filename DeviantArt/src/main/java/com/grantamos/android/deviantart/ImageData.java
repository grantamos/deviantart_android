package com.grantamos.android.deviantart;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Grant on 9/10/13.
 */
public class ImageData implements Serializable {
    public String userIcon, username, userSymbol, category, title, posted, userid;
    public Image thumb, image, superImage;

    public ImageData(JSONObject jsonObject){
        this.userIcon = jsonObject.optString("userIcon", "@drawable/usericon_not_found");
        this.username = jsonObject.optString("username");
        this.userSymbol = jsonObject.optString("userSymbol");
        this.userid = jsonObject.optString("userid");
        this.category = jsonObject.optString("category");
        this.title = jsonObject.optString("title");
        this.posted = jsonObject.optString("posted");

        this.thumb = new Image(jsonObject.optJSONObject("thumb"));
        this.image = new Image(jsonObject.optJSONObject("image"));
        this.superImage = new Image(jsonObject.optJSONObject("superImage"));
    }

    public class Image implements Serializable {
        int width = 0;
        int height = 0;
        public String url = null;

        public Image(JSONObject jsonObject){
            if(jsonObject == null)
                return;

            this.width = jsonObject.optInt("width");
            this.height = jsonObject.optInt("height");
            this.url = jsonObject.optString("url");
        }
    }
}
