package com.method76.common.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Sungjoon Kim on 2016-02-01.
 */
public class CommonHelper {

    public static void showMessage(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

}
