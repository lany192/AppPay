package com.github.lany192.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.github.lany192.pay.ErrorCode;

import java.util.Map;

/**
 * 支付宝支付
 */
public class Alipay {
    private final String TAG = getClass().getSimpleName();
    private String mParams;
    private PayTask mPayTask;
    private AlipayCallBack mCallback;

    public Alipay(Activity activity, String params, AlipayCallBack callback) {
        mParams = params;
        mCallback = callback;
        mPayTask = new PayTask(activity);
    }

    //支付
    public void pay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String, String> map = mPayTask.payV2(mParams, true);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCallback == null) {
                            return;
                        }
                        if (map == null) {
                            mCallback.onError(ErrorCode.ERROR_RESULT);
                            return;
                        }
                        AlipayResult result = new AlipayResult(map);
                        Log.d(TAG, "支付结果:" + result);
                        String status = result.getStatus();
                        if (TextUtils.equals(status, "9000")) {    //支付成功
                            mCallback.onSuccess();
                        } else if (TextUtils.equals(status, "8000")) { //支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            mCallback.onDealing();
                        } else if (TextUtils.equals(status, "6001")) {        //支付取消
                            mCallback.onCancel();
                        } else if (TextUtils.equals(status, "6002")) {     //网络连接出错
                            mCallback.onError(ErrorCode.ERROR_NETWORK);
                        } else if (TextUtils.equals(status, "4000")) {        //支付错误
                            mCallback.onError(ErrorCode.ERROR_PAY);
                        }
                    }
                });
            }
        }).start();
    }
}
