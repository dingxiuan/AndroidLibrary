package com.msznyl.shequ.bodyentity;

import com.google.gson.annotations.SerializedName;

/**
 * 5.2.签约机构相关信息初始化
 */
public class SearchSignedId {
    /**
     * 证件类型 1:身份证, 2:护照, 3:军官证, 4:港澳通行证, 5:回乡证, 6:出生证
     */
    @SerializedName("resident_id_ty")
    private String type;
    /**
     * 证件号码
     */
    @SerializedName("resident_id_num")
    private String num;
    /**
     * 要查询的机构编码，无此参数查询账号下所有机构的数据；多个机构使用“,”隔开，如“001,002,003”
     */
    @SerializedName("hos_code")
    private String historyCode;
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
     * 签名
     */
    @SerializedName("signature")
    private String signature;


}
