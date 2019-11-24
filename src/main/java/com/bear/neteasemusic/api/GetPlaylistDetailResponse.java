package com.bear.neteasemusic.api;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class GetPlaylistDetailResponse extends ApiResponse {
    public static class Track {
        public long id;
        public String name;
        public String albumName;
        public String artistName;
    }

    private GetPlaylistDetailResponse() {}

    public static GetPlaylistDetailResponse parse(JSONObject response) {
        var resp = new GetPlaylistDetailResponse();
        if (response.getIntValue("code") != 200) {
            resp.ok = false;
            resp.reason = "未知错误。";
        } else {
            resp.ok = true;
            JSONObject listObject = response.getJSONObject("playlist");
            resp.tracks = listObject.getJSONArray("tracks").stream().map((Object x) -> {
                JSONObject obj = (JSONObject) x;
                Track t = new Track();
                t.id = obj.getLongValue("id");
                t.name = obj.getString("name");
                t.albumName = obj.getJSONObject("al").getString("name");
                t.artistName = String.join(" / ", obj.getJSONArray("ar").stream().map((Object o) -> ((JSONObject) o).getString("name")).collect(Collectors.toList()));
                return t;
            }).collect(Collectors.toList());
            resp.name = listObject.getString("name");
            resp.playedTimes = listObject.getIntValue("playCount");
            resp.description = listObject.getString("description");
            resp.coverUrl = listObject.getString("coverImgUrl");
        }
        return resp;
    }

    private List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }

    private String name;

    public String getName() {
        return name;
    }

    private int playedTimes;

    public int getPlayedTimes() {
        return playedTimes;
    }

    private String coverUrl;
    public String getCoverUrl() {
        return coverUrl;
    }

    private String description;
    public String getDescription() {
        return description;
    }
}
