package com.grant.amos.deviantart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.grant.amos.util.Animations;

public class BrowseActivity extends Activity {
    AsyncJSONRequest browseRequest;
    String baseURL = "http://ec2-54-200-105-129.us-west-2.compute.amazonaws.com:1337/v1/media/browse/";
    String category = "";
    String time = "";
    String offset = "";

    ListView browseListView;
    ScrollView categorySelectScrollView;
    //ImageView fullscreenImageView;
    //View fullscreenImageViewBackground;
    //ImageView thumbView = null;
    //LinearLayout fullscreenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        setupViews();
        getBrowseData();
    }

    @Override
    public void onBackPressed() {
        //if(thumbView != null){
        //    Animations.zoomView(fullscreenImageView, thumbView, true);
        //    Animations.fade(fullscreenImageViewBackground, 1f, 0f, View.VISIBLE, View.INVISIBLE);
        //    thumbView = null;
        //}
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
    public void setupViews(){
        fullscreenImageView = (ImageView)findViewById(R.id.fullscreen_image_view);
        fullscreenView = (LinearLayout)findViewById(R.id.fullscreen_view);
        //fullscreenImageViewBackground = findViewById(R.id.fullscreen_image_view_background);
        browseListView = (ListView)findViewById(R.id.browse_list_view);
        browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                thumbView = ((BrowseListAdapter.ViewHolder) view.getTag()).imageView;
                fullscreenImageView.setImageDrawable(thumbView.getDrawable());
                //fullscreenImageView.forceLayout();
                Animations.zoomView(thumbView, fullscreenView, false);
                //fullscreenImageView.setVisibility(View.VISIBLE);
                //Rect r = new Rect();
                //fullscreenImageView.getGlobalVisibleRect(r);
                //System.out.println(r);
                //Animations.fade(fullscreenImageViewBackground, 0f, 1f, View.VISIBLE, View.VISIBLE);
            }
        });
        categorySelectScrollView = (ScrollView)findViewById(R.id.category_select_scroll_view);
    }
    */

    public void setupViews(){
        browseListView = (ListView)findViewById(R.id.browse_list_view);
        browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WebView thumbView = ((BrowseListAdapter.ViewHolder) view.getTag()).imageWebView;

                Intent imageDetailIntent = new Intent(getBaseContext(), ImageDetailActivity.class);
                ImageData imageData = (ImageData) adapterView.getItemAtPosition(i);
                imageDetailIntent.putExtra("imageData", imageData);
                startActivity(imageDetailIntent);
            }
        });
        categorySelectScrollView = (ScrollView)findViewById(R.id.category_select_scroll_view);
    }

    public void getBrowseData(){
        browseRequest = new AsyncJSONRequest(this, browseListView);
        browseRequest.execute(baseURL + category + time + offset);
    }

    public void categoryButtonOnClick(View view)
    {
        if(browseListView.isShown() && !categorySelectScrollView.isShown())
        {
            browseListView.setVisibility(View.GONE);
            categorySelectScrollView.setVisibility(View.VISIBLE);
        }
        else if(categorySelectScrollView.isShown() && !browseListView.isShown())
        {
            browseListView.setVisibility(View.VISIBLE);
            categorySelectScrollView.setVisibility(View.GONE);
        }
    }
}
