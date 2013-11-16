package com.grantamos.android.deviantart.fragment;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.grantamos.android.deviantart.AsyncImageView;
import com.grantamos.android.deviantart.ImageData;
import com.grantamos.android.deviantart.JSONCallback;
import com.grantamos.android.deviantart.R;

import java.util.ArrayList;

/**
 * Created by Grant on 11/4/13.
 */
public class ImageListFragment extends ListFragment implements JSONCallback {
    String mUrl, mJson;

    public ImageListFragment(){}

    public static ImageListFragment newInstance(String url){
        ImageListFragment imageListFragment = new ImageListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        imageListFragment.setArguments(bundle);

        return imageListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            setArguments(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putString("url", this.mUrl);
        outState.putString("json", mJson);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ListView listView = (ListView) inflater.inflate(R.layout.list_view_image, container, false);

        return listView;
    }

    public void setArguments(Bundle arguments){
        this.mUrl = arguments.getString("url");
        this.mJson = arguments.getString("json");
    }

    public void consumeJSON(String json){
        this.mJson = json;

    }

    private class ImageListAdapter  extends ArrayAdapter<ImageData> {
        Activity activity;

        public ImageListAdapter(Context context, int textViewResourceId, ArrayList<ImageData> items) {
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
                row=inflater.inflate(R.layout.list_item_browse, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (AsyncImageView) row.findViewById(R.id.image);
                viewHolder.username = (TextView) row.findViewById(R.id.username);
                viewHolder.title = (TextView) row.findViewById(R.id.title);
                row.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)row.getTag();
            }

            viewHolder.imageView.setURL(imageData.thumb.url);
            viewHolder.imageView.downloadImage();
            viewHolder.username.setText(imageData.username);
            viewHolder.title.setText(imageData.title);

            return row;
        }

        class ViewHolder{
            AsyncImageView imageView;
            TextView title, username;
        }
    }
}
