package com.method76.comics.marvel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.method76.comics.marvel.common.constant.AppConst;
import com.method76.common.base.BaseCompatActivity;
import com.method76.common.util.Log;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by method76 on 2016-01-30.
 */
public class SplashActivity extends BaseCompatActivity implements AppConst {

    @Bind(R.id.splashFrame)
    FrameLayout splashFrame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setSmallCircles();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, SPLASH_DURATION);
    }


    private void setSmallCircles(){

        splashFrame.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int centerX = splashFrame.getMeasuredWidth()/2;
        int centerY = splashFrame.getMeasuredHeight()/2;

        // Border around
        for(int i=0;i<ALL_POINT_CNT;i++){
            double angle = 0;
            if(i==0){
                angle = START_ANGLE_SPLASH;
            }else{
                angle = START_ANGLE_SPLASH + (i * ((2 * Math.PI) / ALL_POINT_CNT));
            }

            if(i%5!=2) {
                int x = (int) (centerX + Math.cos(angle) * (centerX-dpToPixel(SIZE_S_CIRCLE*2/5)));
                int y = (int) (centerY + Math.sin(angle) * (centerY-dpToPixel(SIZE_S_CIRCLE*2/5)));

                ImageView sCircle = new ImageView(this);
                sCircle.setImageResource(R.drawable.ic_circle);
                FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                param.width = dpToPixel(SIZE_S_CIRCLE);
                param.height = dpToPixel(SIZE_S_CIRCLE);
                param.leftMargin = x;
                param.topMargin = y;
                param.gravity = Gravity.TOP | Gravity.LEFT;
                sCircle.setLayoutParams(param);
                splashFrame.addView(sCircle, 1);
            }
        }

        int dotCnt = 3;
        double angle45 = Math.PI/4f;
        for(int i=1;i<dotCnt;i++){

            int x = (int) ( centerX - Math.sin(angle45)*centerX * i / dotCnt  - dpToPixel(SIZE_S_CIRCLE/2));
            int y = (int) ( centerY - Math.cos(angle45)*centerY * i / dotCnt - dpToPixel(SIZE_S_CIRCLE/2));

            ImageView sCircle1 = new ImageView(this);
            ImageView sCircle2 = new ImageView(this);
            ImageView sCircle3 = new ImageView(this);
            ImageView sCircle4 = new ImageView(this);
            sCircle1.setImageResource(R.drawable.ic_circle);
            sCircle2.setImageResource(R.drawable.ic_circle);
            sCircle3.setImageResource(R.drawable.ic_circle);
            sCircle4.setImageResource(R.drawable.ic_circle);

            FrameLayout.LayoutParams param1 = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            param1.width = dpToPixel(SIZE_S_CIRCLE);
            param1.height = dpToPixel(SIZE_S_CIRCLE);
            param1.gravity = Gravity.TOP | Gravity.LEFT;
            param1.topMargin = y;
            param1.leftMargin = x;
            sCircle1.setLayoutParams(param1);

            FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            param2.width = dpToPixel(SIZE_S_CIRCLE);
            param2.height = dpToPixel(SIZE_S_CIRCLE);
            param2.gravity = Gravity.TOP | Gravity.RIGHT;
            param2.topMargin = y;
            param2.rightMargin = x;
            sCircle2.setLayoutParams(param2);

            FrameLayout.LayoutParams param3 = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            param3.width = dpToPixel(SIZE_S_CIRCLE);
            param3.height = dpToPixel(SIZE_S_CIRCLE);
            param3.gravity = Gravity.BOTTOM | Gravity.LEFT;
            param3.bottomMargin = y;
            param3.leftMargin = x;
            sCircle3.setLayoutParams(param3);

            FrameLayout.LayoutParams param4 = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            param4.width = dpToPixel(SIZE_S_CIRCLE);
            param4.height = dpToPixel(SIZE_S_CIRCLE);
            param4.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            param4.bottomMargin = y;
            param4.rightMargin = x;
            sCircle4.setLayoutParams(param4);

            splashFrame.addView(sCircle1, 1);
            splashFrame.addView(sCircle2, 1);
            splashFrame.addView(sCircle3, 1);
            splashFrame.addView(sCircle4, 1);

        }

    }

    private void startMainActivity(){
        Intent intent = new Intent(this, CharChooseActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

}
