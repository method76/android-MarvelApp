package com.method76.common.callback;

import com.method76.common.exception.ImageError;

/**
 * Created by Sungjoon Kim on 2016-02-06.
 */
public interface OnBitmapSaveListener {
    void onBitmapSaved();
    void onBitmapSaveError(ImageError error);
}
