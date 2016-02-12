package com.method76.common.callback;

/**
 * Created by Sungjoon Kim on 2016-02-01.
 */
public interface ItemPurchaseListener {
    void onBefore();
    void onSuccess();
    void onFail(Exception error);
}
