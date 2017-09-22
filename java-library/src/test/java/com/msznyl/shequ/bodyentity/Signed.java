package com.msznyl.shequ.bodyentity;

import com.google.gson.annotations.SerializedName;

/**
 * 通过居民证件查询签约数据
 */
public class Signed {
    /**
     * 查询开始日期，格式为 yyyy-MM-dd HH:mm:ss
     */
    @SerializedName("begin_date")
    private String beginDate;
    /**
     * 查询结束日期，格式为 yyyy-MM-dd HH:mm:ss
     */
    @SerializedName("end_date")
    private String endDate;
    /**
     * 分页参数，当前页号
     */
    @SerializedName("page")
    private int page;
    /**
     * 分页参数，页长。超过100时按100处理
     */
    @SerializedName("rows")
    private int rows;
    /**
     * 要查询的机构编码，无此参数查询账号下所有机构的数据；多个机构使用“,”隔开，如“001,002,003”
     */
    @SerializedName("hos_code")
    private String hosCode;
    /**
     * 由580人员提供
     */
    @SerializedName("appkey")
    private String appkey;
    /**
     * 请求报文序列号
     */
    @SerializedName("seq")
    private String requestSequence;
    /**
     * 签名
     */
    @SerializedName("signature")
    private String signature;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getHosCode() {
        return hosCode;
    }

    public void setHosCode(String hosCode) {
        this.hosCode = hosCode;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getRequestSequence() {
        return requestSequence;
    }

    public void setRequestSequence(String requestSequence) {
        this.requestSequence = requestSequence;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
