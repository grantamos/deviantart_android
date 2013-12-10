package com.grantamos.android.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;

/**
 * Created by granta on 12/6/13.
 */
public class FadeInBitmapDrawable extends BitmapDrawable {

    /**
     * A transition is about to start.
     */
    private static final int TRANSITION_STARTING = 0;

    /**
     * The transition has started and the animation is in progress
     */
    private static final int TRANSITION_RUNNING = 1;

    /**
     * No transition will be applied
     */
    private static final int TRANSITION_NONE = 2;

    private static final int TRANSITION_DONE = 3;

    /**
     * The current state of the transition. One of {@link #TRANSITION_STARTING},
     * {@link #TRANSITION_RUNNING} and {@link #TRANSITION_NONE}
     */
    private int mTransitionState = TRANSITION_NONE;

    private long mStartTimeMillis;
    private int mFrom;
    private int mTo;
    private int mDuration;
    private int mAlpha = 0;

    public FadeInBitmapDrawable(Resources resources, Bitmap bitmap, int duration) {

        super(resources, bitmap);
        startTransition(duration);
    }

    /**
     * Begin the second layer on top of the first layer.
     *
     * @param durationMillis The length of the transition in milliseconds
     */
    public void startTransition(int durationMillis) {
        mFrom = 0;
        mTo = 255;
        mAlpha = 0;
        mDuration = durationMillis;
        mTransitionState = TRANSITION_STARTING;

        if(durationMillis == 0){
            mTransitionState = TRANSITION_DONE;
            mAlpha = 255;
        }

        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        boolean done = true;

        switch (mTransitionState) {
            case TRANSITION_STARTING:
                mStartTimeMillis = SystemClock.uptimeMillis();
                done = false;
                mTransitionState = TRANSITION_RUNNING;
                break;

            case TRANSITION_RUNNING:
                if (mStartTimeMillis >= 0) {
                    float normalized = (float)
                            (SystemClock.uptimeMillis() - mStartTimeMillis) / mDuration;
                    done = normalized >= 1.0f;
                    normalized = Math.min(normalized, 1.0f);
                    mAlpha = (int) (mFrom  + (mTo - mFrom) * normalized);

                    if(done)
                        mTransitionState = TRANSITION_DONE;
                }
                break;
            case TRANSITION_DONE:
                break;
        }

        setAlpha(mAlpha);

        super.draw(canvas);

        if (!done) {
            invalidateSelf();
        }
    }
}
