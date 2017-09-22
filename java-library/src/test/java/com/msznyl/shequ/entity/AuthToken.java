package com.msznyl.shequ.entity;

/**
 * 授权token
 */

public class AuthToken {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "token : " + token;
    }
}
