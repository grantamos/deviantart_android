package com.grant.amos.deviantart;

import java.io.Serializable;

/**
 * Created by Grant on 9/10/13.
 */
public class ImageData implements Serializable {
    public String userIcon, username, userSymbol, category, title, posted, userid;
    public String thumbURL, imageURL, superImageURL;

    public ImageData(){};

    public ImageData(String userIcon, String username, String userSymbol, String category, String title, String posted, String userid, String thumbURL, String imageURL, String superImageURL) {
        this.userIcon = userIcon;
        this.username = username;
        this.userSymbol = userSymbol;
        this.category = category;
        this.title = title;
        this.posted = posted;
        this.userid = userid;
        this.thumbURL = thumbURL;
        this.imageURL = imageURL;
        this.superImageURL = superImageURL;
    }
}
