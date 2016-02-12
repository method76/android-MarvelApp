package com.method76.common.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.method76.common.util.Log;
import com.method76.common.util.SingletonManager;


/**
 * Created by Sungjoon Kim on 2016-01-28.
 * Ex) formKey = "dHNCaXVkZjJUMnVvbUZELUNPWmFNUUE6MA"
 * Another : http://urqa.io/urqa/
 */
public class BaseApplication extends Application{

    private Context context;
    private static boolean isDebuggable = true;
    private static BaseApplication application;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        application = this;
        initApp();
    }


    /**
     * 애플리케이션 초기화
     */
    private void initApp(){
        // Init Debugger
        try {
            ApplicationInfo appinfo = getPackageManager().getApplicationInfo(getPackageName(), 0);
            isDebuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
            isDebuggable = true;
            Log.d("pkg: " + getPackageName() + ", isDebuggable: " + isDebuggable);
        } catch (PackageManager.NameNotFoundException e) {
            /* debuggable variable will remain false */
            e.printStackTrace();
        }
    }

    /**
     * 현재 디버그모드여부를 리턴
     *
     * @return
     */
    public static boolean isDebuggable() {
        return isDebuggable;
    }


    public void restartPackege(){
//        Toast.makeText(this, R.string.txt_fail_change_succ, Toast.LENGTH_SHORT).show();
        // 앱 종료 후 재시작: 지연시간 2초 부여
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = getBaseContext().getPackageManager().
                        getLaunchIntentForPackage(getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 2000);
    }

    public RequestQueue getRequestQueue(){
        return SingletonManager.getInstance(context).getRequestQueue();
    }
}
