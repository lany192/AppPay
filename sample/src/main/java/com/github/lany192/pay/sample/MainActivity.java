package com.github.lany192.pay.sample;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.lany192.pay.ErrorCode;
import com.github.lany192.pay.PayUtils;
import com.github.lany192.pay.alipay.Alipay;
import com.github.lany192.pay.alipay.AlipayResultCallBack;
import com.github.lany192.pay.wxpay.WXPay;
import com.github.lany192.pay.wxpay.WXPayResultCallBack;
import com.hjq.toast.ToastUtils;
import com.lany.box.activity.BaseActivity;
import com.lany.box.config.ActivityConfig;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.editWXParam)
    EditText editWXParam;
    @BindView(R.id.editAlipayParam)
    EditText editAlipayParam;
    @BindView(R.id.btnWXPay)
    Button btnWXPay;
    @BindView(R.id.btnAliPay)
    Button btnAlipay;
    @BindView(R.id.btnWXClear)
    Button btnWXClear;
    @BindView(R.id.btnWXPaste)
    Button btnWXPaste;
    @BindView(R.id.btnAliPayClear)
    Button btnAliPayClear;
    @BindView(R.id.btnAliPayPaste)
    Button btnAliPayPaste;
    @BindView(R.id.btnGetIp)
    Button btnGetIp;

    @NonNull
    @Override
    protected ActivityConfig getConfig(ActivityConfig config) {
        return config.layoutId(R.layout.activity_main).hasBackBtn(false);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        btnAlipay.setOnClickListener(this);
        btnWXPay.setOnClickListener(this);
        btnWXClear.setOnClickListener(this);
        btnWXPaste.setOnClickListener(this);
        btnAliPayClear.setOnClickListener(this);
        btnAliPayPaste.setOnClickListener(this);
        btnGetIp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnWXPay:
                String wx_pay_param = editWXParam.getText().toString();
                if (TextUtils.isEmpty(wx_pay_param)) {
                    Toast.makeText(getApplication(), "请输入参数", Toast.LENGTH_SHORT).show();
                    return;
                }
                doWXPay(wx_pay_param);
                break;

            case R.id.btnWXClear:
                editWXParam.setText("");
                break;

            case R.id.btnWXPaste:
                ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                editWXParam.setText(cbm.getText());
                break;

            case R.id.btnAliPay:
                String alipay_pay_param = editAlipayParam.getText().toString();
                if (TextUtils.isEmpty(alipay_pay_param)) {
                    Toast.makeText(getApplication(), "请输入参数", Toast.LENGTH_SHORT).show();
                    return;
                }
                doAlipay(alipay_pay_param);
                break;

            case R.id.btnAliPayClear:
                editAlipayParam.setText("");
                break;

            case R.id.btnAliPayPaste:
                ClipboardManager cbm2 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                editAlipayParam.setText(cbm2.getText());
                break;

            case R.id.btnGetIp:
                String ip = PayUtils.getIpAddress();
                if (ip != null) {
                    Toast.makeText(getApplication(), ip, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "获取ip失败", Toast.LENGTH_SHORT).show();
                }

            default:
                break;
        }
    }

    /**
     * 支付宝支付
     *
     * @param pay_param 支付服务生成的支付参数
     */
    private void doAlipay(String pay_param) {
        new Alipay(this, pay_param, new AlipayResultCallBack() {
            @Override
            public void onSuccess() {
                ToastUtils.show("支付成功");
            }

            @Override
            public void onDealing() {
                ToastUtils.show("支付处理中...");
            }

            @Override
            public void onError(ErrorCode code) {
                switch (code) {
                    case ERROR_RESULT:
                        Toast.makeText(getApplication(), "支付失败:支付结果解析错误", Toast.LENGTH_SHORT).show();
                        break;

                    case ERROR_NETWORK:
                        Toast.makeText(getApplication(), "支付失败:网络连接错误", Toast.LENGTH_SHORT).show();
                        break;

                    case ERROR_PAY:
                        Toast.makeText(getApplication(), "支付错误:支付码支付失败", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        ToastUtils.show("支付错误");
                        break;
                }
            }

            @Override
            public void onCancel() {
                ToastUtils.show("支付取消");
            }
        }).pay();
    }

    /**
     * 微信支付
     *
     * @param pay_param 支付服务生成的支付参数
     */
    private void doWXPay(String pay_param) {
        String wx_appid = "wxXXXXXXX";     //替换为自己的appid
        WXPay.init(getApplicationContext(), wx_appid);      //要在支付前调用
        WXPay.getInstance().pay(pay_param, new WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                ToastUtils.show("支付成功");
            }

            @Override
            public void onError(ErrorCode code) {
                switch (code) {
                    case ERROR_NO_OR_LOW_WX:
                        Toast.makeText(getApplication(), "未安装微信或微信版本过低", Toast.LENGTH_SHORT).show();
                        break;

                    case ERROR_PAY_PARAM:
                        Toast.makeText(getApplication(), "参数错误", Toast.LENGTH_SHORT).show();
                        break;

                    case ERROR_PAY:
                        Toast.makeText(getApplication(), "支付失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onCancel() {
                ToastUtils.show("支付取消");
            }
        });
    }
}
