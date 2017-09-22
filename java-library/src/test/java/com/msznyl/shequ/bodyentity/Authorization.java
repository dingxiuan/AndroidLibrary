package com.msznyl.shequ.bodyentity;

import com.google.gson.annotations.SerializedName;

/**
 * 5.1.身份验证
 */
public class Authorization {

    /**
     * 由580人员提供
     */
    @SerializedName("appkey")
    private String appKey;
    /**
     * 请求报文序列号
     */
    @SerializedName("seq")
    private String requestSequence;
    /**
     * 账号
     */
    @SerializedName("account")
    private String account;
    /**
     * 密码（密文）
     */
    @SerializedName("passwd")
    private String password;
    /**
     * 签名
     */
    @SerializedName("signature")
    private String signature;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getRequestSequence() {
        return requestSequence;
    }

    public void setRequestSequence(String requestSequence) {
        this.requestSequence = requestSequence;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "AuthoJson{" +
                "appKey='" + appKey + '\'' +
                ", requestSequence='" + requestSequence + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
