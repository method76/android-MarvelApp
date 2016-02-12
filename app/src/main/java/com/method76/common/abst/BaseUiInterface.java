package com.method76.common.abst;

import android.widget.ProgressBar;

/**
 * Created by Sungjoon Kim on 2016-02-10.
 */
public interface BaseUiInterface {
    void showToast(CharSequence text);
    void setBaseProgress(ProgressBar progress);
    ProgressBar getBaseProgress();
}
