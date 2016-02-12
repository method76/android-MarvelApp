package com.method76.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.method76.comics.marvel.common.constant.AppConst;
import com.method76.common.abst.BaseUiInterface;
import com.method76.common.constant.CommonConst;


/**
 * Created by Sungjoon Kim on 2016-02-06.
 */
public class BaseFragment extends Fragment implements
        CommonConst, AppConst, BaseUiInterface {

    private BaseApplication application;
    private ProgressBar progress;
    private Toast mCurrentToast;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        application = ((BaseApplication) getActivity().getApplication());
    }

    public BaseApplication getBaseApplication(){
        return application;
    }

    @Override
    public ProgressBar getBaseProgress() {
        return this.progress;
    }

    @Override
    public void setBaseProgress(ProgressBar progress) {
        this.progress = progress;
    }
    @Override
    public void showToast(CharSequence text) {
        if (null != mCurrentToast) {
            mCurrentToast.cancel();
        }
        mCurrentToast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        mCurrentToast.show();
    }
}
