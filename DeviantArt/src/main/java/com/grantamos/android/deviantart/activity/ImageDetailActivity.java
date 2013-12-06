package com.grantamos.android.deviantart.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.grantamos.android.deviantart.R;
import com.grantamos.android.deviantart.model.Image;
import com.grantamos.android.util.FadeInBitmapDrawable;
import com.grantamos.android.util.ScalableImageView;
import com.grantamos.android.util.VolleyHelper;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageDetailActivity extends Activity {

    Image imageData = null;

    ImageView imageView;
    TextView usernameTextView;
    TextView titleTextView;
    ImageView userIconImageView;

    PhotoViewAttacher mAttacher;

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageData = (Image) extras.get("imageData");
        }

        if(imageData == null)
            finish();

        mImageLoader = VolleyHelper.getInstance(getApplicationContext()).getImageLoader();

        setContentView(R.layout.activity_image_detail);
        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds data to the action bar if it is present.
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
        imageView = (ImageView) findViewById(R.id.detail_image_view);
        userIconImageView = (ImageView) findViewById(R.id.user_icon_image_view);
        usernameTextView = (TextView) findViewById(R.id.username);
        titleTextView = (TextView) findViewById(R.id.title);

        mAttacher = new PhotoViewAttacher(imageView);

        mImageLoader.get(imageData.thumb.url, getImageListener(imageView, 0, 0));

        if(imageData.image.url != null){
            mImageLoader.get(imageData.image.url, getImageListener(imageView, 0, 0));
        }

        if(imageData.title != null)
            titleTextView.setText(imageData.title);

        if(imageData.user.username != null)
            usernameTextView.setText(imageData.user.username);

        if(imageData.user.userIcon != null){
            mImageLoader.get(imageData.user.userIcon, getCroppedImageListener(userIconImageView, 0, 0));
        }
    }

    public ImageLoader.ImageListener getImageListener(final ImageView view, final int defaultImageResId, final int errorImageResId){
        return new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                    mAttacher.update();
                } else if (defaultImageResId != 0) {
                    view.setImageResource(defaultImageResId);
                }
            }
        };
    }

    public ImageLoader.ImageListener getCroppedImageListener(final ImageView view, final int defaultImageResId, final int errorImageResId){
        return new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    view.setImageDrawable(new FadeInBitmapDrawable(getResources(), getCroppedBitmap(response.getBitmap()), 500));
                } else if (defaultImageResId != 0) {
                    view.setImageResource(defaultImageResId);
                }
            }
        };
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
