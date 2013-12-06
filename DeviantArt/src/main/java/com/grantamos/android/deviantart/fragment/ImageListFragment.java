package com.grantamos.android.deviantart.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.grantamos.android.deviantart.R;
import com.grantamos.android.deviantart.activity.BrowseActivity;
import com.grantamos.android.deviantart.helpers.BrowseActivityInterface;
import com.grantamos.android.deviantart.model.Image;
import com.grantamos.android.deviantart.model.Stream;
import com.grantamos.android.util.GsonRequest;
import com.grantamos.android.util.LruBitmapCache;
import com.grantamos.android.util.ScalableImageView;
import com.grantamos.android.util.VolleyHelper;

import java.util.ArrayList;

import static com.android.volley.toolbox.ImageLoader.ImageListener;

/**
 * Created by Grant on 11/4/13.
 */
public class ImageListFragment extends ListFragment {

    private Stream mStream;

    private ListView mListView;

    private ImageListAdapter mImageListAdapter;

    private BrowseActivityInterface onClickHandler;

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    private String mUrl;

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

        VolleyHelper volleyHelper = VolleyHelper.getInstance(getActivity().getApplicationContext());

        mRequestQueue = volleyHelper.getRequestQueue();
        mImageLoader = volleyHelper.getImageLoader();
        mStream = new Stream();

        getStream(mUrl);
    }

    @Override
    public void onStop() {

        super.onStop();
        mRequestQueue.cancelAll(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState){

        super.onSaveInstanceState(outState);

        outState.putString("url", mUrl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mListView = (ListView) inflater.inflate(R.layout.list_view_image, container, false);

        mImageListAdapter = new ImageListAdapter(getActivity(), 0, mStream.data);
        mListView.setAdapter(mImageListAdapter);

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
            onClickHandler.onImageClick(((ImageListAdapter.ViewHolder)view.getTag()).imageView, (Image)listView.getItemAtPosition(position));
    }

    public void setArguments(Bundle arguments){
        mUrl = arguments.getString("url");
    }

    public void refreshData() {

        mStream.clear();

        getStream(mUrl);
    }

    public void getStream(String url) {


        Response.Listener responseListener = new Response.Listener<Stream>() {
            @Override
            public void onResponse(Stream stream) {

                mStream.append(stream);

                if(mListView != null)
                    ((ArrayAdapter)mListView.getAdapter()).notifyDataSetChanged();
            }
        };

        GsonRequest<Stream> gsonRequest = new GsonRequest<Stream>(url, Stream.class, null, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
        mRequestQueue.add(gsonRequest);
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    private class ImageListAdapter  extends ArrayAdapter<Image> {

        Activity activity;

        int FADE_IN_TIME = 500;

        public ImageListAdapter(Context context, int textViewResourceId, ArrayList<Image> items) {
            super(context, textViewResourceId, items);
            this.activity = (Activity)context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View row = convertView;
            ViewHolder viewHolder;
            Image image = this.getItem(position);

            if(row == null){
                LayoutInflater inflater = activity.getLayoutInflater();
                row = inflater.inflate(R.layout.list_item_browse, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ScalableImageView) row.findViewById(R.id.image);
                viewHolder.username = (TextView) row.findViewById(R.id.username);
                viewHolder.title = (TextView) row.findViewById(R.id.title);
                row.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)row.getTag();
                viewHolder.imageView.setImageDrawable(null);
            }

            viewHolder.imageView.imageHeight = image.thumb.height;
            viewHolder.imageView.imageWidth = image.thumb.width;
            viewHolder.url = image.thumb.url;
            viewHolder.username.setText(image.user.username);
            viewHolder.title.setText(image.title);

            if(viewHolder.imageContainer != null)
                viewHolder.imageContainer.cancelRequest();

            viewHolder.imageContainer = mImageLoader.get(viewHolder.url, getImageListener(viewHolder.imageView, 0, 0));

            return row;
        }

        public ImageListener getImageListener(final ScalableImageView view, final int defaultImageResId, final int errorImageResId){
            return new ImageListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (errorImageResId != 0) {
                        view.setImageResource(errorImageResId);
                    }
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        view.setImageBitmap(response.getBitmap(), isImmediate ? 0 : 500);
                    } else if (defaultImageResId != 0) {
                        view.setImageResource(defaultImageResId);
                    }
                }
            };
        }

        class ViewHolder {
            ScalableImageView imageView;
            String url;
            TextView title, username;
            ImageLoader.ImageContainer imageContainer;
        }
    }
}
