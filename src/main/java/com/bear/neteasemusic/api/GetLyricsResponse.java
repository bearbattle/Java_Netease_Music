package com.bear.neteasemusic.api;

import com.alibaba.fastjson.JSONObject;

import java.util.stream.Collectors;

public class GetLyricsResponse extends ApiResponse {

    private GetLyricsResponse() {
    }

    public static GetLyricsResponse parse(JSONObject response) {
        var resp = new GetLyricsResponse();
        if (response.getIntValue("code") != 200) {
            resp.ok = false;
            resp.reason = "未知错误。";
        } else {
            var pureMusic = response.get("nolyric");
            var lyricMissing = response.get("sgc");
            var lrc = response.get("lrc");
            if (pureMusic instanceof Boolean && (Boolean)pureMusic)
            {
                resp.ok = true;
                resp.pure = true;
                resp.noLyric = false;
            }
            else if (lyricMissing instanceof Boolean && (Boolean)lyricMissing)
            {
                resp.ok = true;
                resp.pure = false;
                resp.noLyric = true;
            }
            else if (lrc != null)
            {
                String value = ((JSONObject)lrc).getString("lyric");
                resp.ok = true;
                resp.pure = resp.noLyric = false;
                resp.lyric = value;
            }
            else
            {
                resp.ok = false;
                resp.reason = "获取歌词失败。";
            }
        }
        return resp;
    }

    private boolean pure;

    public boolean isPureMusic() {
        return pure;
    }

    private boolean noLyric;

    public boolean isLyricMissing() {
        return noLyric;
    }

    private String lyric;

    public String getLyric() {
        return lyric;
    }
}
