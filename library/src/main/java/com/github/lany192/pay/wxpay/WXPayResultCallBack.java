package com.github.lany192.pay.wxpay;

import com.github.lany192.pay.ErrorCode;

/**
 * 微信支付回调
 */
public interface WXPayResultCallBack {
    /**
     * 支付成功
     */
    void onSuccess();

    /**
     * 支付失败
     *
     * @param code 错误码
     */
    void onError(ErrorCode code);

    /**
     * 支付取消
     */
    void onCancel();
}