package com.method76.common.handler;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.method76.common.abst.BaseUiInterface;
import com.method76.common.constant.CommonConst;
import com.method76.common.data.BaseSession;
import com.method76.common.util.Log;


/**
 * Created by Sungjoon Kim on 2016-02-10.
 */
public class SessionHandler implements CommonConst{

    private static SessionHandler mInstance;
    private Context mCtx;
    private BaseUiInterface uiHandler;
    private BaseSession session;


    private SessionHandler(Context context) {
        this.mCtx = context.getApplicationContext();
        this.uiHandler = (BaseUiInterface)context;
    }

    public static synchronized SessionHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SessionHandler(context);
        }
        return mInstance;
    }

    public BaseSession initialize(){
        session = getSession();
        return session;
    }

    public void clear(){
        remove(SESSION);
    }

    private BaseSession getSession(){
        Gson gson = new Gson();
        BaseSession session = null;
        try {
            session = gson.fromJson(get(SESSION), BaseSession.class);
        }catch(Exception e){
            Log.e(e);
        }
        return session;
    }

    // 값 불러오기
    private String get(String key){
        SharedPreferences pref = mCtx.getSharedPreferences(PREF_BASE_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    // 값 저장하기
    private void save(String key, String value){
        SharedPreferences pref = mCtx.getSharedPreferences(PREF_BASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // 값(Key Data) 삭제하기
    private void remove(String key){
        SharedPreferences pref = mCtx.getSharedPreferences(PREF_BASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.apply();
    }

    // 값(ALL Data) 삭제하기
    private void removeAll(){
        SharedPreferences pref = mCtx.getSharedPreferences(PREF_BASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

}
