package com.bear.neteasemusic.api;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginRequest extends ApiRequest {
    public LoginRequest(String username, String password) {
        this.setUserName(username);
        this.setPlainPassword(password);
    }

    @Override
    public String getPath() {
        return "login";
    }

    private static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private String userName;
    @JSONField(name = "username")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String name)
    {
        userName = name;
    }

    private String password;
    @JSONField(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPlainPassword(String pwd) {
        digest.reset();
        digest.update(pwd.getBytes());
        password = Hex.encodeHexString(digest.digest());
    }

    @JSONField(name = "rememberLogin")
    public boolean getRememberLogin() {
        return true;
    }
}
