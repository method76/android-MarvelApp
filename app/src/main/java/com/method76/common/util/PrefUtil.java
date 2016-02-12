package com.method76.common.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sungjoon Kim on 2016-02-07.
 */
public class PrefUtil {

    private final String PREF_BASE_NAME = "basePref";
    private static Context mCtx;
    private static PrefUtil util = new PrefUtil();

    private PrefUtil(){}
    public static PrefUtil getInstance(Context context){
        mCtx = context;
        return util;
    }

    // 값 불러오기
    public String get(String key){
        SharedPreferences pref = mCtx.getSharedPreferences(PREF_BASE_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    // 값 저장하기
    public void put(String key, String value){
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
    public void removeAll(){
        SharedPreferences pref = mCtx.getSharedPreferences(PREF_BASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
