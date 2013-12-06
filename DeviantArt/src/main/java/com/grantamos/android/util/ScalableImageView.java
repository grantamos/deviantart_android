package com.grantamos.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Grant on 9/12/13.
 */
public class ScalableImageView extends ImageView {

    public int imageHeight = 0;

    public int imageWidth = 0;

    public ScalableImageView(Context context) {
        super(context);
    }

    public ScalableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScalableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageBitmap(Bitmap bitmap, int duration){
        setImageDrawable(new FadeInBitmapDrawable(getResources(), bitmap, duration));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        boolean scaleToWidth = true;

        Drawable drawable = getDrawable();
        float iHeight = imageHeight;
        float iWidth = imageWidth;

        if(drawable != null){
            iHeight = drawable.getIntrinsicHeight();
            iWidth = drawable.getIntrinsicWidth();
        }

        if(iHeight <= 0 || iWidth <= 0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        if(widthMode == MeasureSpec.UNSPECIFIED){
            if(heightMode != MeasureSpec.UNSPECIFIED)
                scaleToWidth = false;
            else{
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
        }else if(widthMode != MeasureSpec.UNSPECIFIED && heightMode != MeasureSpec.UNSPECIFIED){
            scaleToWidth = (iWidth/width >= iHeight/height);
        }

        if(scaleToWidth){
            height = (int) (iHeight*(width/iWidth));
        }else{
            width = (int) (iWidth*(height/iHeight));
        }

        setMeasuredDimension(width, height);
    }
}