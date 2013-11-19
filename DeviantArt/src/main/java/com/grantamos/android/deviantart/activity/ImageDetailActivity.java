package com.grantamos.android.deviantart.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.grantamos.android.deviantart.AsyncImageView;
import com.grantamos.android.deviantart.ImageData;
import com.grantamos.android.deviantart.R;

public class ImageDetailActivity extends Activity {

    ImageData imageData = null;

    int thumbLeft, thumbTop, thumbWidth, thumbHeight;
    int animationDuration = 200;
    float thumbScaleX, thumbScaleY;

    AsyncImageView imageView;
    TextView usernameTextView;
    TextView titleTextView;
    AsyncImageView userIconImageView;
    View rootView;
    ImageView backgroundView;
    Bitmap screenshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageData = (ImageData) extras.get("imageData");
            thumbLeft = extras.getInt("left");
            thumbTop = extras.getInt("top");
            thumbWidth = extras.getInt("width");
            thumbHeight = extras.getInt("height");
            screenshot = extras.getParcelable("screenshot");
        }

        if(imageData == null)
            finish();

        setContentView(R.layout.activity_image_detail);
        setupViews();

        if(savedInstanceState == null)
            setupAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.image_detail, menu);
        return true;
    }

    /**
     * Returns true if the current runtime is Honeycomb or later
     */
    private boolean isRuntimePostGingerbread() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
    }

    public void setupViews(){
        imageView = (AsyncImageView) findViewById(R.id.detail_image_view);
        userIconImageView = (AsyncImageView) findViewById(R.id.user_icon_image_view);
        usernameTextView = (TextView) findViewById(R.id.username);
        titleTextView = (TextView) findViewById(R.id.title);
        rootView = findViewById(R.id.image_detail_scrollview);
        backgroundView = (ImageView) findViewById(R.id.backgroundImage);

        //backgroundView.setImageBitmap(screenshot);

        imageView.setURL(imageData.thumb.url);
        imageView.downloadImage();

        if(imageData.image != null){
            imageView.setURL(imageData.image.url);
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

    public void setupAnimation(){
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                    thumbScaleX = thumbWidth * 1f / imageView.getWidth();
                    thumbScaleY = thumbHeight * 1f / imageView.getHeight();

                    animate();

                    return true;
                }
            });
        }
    }

    @SuppressLint("NewApi")
    public void animate() {
        int[] screenLocation = new int[2];
        imageView.getLocationOnScreen(screenLocation);

        if(isRuntimePostGingerbread()){
            imageView.setPivotX(0);
            imageView.setPivotY(0);
            imageView.setScaleX(thumbScaleX);
            imageView.setScaleY(thumbScaleY);
            imageView.setTranslationX(thumbLeft - screenLocation[0]);
            imageView.setTranslationY(thumbTop - screenLocation[1]);

            imageView.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .translationX(0)
                    .translationY(0)
                    .setDuration(animationDuration);
        } else {
            AnimationSet set = new AnimationSet(true);

            TranslateAnimation moveAnim = new TranslateAnimation(thumbLeft - screenLocation[0], 0, thumbTop - screenLocation[1], 0);
            set.addAnimation(moveAnim);

            ScaleAnimation scaleAnimation = new ScaleAnimation(thumbScaleX, 1, thumbScaleY, 1);
            set.addAnimation(scaleAnimation);

            set.setDuration(animationDuration);
            imageView.startAnimation(set);
        }
    }
}
