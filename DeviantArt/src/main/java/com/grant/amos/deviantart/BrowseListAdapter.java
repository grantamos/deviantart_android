package com.grant.amos.deviantart;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.grant.amos.util.WebViewImageHTML;

import java.util.ArrayList;

/**
 * Created by Grant on 9/10/13.
 */
public class BrowseListAdapter extends ArrayAdapter<ImageData> {
    Activity activity;

    public BrowseListAdapter(Context context, int textViewResourceId, ArrayList<ImageData> items) {
        super(context, textViewResourceId, items);
        this.activity = (Activity)context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ViewHolder viewHolder;
        ImageData imageData = this.getItem(position);

        if(row==null){
            LayoutInflater inflater = activity.getLayoutInflater();
            row=inflater.inflate(R.layout.browse_list_item, parent, false);

            BrowseListAdapter ref = this;

            viewHolder = new ViewHolder();
            viewHolder.imageWebView = (WebView) row.findViewById(R.id.image);
            viewHolder.imageWebView.setClickable(false);
            viewHolder.touchListener = new WebViewTouchListener(row, parent, position);
            viewHolder.imageWebView.setOnTouchListener(viewHolder.touchListener);

            viewHolder.username = (TextView) row.findViewById(R.id.username);
            viewHolder.title = (TextView) row.findViewById(R.id.title);
            row.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)row.getTag();
        }

        String html = WebViewImageHTML.GetHTMLWithImageURL(imageData.thumbURL);
        viewHolder.imageWebView.loadData(html, "text/html", "utf-8");
        viewHolder.touchListener.setPosition(position);
        viewHolder.username.setText(imageData.username);
        viewHolder.title.setText(imageData.title);

        return row;
    }

    static class ViewHolder{
        WebViewTouchListener touchListener;
        WebView imageWebView;
        TextView title, username;
    }
}
