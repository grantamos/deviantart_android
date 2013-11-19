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
import com.grantamos.android.deviantart.AsyncJSONRequest;
import com.grantamos.android.deviantart.ImageData;
import com.grantamos.android.deviantart.JSONCallback;
import com.grantamos.android.deviantart.R;
import com.grantamos.android.deviantart.activity.BrowseActivity;
import com.grantamos.android.deviantart.activity.BrowseActivityInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Grant on 11/4/13.
 */
public class ImageListFragment extends ListFragment implements JSONCallback {
    String mBaseUrl, mCategory, mTime, mJson;
    int mOffset, mLength;
    AsyncJSONRequest asyncJSONRequest;
    ListView mListView;
    ArrayList<ImageData> mImages;
    ImageListAdapter mImageListAdapter;
    BrowseActivityInterface onClickHandler;

    public String getCategory() { return mCategory; }
    public void setCategory(String category) { mCategory = category; }

    public void setTime(String time) { this.mTime = time; }

    public ImageListFragment(){}

    public static ImageListFragment newInstance(String baseUrl, String category, String time, int offset, int length){
        ImageListFragment imageListFragment = new ImageListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("baseUrl", baseUrl);
        bundle.putString("category", category);
        bundle.putString("time", time);
        bundle.putInt("offset", offset);
        bundle.putInt("length", length);
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

        outState.putString("baseUrl", mBaseUrl);
        outState.putString("category", mCategory);
        outState.putString("time", mTime);
        outState.putInt("offset", mOffset);
        outState.putInt("length", mLength);
        outState.putString("json", mJson);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mListView = (ListView) inflater.inflate(R.layout.list_view_image, container, false);

        if(mJson != null && !mJson.isEmpty())
            consumeJSON(mJson);
        else
            fetchData();

        return mListView;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        onClickHandler = (BrowseActivity) activity;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        if(onClickHandler != null)
            onClickHandler.onImageClick(((ImageListAdapter.ViewHolder)view.getTag()).imageView, (ImageData)listView.getItemAtPosition(position));
    }

    public void setArguments(Bundle arguments){
        mBaseUrl = arguments.getString("baseUrl");
        mCategory = arguments.getString("category");
        mTime = arguments.getString("time");
        mOffset = arguments.getInt("offset");
        mLength = arguments.getInt("length");
        mJson = arguments.getString("json");
    }

    public void consumeJSON(String json){
        mJson = json;

        try {
            JSONArray jsonArray = new JSONArray(mJson);

            if(mImageListAdapter == null){
                mImages = new ArrayList<ImageData>();
                mImageListAdapter = new ImageListAdapter(getActivity(), 0, mImages);
                mListView.setAdapter(mImageListAdapter);
            }

            for(int i = 0; i < jsonArray.length(); i++)
                mImages.add(new ImageData(jsonArray.getJSONObject(i)));

            mImageListAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchData(){
        asyncJSONRequest = new AsyncJSONRequest(this);
        asyncJSONRequest.execute(getUrl());
    }

    public void refreshData() {
        mImages.clear();
        if(mListView.getAdapter() != null)
            ((ImageListAdapter) mListView.getAdapter()).notifyDataSetChanged();
        fetchData();
    }

    public String getUrl() {
        String url = mBaseUrl + "?";

        if(!mCategory.isEmpty())
            url += "category="+mCategory+"&";
        if(!mTime.isEmpty())
            url += "time="+mTime+"&";
        if(mOffset != 0)
            url += "offset="+mOffset+"&";
        if(mLength != 0)
            url += "length="+mLength;

        return url;
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

            if(row == null){
                LayoutInflater inflater = activity.getLayoutInflater();
                row = inflater.inflate(R.layout.list_item_browse, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (AsyncImageView) row.findViewById(R.id.image);
                viewHolder.username = (TextView) row.findViewById(R.id.username);
                viewHolder.title = (TextView) row.findViewById(R.id.title);
                row.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)row.getTag();
                viewHolder.imageView.setImageDrawable(null);
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
