package com.grant.amos.deviantart;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
            viewHolder = new ViewHolder();
            viewHolder.imageView = (AsyncImageView) row.findViewById(R.id.image);
            viewHolder.username = (TextView) row.findViewById(R.id.username);
            viewHolder.title = (TextView) row.findViewById(R.id.title);
            row.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)row.getTag();
        }

        viewHolder.imageView.setURL(imageData.thumbURL);
        viewHolder.imageView.downloadImage();
        viewHolder.username.setText(imageData.username);
        viewHolder.title.setText(imageData.title);

        return row;
    }

    static class ViewHolder{
        AsyncImageView imageView;
        TextView title, username;
    }
}
