package com.msznyl.shequ.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 请求
 */

public class HttpResult<T> {

    @SerializedName("encrypted")
    private boolean encrypted;
    @SerializedName("total")
    private int total;
    @SerializedName("page")
    private int page;
    @SerializedName("rows")
    private int rows;
    @SerializedName("err")
    private int err;
    @SerializedName("errmsg")
    private String errMsg;
    @SerializedName("data")
    private T data;

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "encrypted=" + encrypted +
                ", total=" + total +
                ", page=" + page +
                ", rows=" + rows +
                ", err=" + err +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
