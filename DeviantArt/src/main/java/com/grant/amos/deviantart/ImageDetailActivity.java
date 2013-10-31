package com.grant.amos.deviantart;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.grant.amos.util.WebViewImageHTML;

public class ImageDetailActivity extends Activity {

    ImageData imageData = null;

    WebView imageView;
    TextView usernameTextView;
    TextView titleTextView;
    WebView userIconImageView;

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
        imageView = (WebView) findViewById(R.id.detail_image_view);
        userIconImageView = (WebView) findViewById(R.id.user_icon_image_view);
        usernameTextView = (TextView) findViewById(R.id.username);
        titleTextView = (TextView) findViewById(R.id.title);

        String urlToLoad = imageData.imageURL != null ? imageData.imageURL : imageData.thumbURL;
        imageView.loadData(WebViewImageHTML.GetHTMLWithImageURL(urlToLoad), "text/html", "utf-8");
        imageView.getSettings().setSupportZoom(true);
        imageView.getSettings().setBuiltInZoomControls(true);

        if(imageData.title != null)
            titleTextView.setText(imageData.title);

        if(imageData.username != null)
            usernameTextView.setText(imageData.username);

        if(imageData.userIcon != null){
            userIconImageView.loadData(WebViewImageHTML.GetHTMLWithImageURL(imageData.userIcon), "text/html", "utf-8");
        }
    }
    
}
