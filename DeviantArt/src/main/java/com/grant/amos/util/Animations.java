package com.grant.amos.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by Grant on 10/16/13.
 */
public class Animations {
    static Animator mCurrentZoomAnimator;
    static int mShortAnimationDuration = 5000;

    public static void fade(final View view, float start, float end, int startVis, final int endVis){
        view.setAlpha(start);
        view.setVisibility(startVis);
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, View.ALPHA, start, end));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(endVis);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                view.setVisibility(endVis);
            }
        });
        set.start();
    }

    /*
    public static void zoomView(final View sourceView, final View destinationView, final boolean zoomSourceToDestination) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentZoomAnimator != null) {
            mCurrentZoomAnimator.cancel();
        }

        //System.out.println(sourceView.isDirty() + " " + destinationView.isDirty());
        //destinationView.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        //System.out.println(destinationView.getMeasuredHeight() + " " + destinationView.getMeasuredWidth());

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        sourceView.getGlobalVisibleRect(startBounds);
        destinationView.getGlobalVisibleRect(finalBounds);

        System.out.println(finalBounds);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        View viewToScale;

        if(zoomSourceToDestination)
            viewToScale = sourceView;
        else{
            viewToScale = destinationView;

            // Hide the thumbnail and show the zoomed-in view. When the animation
            // begins, it will position the zoomed-in view in the place of the
            // thumbnail.
            sourceView.setVisibility(View.INVISIBLE);
            destinationView.setVisibility(View.VISIBLE);
        }

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        viewToScale.setPivotX(0f);
        viewToScale.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(viewToScale, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(viewToScale, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(viewToScale, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(viewToScale, View.SCALE_Y,
                        startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentZoomAnimator = null;

                if(zoomSourceToDestination){
                    sourceView.setVisibility(View.GONE);
                    destinationView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentZoomAnimator = null;

                if(zoomSourceToDestination){
                    sourceView.setVisibility(View.GONE);
                    destinationView.setVisibility(View.VISIBLE);
                }
            }
        });
        set.start();
        mCurrentZoomAnimator = set;
    }
    */
    public static void zoomView(final View sourceView, final View destinationView, final boolean zoomSourceToDestination) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentZoomAnimator != null) {
            mCurrentZoomAnimator.cancel();
        }

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        sourceView.getGlobalVisibleRect(startBounds);
        destinationView.getGlobalVisibleRect(finalBounds);

        System.out.println(finalBounds);

        float startScaleY = (float) startBounds.height() / finalBounds.height();
        float startScaleX = (float) startBounds.width() / finalBounds.width();

        View viewToScale;

        if(zoomSourceToDestination)
            viewToScale = sourceView;
        else{
            viewToScale = destinationView;

            // Hide the thumbnail and show the zoomed-in view. When the animation
            // begins, it will position the zoomed-in view in the place of the
            // thumbnail.
            sourceView.setVisibility(View.INVISIBLE);
            destinationView.setVisibility(View.VISIBLE);
        }

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        viewToScale.setPivotX(0f);
        viewToScale.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(viewToScale, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(viewToScale, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(viewToScale, View.SCALE_X,
                        startScaleX, 1f))
                .with(ObjectAnimator.ofFloat(viewToScale, View.SCALE_Y,
                        startScaleY, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentZoomAnimator = null;

                if(zoomSourceToDestination){
                    sourceView.setVisibility(View.GONE);
                    destinationView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentZoomAnimator = null;

                if(zoomSourceToDestination){
                    sourceView.setVisibility(View.GONE);
                    destinationView.setVisibility(View.VISIBLE);
                }
            }
        });
        set.start();
        mCurrentZoomAnimator = set;
    }
}
