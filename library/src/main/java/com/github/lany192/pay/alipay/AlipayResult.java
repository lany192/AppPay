package com.github.lany192.pay.alipay;

import java.util.Map;

public class AlipayResult {
    private String status;
    private String result;
    private String msg;

    public AlipayResult(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return;
        }
        status = map.get("resultStatus");
        result = map.get("result");
        msg = map.get("memo");
    }

    public String getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "AlipayResult{" +
                "status='" + status + '\'' +
                ", result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
