package com.github.lany192.pay;


public enum ErrorCode {
    //
    UNKNOWN(0, "未知错误"),
    ERROR_NO_OR_LOW_WX(1, "未安装微信或微信版本过低"),
    ERROR_PAY_PARAM(2, "参数错误"),
    ERROR_PAY(3, "支付失败"),
    ERROR_RESULT(4, "支付结果解析错误"),
    ERROR_NETWORK(5, "网络连接错误");

    private int code;
    private String name;

    ErrorCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ErrorCode get(int value) {
        for (ErrorCode item : values()) {
            if (item.getCode() == value) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public static ErrorCode get(String name) {
        for (ErrorCode item : values()) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}