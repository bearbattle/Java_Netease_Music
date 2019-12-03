package com.bear.neteasemusic.api;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class SearchResponse extends ApiResponse {
    private SearchResponse() {}
    public static SearchResponse parse(JSONObject response) {
        SearchResponse resp = new SearchResponse();
        if (response.getIntValue("code") != 200) {
            resp.ok = false;
            resp.reason = "未知错误。";
        } else {
            resp.ok = true;

            JSONObject listObject = response.getJSONObject("result");
            resp.tracks = listObject.getJSONArray("songs").stream().map((Object x) -> {
                JSONObject obj = (JSONObject) x;
                TrackInfo t = new TrackInfo();
                t.id = obj.getLongValue("id");
                t.name = obj.getString("name");
                t.isFee = obj.getIntValue("fee") == 1;
                t.feeValue = obj.getIntValue("fee");

                t.albumName = obj.getJSONObject("album").getString("name");
                t.albumCoverUrl = obj.getJSONObject("album").getString("picId");
                t.artistName = String.join(" / ", obj.getJSONArray("artists").stream().map((Object o) -> ((JSONObject) o).getString("name")).collect(Collectors.toList()));
                return t;
            }).collect(Collectors.toList());
        }
        return resp;
    }

    private List<TrackInfo> tracks;

    public List<TrackInfo> getTracks() {
        return tracks;
    }
}
