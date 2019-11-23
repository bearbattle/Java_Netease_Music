package com.bear.neteasemusic.api;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetUserPlaylistsResponse extends ApiResponse {

    private GetUserPlaylistsResponse() { }

    public static class Playlist {
        public String name;
        public int id;
        public int trackCount;
        public int playCount;
        public String coverImgUrl;
    }

    public static GetUserPlaylistsResponse parse(JSONObject response) {
        GetUserPlaylistsResponse resp = new GetUserPlaylistsResponse();
        if (response.getIntValue("code") != 200) {
            resp.ok = false;
            resp.reason = "未知错误。";
        } else {
            resp.ok = true;
            resp.lists = response.getJSONArray("playlist").stream().map((Object x)->{
                JSONObject obj = (JSONObject)x;
                Playlist p = new Playlist();
                p.name = obj.getString("name");
                p.id = obj.getIntValue("id");
                p.trackCount = obj.getIntValue("trackCount");
                p.playCount = obj.getIntValue("playCount");
                p.coverImgUrl = obj.getString("coverImgUrl");
                return p;
            }).collect(Collectors.toList());
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

    private List<Playlist> lists;
    public List<Playlist> getLists() {
        return lists;
    }
}
