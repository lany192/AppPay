package com.github.lany192.pay.wxpay;

import android.content.Context;
import android.text.TextUtils;

import com.github.lany192.pay.ErrorCode;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 微信支付
 */
public class WXPay {
    private static WXPay mWXPay;
    private IWXAPI mWXApi;
    private String mPayParam;
    private WXPayResultCallBack mCallback;

    public WXPay(Context context, String appId) {
        mWXApi = WXAPIFactory.createWXAPI(context, null);
        mWXApi.registerApp(appId);
    }

    public static void init(Context ctx, String appId) {
        if (mWXPay == null) {
            mWXPay = new WXPay(ctx, appId);
        }
    }

    public static WXPay getInstance() {
        return mWXPay;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    /**
     * 发起微信支付
     */
    public void pay(String params, WXPayResultCallBack callback) {
        mPayParam = params;
        mCallback = callback;
        //检测是否支持微信支付
        if (!mWXApi.isWXAppInstalled() || mWXApi.getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT) {
            if (mCallback != null) {
                mCallback.onError(ErrorCode.ERROR_NO_OR_LOW_WX);
            }
            return;
        }

        JSONObject param = null;
        try {
            param = new JSONObject(mPayParam);
        } catch (JSONException e) {
            e.printStackTrace();
            if (mCallback != null) {
                mCallback.onError(ErrorCode.ERROR_PAY_PARAM);
            }
            return;
        }
        if (TextUtils.isEmpty(param.optString("appid")) || TextUtils.isEmpty(param.optString("partnerid"))
                || TextUtils.isEmpty(param.optString("prepayid")) || TextUtils.isEmpty(param.optString("package")) ||
                TextUtils.isEmpty(param.optString("noncestr")) || TextUtils.isEmpty(param.optString("timestamp")) ||
                TextUtils.isEmpty(param.optString("sign"))) {
            if (mCallback != null) {
                mCallback.onError(ErrorCode.ERROR_PAY_PARAM);
            }
            return;
        }

        PayReq req = new PayReq();
        req.appId = param.optString("appid");
        req.partnerId = param.optString("partnerid");
        req.prepayId = param.optString("prepayid");
        req.packageValue = param.optString("package");
        req.nonceStr = param.optString("noncestr");
        req.timeStamp = param.optString("timestamp");
        req.sign = param.optString("sign");

        mWXApi.sendReq(req);
    }

    //支付回调响应
    public void onResp(int error_code) {
        if (mCallback == null) {
            return;
        }

        if (error_code == 0) {   //成功
            mCallback.onSuccess();
        } else if (error_code == -1) {   //错误
            mCallback.onError(ErrorCode.ERROR_PAY);
        } else if (error_code == -2) {   //取消
            mCallback.onCancel();
        }
        mCallback = null;
    }
}
