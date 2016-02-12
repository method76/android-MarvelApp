package com.method76.common.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import com.method76.common.util.Log;


/**
 * Created by Sungjoon Kim on 2016-01-29.
 */
public class BaseCompatFloatActivity extends BaseCompatActivity {

    final int BANNER_HEIGHT_PORT_DP = 50;

    final int BANNER_HEIGHT_MIN     = 32;
    final int BANNER_HEIGHT_MID     = 50;
    final int BANNER_HEIGHT_MAX     = 90;

    final int SCREEN_HEIGHT_MIN     = 0;
    final int SCREEN_HEIGHT_MID     = 400;
    final int SCREEN_HEIGHT_MAX     = 720;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateAdMargin();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateAdMargin();
    }

    private void updateAdMargin(){

        WindowManager wm = getWindowManager();

        if(wm == null) return;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.TOP;
        lp.width = width;

        if(wm.getDefaultDisplay().getRotation() == Surface.ROTATION_90
                || wm.getDefaultDisplay().getRotation() == Surface.ROTATION_270) {
            Log.d("landscape");
            int bannerHeight = 0;
            if (height >= SCREEN_HEIGHT_MIN && height <= SCREEN_HEIGHT_MID) {
                bannerHeight = BANNER_HEIGHT_MIN;
            } else if (height > SCREEN_HEIGHT_MID && height <= SCREEN_HEIGHT_MAX) {
                bannerHeight = BANNER_HEIGHT_MID;
            } else if (height > SCREEN_HEIGHT_MAX) {
                bannerHeight = BANNER_HEIGHT_MAX;
            }
            lp.height = height - bannerHeight; // pixelToDp(bannerHeight);
        }else {
            Log.d("portrait");
            lp.height = height - dpToPixel(BANNER_HEIGHT_PORT_DP);
        }
        getWindowManager().updateViewLayout(view, lp);
        Log.d("w/h: " + lp.width + "/" + lp.height);
    }

}
