package com.grantamos.android.deviantart.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by granta on 12/4/13.
 */
public class Image implements Serializable {

    public User user;

    public ImageData thumb;

    public ImageData image;

    public ImageData superImage;

    public String title;

    public String category;

    public List<Comment> comments;

    public float widthScale() {
        if(image != null){
            return ((float) thumb.width) / thumb.height;
        }

        return 1;
    }

    public float heightScale() {
        if(image != null){
            return ((float) thumb.height) / thumb.width;
        }

        return 1;
    }

    public class ImageData implements Serializable {

        public String url;

        public int width;

        public int height;
    }
}
