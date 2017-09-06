package com.dxa.okhttp.entity;

import com.google.gson.annotations.SerializedName;

/**
 */

public class UserInfo {
    @SerializedName("age")
    private String age;
    @SerializedName("bhno")
    private String bhno;
    @SerializedName("gender")
    private String gender;
    @SerializedName("lianxiren")
    private String lianxiren;
    @SerializedName("lxPhone")
    private String lxPhone;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBhno() {
        return bhno;
    }

    public void setBhno(String bhno) {
        this.bhno = bhno;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLianxiren() {
        return lianxiren;
    }

    public void setLianxiren(String lianxiren) {
        this.lianxiren = lianxiren;
    }

    public String getLxPhone() {
        return lxPhone;
    }

    public void setLxPhone(String lxPhone) {
        this.lxPhone = lxPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Info{" +
            "age='" + age + '\'' +
            ", bhno='" + bhno + '\'' +
            ", gender='" + gender + '\'' +
            ", lianxiren='" + lianxiren + '\'' +
            ", lxPhone='" + lxPhone + '\'' +
            ", name='" + name + '\'' +
            ", phone='" + phone + '\'' +
            '}';
    }
}
