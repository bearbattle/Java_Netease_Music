package com.bear.neteasemusic.api;

import com.alibaba.fastjson.annotation.JSONField;

public class GetLyricsRequest extends ApiRequest {
    @Override
    public String getPath() {
        return "song/lyric?lv=-1&kv=-1&tv=-1";
    }

    @Override
    public NeteaseAPI.EncryptionType getEncryptionMethod() {
        return NeteaseAPI.EncryptionType.LinuxAPI;
    }

    public GetLyricsRequest(long id) {
        this.id = id;
    }

    private long id;
    @JSONField(name = "id")
    public long getId(){
        return id;
    }
}
