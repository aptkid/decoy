package com.match.android.Utils;

/**
 * Created by Brant on 2017/8/22 23:54.
 */

public class OSSToken {

    private static String id;

    private static String secret;

    private static String token;

    public static String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public static String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
