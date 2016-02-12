package com.method76.common.util;

/**
 * Created by Sungjoon Kim on 2016-02-10.
 */

import com.method76.common.base.BaseApplication;

/**
 * Created by Sungjoon Kim on 2016-01-29.
 */
public class Log {

    /** Log Level Debug **/
    public static final void d() {
        if (BaseApplication.isDebuggable())android.util.Log.d(getFileName(), makeLogStr(""));
    }
    /** Log Level Debug **/
    public static final void d(String message) {
        if (BaseApplication.isDebuggable())android.util.Log.d(getFileName(), makeLogStr(message));
    }
    /** Log Level Debug **/
    public static final void d(int message) {
        if (BaseApplication.isDebuggable())android.util.Log.d(getFileName(), makeLogStr("" + message));
    }
    /** Log Level Error **/
    public static final void w(String message) {
        if (BaseApplication.isDebuggable())android.util.Log.w(getFileName(), makeLogStr(message));
    }
    /** Log Level Error **/
    public static final void w(Exception e) {
        if (BaseApplication.isDebuggable())android.util.Log.w(getFileName(), makeLogStr(e.getMessage()));
    }
    /** Log Level Error **/
    public static final void e(Exception e) {
        if (BaseApplication.isDebuggable())android.util.Log.e(getFileName(), makeLogStr(e.getMessage()));
    }


    /** Log Level Debug **/
    public static final void d(String tag, String message) {
        if (BaseApplication.isDebuggable())android.util.Log.d(tag, makeLogStr(message));
    }

    /** Log Level Error **/
    public static final void e(String tag, Exception e) {
        if (BaseApplication.isDebuggable())android.util.Log.e(tag, makeLogStr(e.getMessage()));
    }


    private static String getFileName() {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        return ste.getFileName().replace(".java", "");
    }

    private static String makeLogStr(String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append(ste.getMethodName());
        sb.append("()[" + ste.getLineNumber() + "]: ");
        sb.append(message);
        return sb.toString();
    }

}