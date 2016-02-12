package com.method76.common.base;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.method76.comics.marvel.common.constant.AppConst;
import com.method76.common.abst.BaseUiInterface;
import com.method76.common.constant.CommonConst;

import java.util.Calendar;


/**
 * Created by Sungjoon Kim on 2016-01-29.
 */
public class BaseCompatActivity extends AppCompatActivity implements
        CommonConst, AppConst, BaseUiInterface {

    private BaseApplication application;
    private boolean sessionExpired;
    private long lastTimestamp;
    private Toast mCurrentToast;
    private ProgressBar progress;
    protected boolean canEscape;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (BaseApplication)getApplication();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSessionExpired();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkSessionExpired();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean checkSessionExpired(){
        if(lastTimestamp != 0){
            long interval = getThisTIme() - lastTimestamp;
            if(interval > TIMEOUT_SESSION){
                return true;
            }
        }
        return false;
    }

    public String getClassname(){
        return this.getClass().getSimpleName();
    }

    public BaseApplication getCustomApplication(){
        return (BaseApplication)getApplication();
    }

    private long getThisTIme(){
       return  Calendar.getInstance().getTimeInMillis();
    }

    public int dpToPixel(float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public int pixelToDp(int px) {
        return (int) (px / getResources().getDisplayMetrics().density);
    }

    @Override
    public void showToast(CharSequence text) {
        if (null != mCurrentToast) {
            mCurrentToast.cancel();
        }
        mCurrentToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mCurrentToast.show();
    }
    @Override
    public ProgressBar getBaseProgress() {
        return this.progress;
    }
    @Override
    public void setBaseProgress(ProgressBar progress) {
        this.progress = progress;
    }


    public BaseApplication getBaseApplication(){
        return application;
    }

}
