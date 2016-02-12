package com.method76.common.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;

import com.method76.common.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sungjoon Kim on 2016-02-05.
 */
public class BaseSmsActivity extends BaseCompatActivity {

    private static final String SMS_MESSAGE			= "android.provider.Telephony.SMS_RECEIVED";
    private static final String EXTRA_PDUS			= "pdus";
    private static final String SMS_HEADER_METHOD 	= "[method76]";

    private int timerCount;
    private BroadcastReceiver mReceiver;
    private TimerTask mTask;
    private Timer mTimer;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver();
        mTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        substractSec();
                    }
                });
            }
        };
        initTimer(false);
    }

    /**
     * SMS리시버 등록
     */
    private void registerReceiver() {
        if (mReceiver != null) {
            return;
        }
        IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(SMS_MESSAGE);
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(SMS_MESSAGE)) {
                    parseSms(intent);
                }
            }
        };
        this.registerReceiver(this.mReceiver, theFilter);
    }

    private void initTimer(boolean retry) {
        timerCount = 60 * 3;
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.schedule(mTask, 0, 1000);
        if (retry) {
//            Toast.makeText(this, R.string.txt_sms_resent, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, R.string.txt_sms_sent, Toast.LENGTH_SHORT).show();
        }
    }

    private void substractSec() {
        int min = (int) Math.ceil(timerCount / 60);
        int sec = (int) Math.ceil(timerCount % 60);
        if (min < 0) {
            min = 0;
        }
        if (sec < 0) {
            sec = 0;
        }
        String minStr = (min < 10) ? ("0" + min) : ("" + min);
        String secStr = (sec < 10) ? ("0" + sec) : ("" + sec);
        String timerStr = minStr + " : " + secStr;
//        txtCountForm.setText(timerStr);
        timerCount--;
    }

    private void parseSms(Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String sender = null;
        String content = null;
        if (bundle != null) {
            // ---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get(EXTRA_PDUS);
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sender = msgs[i].getOriginatingAddress();
                content = msgs[i].getMessageBody().toString();
            }
			if (content != null && content.startsWith(SMS_HEADER_METHOD)) {
				// 특정 패턴의 SMS가 오면 영상으로 이동하는 샘플 로직 추가
                Log.d("onReceive str: " + sender + "/" + content);
				try {
					int strStart = content.lastIndexOf("[");
					int strEnd = content.lastIndexOf("]");
					String smsStr = content.substring(strStart + 1, strEnd);
				} catch (Exception e) {
                    Log.e(e);
				}
			}
            // ---display the new SMS message---
        }
    }

}
