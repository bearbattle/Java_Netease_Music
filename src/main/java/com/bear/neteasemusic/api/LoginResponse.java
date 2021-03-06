package com.bear.neteasemusic.api;

import com.alibaba.fastjson.JSONObject;

public class LoginResponse extends ApiResponse {

    private LoginResponse() {
    }

    public static LoginResponse parse(JSONObject response) {
        LoginResponse resp = new LoginResponse();
        if (response.getIntValue("code") != 200) {
            resp.ok = false;
            if (response.containsKey("msg"))
                resp.reason = response.getString("msg");
            else
                resp.reason = "用户名或密码错误。";
        } else {
            resp.ok = true;
            var profile = response.getJSONObject("profile");
            resp.signature = profile.getString("signature");
            resp.nickName = profile.getString("nickname");
            resp.userId = profile.getLongValue("userId");
            resp.avatarUrl = profile.getString("avatarUrl");
        }
        return resp;
    }

    private String nickName;

    public String getNickName() {
        return nickName;
    }

    private String signature;

    public String getSignature() {
        return signature;
    }

    private long userId;
    public long getUserId() {
        return userId;
    }

    private String avatarUrl;
    public String getAvatarUrl() {
        return avatarUrl;
    }
}
