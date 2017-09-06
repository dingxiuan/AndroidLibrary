package com.dxa.okhttp.entity;

import com.google.gson.annotations.SerializedName;

public class HttpResult<T> {

    @SerializedName("data")
    private T data;
    @SerializedName("reason")
    private String reason;
    @SerializedName("statusCode")
    private int statusCode;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "data=" + data +
                ", reason='" + reason + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
