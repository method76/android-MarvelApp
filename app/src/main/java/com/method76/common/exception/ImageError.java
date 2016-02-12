package com.method76.common.exception;

import android.support.annotation.NonNull;

/**
 * Created by Sungjoon Kim on 2016-02-06.
 */
public class ImageError extends Throwable {

    private int errorCode;
    public static final int ERROR_GENERAL_EXCEPTION = -1;
    public static final int ERROR_INVALID_FILE      = 0;
    public static final int ERROR_DECODE_FAILED     = 1;
    public static final int ERROR_FILE_EXISTS       = 2;
    public static final int ERROR_PERMISSION_DENIED = 3;
    public static final int ERROR_IS_DIRECTORY      = 4;

    public ImageError(@NonNull String message) {
        super(message);
    }

    public ImageError(@NonNull Throwable error) {
        super(error.getMessage(), error.getCause());
        this.setStackTrace(error.getStackTrace());
    }

    public ImageError setErrorCode(int code) {
        this.errorCode = code;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
