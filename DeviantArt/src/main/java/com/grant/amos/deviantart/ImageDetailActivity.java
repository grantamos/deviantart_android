package com.grant.amos.deviantart;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageDetailActivity extends Activity {

    ImageData imageData = null;

    AsyncImageView imageView;
    TextView usernameTextView;
    TextView titleTextView;
    AsyncImageView userIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageData = (ImageData) extras.get("imageData");
        }

        if(imageData == null)
            finish();

        setContentView(R.layout.image_detail_activity);
        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_detail, menu);
        return true;
    }

    public void setupViews(){
        imageView = (AsyncImageView) findViewById(R.id.detail_image_view);
        userIconImageView = (AsyncImageView) findViewById(R.id.user_icon_image_view);
        usernameTextView = (TextView) findViewById(R.id.username);
        titleTextView = (TextView) findViewById(R.id.title);

        imageView.setURL(imageData.thumbURL);
        imageView.downloadImage();

        if(imageData.imageURL != null){
            imageView.setURL(imageData.imageURL);
            imageView.downloadImage();
        }

        if(imageData.title != null)
            titleTextView.setText(imageData.title);

        if(imageData.username != null)
            usernameTextView.setText(imageData.username);

        if(imageData.userIcon != null){
            userIconImageView.setURL(imageData.userIcon);
            userIconImageView.downloadImage();
        }
    }
    
}