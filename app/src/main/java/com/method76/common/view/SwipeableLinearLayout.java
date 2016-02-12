package com.method76.common.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.method76.common.util.Log;


/**
 * Created by Sungjoon Kim on 2016-02-09.
 */
public class SwipeableLinearLayout extends LinearLayout {

    boolean mIsScrolling;
    int mTouchSlop;
    GestureDetector gestureDetector;

    public SwipeableLinearLayout(Context context, int mTouchSlop) {
        super(context);
        gestureDetector = new GestureDetector(new SwipeGestureDetector());
//        this.mTouchSlop = mTouchSlop;
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         */
        final int action = MotionEventCompat.getActionMasked(ev);

        // Always handle the case of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the scroll.
            mIsScrolling = false;
            return false; // Do not intercept touch event, let the child handle it
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                if (mIsScrolling) {
                    // We're currently scrolling, so yes, intercept the
                    // touch event!
                    return true;
                }

                // If the user has dragged her finger horizontally more than
                // the touch slop, start the scroll

                // left as an exercise for the reader
//                final int xDiff = calculateDistanceX(ev);
//
//                // Touch slop should be calculated using ViewConfiguration
//                // constants.
//                if (xDiff > mTouchSlop) {
//                    // Start scrolling!
//                    mIsScrolling = true;
//                    return true;
//                }
                break;
            }
        }

        // In general, we don't want to intercept touch events. They should be
        // handled by the child view.
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Here we actually handle the touch event (e.g. if the action is ACTION_MOVE,
        // scroll this container).
        // This method will only be called if the touch event was intercepted in
        // onInterceptTouchEvent
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
        // Do something
    }

    private void onRightSwipe() {
        // Do something
        Log.d("ㅋㅋㅋㅋㅋ");
    }


    // Private class for gestures
    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE         = 120;
        private static final int SWIPE_MAX_OFF_PATH         = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY   = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {

            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    // Left swipe
                    Log.d("Left");
                    SwipeableLinearLayout.this.onLeftSwipe();
                } else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    // Right swipe
                    Log.d("Right");
                    SwipeableLinearLayout.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e(e);
            }
            return false;
        }
    }
}
