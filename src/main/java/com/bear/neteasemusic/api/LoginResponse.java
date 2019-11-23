package com.bear.neteasemusic.api;

import com.alibaba.fastjson.JSONObject;

public class LoginResponse extends ApiResponse {

    private LoginResponse() {
    }

    public static LoginResponse parse(JSONObject response) {
        LoginResponse resp = new LoginResponse();
        if (response.getIntValue("code") != 200) {
            resp.ok = false;
            resp.reason = "用户名或密码错误。";
        } else {
            resp.ok = true;
            var profile = response.getJSONObject("profile");
            resp.signature = profile.getString("signature");
            resp.nickName = profile.getString("nickname");
            resp.userId = profile.getIntValue("userId");
            resp.avatarUrl = profile.getString("avatarUrl");
        }
        return resp;
    }

    private boolean ok;

    @Override
    public boolean isOk() {
        return ok;
    }

    private String reason;

    @Override
    public String errorReason() {
        return reason;
    }

    private String nickName;

    public String getNickName() {
        return nickName;
    }

    private String signature;

    public String getSignature() {
        return signature;
    }

    private int userId;
    public int getUserId() {
        return userId;
    }

    private String avatarUrl;
    public String getAvatarUrl() {
        return avatarUrl;
    }
}
