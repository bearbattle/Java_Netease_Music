package com.bear.neteasemusic.api;

import com.alibaba.fastjson.JSONObject;

import java.util.stream.Collectors;

public class GetTrackUrlResponse extends ApiResponse {
    private String url;
    public String getUrl() {
        return url;
    }

    private GetTrackUrlResponse() { }

    public static GetTrackUrlResponse parse(JSONObject response) {
        GetTrackUrlResponse resp = new GetTrackUrlResponse();
        resp.ok = false;
        if (response.getIntValue("code") != 200) {
            resp.reason = "未知错误。";
        } else {
            var arr = response.getJSONArray("data");
            if (arr.size() > 0)
            {
                var first = (JSONObject)arr.get(0);
                if (first.getIntValue("code") == 200) {
                    resp.url = first.getString("url");
                    resp.ok = true;
                } else {
                    resp.reason = "歌曲暂不可用。";
                }
            }
        }
        return resp;
    }
}
