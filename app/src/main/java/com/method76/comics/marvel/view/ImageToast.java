package com.method76.comics.marvel.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.method76.comics.marvel.R;

/**
 * Toast that showing image icon
 * Created by Sungjoon Kim on 2016-02-20.
 */
public class ImageToast extends Toast {

    public enum Status{
        NEUTRAL,
        POSITIVE,
        NEGATIVE
    }
    private Context ctx;
    private int mDuration;
    private View mNextView;

    public ImageToast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, CharSequence text,
                         @Snackbar.Duration int duration, Status status) {
        ImageToast result = new ImageToast(context);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast_custom, null);
        result.setView(v);
        ImageView icon = (ImageView)v.findViewById(R.id.icon);
        if(status == Status.POSITIVE){
            icon.setImageResource(R.drawable.ic_fb_like);
        }else if(status == Status.NEGATIVE){
            icon.setImageResource(R.drawable.ic_fb_dislike);
        }else{
            icon.setVisibility(View.GONE);
        }
        TextView tv = (TextView)v.findViewById(R.id.message);
        tv.setText(text);
        result.mNextView = v;
        result.mDuration = duration;
        return result;
    }

    public static Toast makeText(Context context, @StringRes int resId,
                             @Snackbar.Duration int duration, Status status)
            throws Resources.NotFoundException {
        ImageToast result = new ImageToast(context);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast_custom, null);
        result.setView(v);
        ImageView icon = (ImageView)v.findViewById(R.id.icon);
        if(status == Status.NEUTRAL){
            icon.setImageResource(R.drawable.ic_fb_like);
        }else if(status == Status.NEGATIVE){
            icon.setImageResource(R.drawable.ic_fb_dislike);
        }else{
            icon.setVisibility(View.GONE);
        }
        TextView tv = (TextView)v.findViewById(R.id.message);
        tv.setText(context.getResources().getString(resId));
        result.mNextView = v;
        result.mDuration = duration;
        return result;
    }

    public static Toast makeText(Context context, @StringRes int resId,
                                 @Snackbar.Duration int duration)
            throws Resources.NotFoundException {
        return makeText(context, resId, duration, Status.NEUTRAL);
    }

    public static Toast makeText(Context context, CharSequence text,
                                 @Snackbar.Duration int duration) {
        return makeText(context, text, duration, Status.NEUTRAL);
    }
}
