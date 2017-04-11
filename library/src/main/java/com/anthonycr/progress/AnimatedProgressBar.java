/*
 * Copyright 2016 Anthony Restaino
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anthonycr.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import java.util.ArrayDeque;
import java.util.Queue;

@SuppressWarnings("unused")
public class AnimatedProgressBar extends View {

    // State variables
    private int mProgress = 0;
    private int mDrawWidth = 0;

    // Consumer variables
    private int mProgressColor;
    private boolean mBidirectionalAnimate = true;
    private int mAnimationDuration;

    // Animation interpolators
    private final Interpolator mAlphaInterpolator = new LinearInterpolator();
    private final Interpolator mProgressInterpolator = new BezierEaseInterpolator();

    private final Queue<Animation> mAnimationQueue = new ArrayDeque<>();

    private static final int PROGRESS_DURATION = 500;
    private static final int ALPHA_DURATION = 200;

    private static final int MAX_PROGRESS = 100;

    public AnimatedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnimatedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * Initialize the AnimatedProgressBar
     *
     * @param context is the context passed by the constructor
     * @param attrs   is the attribute set passed by the constructor
     */
    private void init(final Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AnimatedProgressBar, 0, 0);
        try {
            // Retrieve the style of the progress bar that the user hopefully set
            mProgressColor = array.getColor(R.styleable.AnimatedProgressBar_progressColor, Color.RED);
            mBidirectionalAnimate = array.getBoolean(R.styleable.AnimatedProgressBar_bidirectionalAnimate, false);
            mAnimationDuration = array.getInteger(R.styleable.AnimatedProgressBar_animationDuration, PROGRESS_DURATION);
        } finally {
            array.recycle();
        }
    }

    /**
     * Sets the duration of the animation that
     * runs on the progress bar.
     *
     * @param duration the duration of the animation,
     *                 in milliseconds.
     */
    public void setDuration(int duration) {
        mAnimationDuration = duration;
    }

    /**
     * Sets whether or not the view should animate
     * in both directions, or whether is should only
     * animate up.
     *
     * @param bidirectionalAnimate true to animate in both
     *                             directions, false to animate
     *                             only up.
     */
    public void setBidirectionalAnimate(boolean bidirectionalAnimate) {
        mBidirectionalAnimate = bidirectionalAnimate;
    }

    /**
     * Sets the color that the progress bar will be.
     * Calling this method will trigger a redraw.
     *
     * @param color the color that should be used to draw
     *              the progress bar.
     */
    public void setProgressColor(@ColorInt int color) {
        mProgressColor = color;
        invalidate();
    }

    /**
     * Returns the current progress value between 0 and 100
     *
     * @return progress of the view
     */
    public int getProgress() {
        return mProgress;
    }

    private final Paint mPaint = new Paint();
    private final Rect mRect = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(10);
        mRect.right = mRect.left + mDrawWidth;
        canvas.drawRect(mRect, mPaint);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        invalidate();
    }

    /**
     * sets the progress as an integer value between 0 and 100.
     * Values above or below that interval will be adjusted to their
     * nearest value within the interval, i.e. setting a value of 150 will have
     * the effect of setting the progress to 100. You cannot trick us.
     *
     * @param progress an integer between 0 and 100
     */
    public void setProgress(int progress) {
        // Progress cannot be greater than 100
        if (progress > MAX_PROGRESS) {
            progress = MAX_PROGRESS;
        } else if (progress < 0) {
            // progress cannot be less than 0
            progress = 0;
        }

        int width = getMeasuredWidth();

        // If the view is not laid out yet, then we can't
        // render the progress, so we post a runnable to
        // the view to set the progress, and return.
        final int finalProgress = progress;
        if (width == 0 && !ViewCompat.isLaidOut(this)) {
            post(new Runnable() {
                @Override
                public void run() {
                    setProgress(finalProgress);
                }
            });

            return;
        }

        if (getAlpha() < 1.0f) {
            fadeIn();
        }

        // Set the drawing bounds for the ProgressBar
        mRect.left = 0;
        mRect.top = 0;
        mRect.bottom = getBottom() - getTop();
        if (progress < mProgress && !mBidirectionalAnimate) {
            // Reset the view width if it is less than the
            // previous progress and we aren't using bidirectional animation.
            mDrawWidth = 0;
        } else if (progress == mProgress) {
            if (progress == MAX_PROGRESS) {
                fadeOut();
            }
        }

        // Store the current progress
        mProgress = progress;

        // Calculate the width delta
        final int deltaWidth = (width * mProgress / MAX_PROGRESS) - mDrawWidth;

        if (deltaWidth != 0) {
            // Animate the width change
            animateView(mDrawWidth, deltaWidth, width);
        }
    }

    /**
     * private method used to create and run the animation used to change the progress
     *
     * @param initialWidth is the width at which the progress starts at
     * @param deltaWidth   is the amount by which the width of the progress view will change
     * @param maxWidth     is the maximum width (total width of the view)
     */
    private void animateView(final int initialWidth, final int deltaWidth, final int maxWidth) {
        Animation fill = new ProgressAnimation(initialWidth, deltaWidth, maxWidth);

        fill.setDuration(mAnimationDuration);
        fill.setInterpolator(mProgressInterpolator);

        if (!mAnimationQueue.isEmpty()) {
            mAnimationQueue.add(fill);
        } else {
            startAnimation(fill);
        }
    }

    /**
     * fades in the progress bar
     */
    private void fadeIn() {
        animate().alpha(1)
                .setDuration(ALPHA_DURATION)
                .setInterpolator(mAlphaInterpolator)
                .start();
    }

    /**
     * fades out the progress bar
     */
    private void fadeOut() {
        animate().alpha(0)
                .setDuration(ALPHA_DURATION)
                .setInterpolator(mAlphaInterpolator)
                .start();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mProgress = bundle.getInt("progressState");
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("progressState", mProgress);
        return bundle;
    }

    private class ProgressAnimation extends Animation {

        private int mInitialWidth;
        private int mDeltaWidth;
        private int mMaxWidth;

        ProgressAnimation(int initialWidth, int deltaWidth, int maxWidth) {
            mInitialWidth = initialWidth;
            mDeltaWidth = deltaWidth;
            mMaxWidth = maxWidth;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int width = mInitialWidth + (int) (mDeltaWidth * interpolatedTime);
            if (width <= mMaxWidth) {
                mDrawWidth = width;
                invalidate();
            }
            if (Math.abs(1.0f - interpolatedTime) < 0.00001) {
                if (mProgress >= MAX_PROGRESS) {
                    fadeOut();
                }
                if (!mAnimationQueue.isEmpty()) {
                    startAnimation(mAnimationQueue.poll());
                }
            }
        }

        @Override
        public boolean willChangeBounds() {
            return false;
        }

        @Override
        public boolean willChangeTransformationMatrix() {
            return false;
        }
    }

}

