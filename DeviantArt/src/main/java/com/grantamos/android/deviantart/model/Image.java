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

    public class ImageData {

        public String url;

        public int width;

        public int height;
    }
}
