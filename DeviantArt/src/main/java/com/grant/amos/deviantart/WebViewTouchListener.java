package com.grant.amos.deviantart;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;

/**
 * Created by Grant on 10/30/13.
 */
public class WebViewTouchListener implements View.OnTouchListener {
    private int position;
    private ViewGroup vg;
    private View v;

    public WebViewTouchListener(View v, ViewGroup vg, int position) {
        this.v = v;
        this.vg = vg;
        this.position = position;
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_CANCEL:
                return true;
            case MotionEvent.ACTION_UP:
                sendClick();
                return true;
        }

        return false;
    }

    public void sendClick() {
        ListView lv = (ListView) vg;
        lv.performItemClick(v, position, 0);
    }

    public void setPosition(int position){
        this.position = position;
    }
}
