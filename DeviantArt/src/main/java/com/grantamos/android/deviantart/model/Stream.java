package com.grantamos.android.deviantart.model;

import java.util.ArrayList;

/**
 * Created by granta on 12/4/13.
 */
public class Stream {

    public ArrayList<Image> data;

    public String url;

    public String next;

    public Stream() {
        data = new ArrayList<Image>();
    }

    public void append(Stream stream) {

        url = stream.url;
        next = stream.next;
        data.addAll(stream.data);
    }

    public void clear() {
        data.clear();
        url = null;
        next = null;
    }
}
