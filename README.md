[![](https://jitpack.io/v/lany192/AppPay.svg)](https://jitpack.io/#lany192/AppPay)
# AppPay

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

> 对微信支付和支付宝支付的App端SDK进行二次封装，对外提供一个较为简单的接口和支付结果回调

## 1. 如何添加

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

	dependencies {
	        implementation 'com.github.lany192:AppPay:1.0.1'
	}

## 2. Android Manifest配置

**权限声明**

```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

**注册activity**

```xml
        <!--微信支付必要配置-->
        <activity-alias
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:targetActivity="com.github.lany192.pay.wxpay.WXPayCallbackActivity" />
```

## 3. 发起支付

### 3.1 微信支付

```java
   /**
     * 微信支付
     * @param pay_param 支付服务生成的支付参数
     */
    private void doWXPay(String pay_param) {
        String wxAppId = "wxXXXXXXX";     //替换为自己的appid
        WXPay.init(getApplicationContext(), wxAppId);      //要在支付前调用
        WXPay.getInstance().pay(pay_param, new WXPayCallBack() {
            @Override
            public void onSuccess() {
                ToastUtils.show("支付成功");
            }

            @Override
            public void onError(ErrorCode code) {
                switch (code) {
                    case ERROR_NO_OR_LOW_WX:
                        ToastUtils.show("未安装微信或微信版本过低");
                        break;

                    case ERROR_PAY_PARAM:
                        ToastUtils.show("参数错误");
                        break;

                    case ERROR_PAY:
                        ToastUtils.show("支付失败");
                        break;
                }
            }

            @Override
            public void onCancel() {
                ToastUtils.show("支付取消");
            }
        });
    }
```

### 3.2 支付宝支付

```java
   /**
     * 支付宝支付
     * @param pay_param 支付服务生成的支付参数
     */
    private void doAlipay(String pay_param) {
        new Alipay(this, pay_param, new AlipayCallBack() {
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
                        ToastUtils.show("支付失败:支付结果解析错误");
                        break;

                    case ERROR_NETWORK:
                        ToastUtils.show("支付失败:网络连接错误");
                        break;

                    case ERROR_PAY:
                        ToastUtils.show("支付错误:支付码支付失败");
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
```

## Demo使用

项目中Demo支付宝支付可以直接使用。微信支付需要修改以下：

1. appid需要替换为自己的微信appid
1. PackageName和ApplicationId设置为自己的（与微信开放平台填入的包名一致）
1. 使用自己的签名进行打包（与微信开放平台填入签名一致）



## 混淆

```
#pay
-dontwarn com.github.lany192.pay.**
-keep class com.github.lany192.pay.**{*;}

#weixin
-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

#alipay
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}

```

License
-------

    Copyright 2019 YG.Lan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
