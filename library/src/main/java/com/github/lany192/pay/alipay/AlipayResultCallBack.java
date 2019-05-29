package com.github.lany192.pay.alipay;

import com.github.lany192.pay.ErrorCode;

/**
 * 支付宝支付回调
 */
public interface AlipayResultCallBack {
    /**
     * 支付成功
     */
    void onSuccess();

    /**
     * 正在处理中 小概率事件 此时以验证服务端异步通知结果为准
     */
    void onDealing();

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
