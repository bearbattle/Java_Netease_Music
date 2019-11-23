package com.bear.neteasemusic.api;

import com.alibaba.fastjson.annotation.JSONField;

public class GetUserPlaylistsRequest extends ApiRequest {
    @Override
    public String getPath() {
        return "user/playlist";
    }

    public GetUserPlaylistsRequest(int uid, int limit, int offset) {
        this.uid = uid;
        this.limit = limit;
        this.offset = offset;
    }

    public GetUserPlaylistsRequest(int uid) {
        this.uid = uid;
        this.limit = 30;
        this.offset = 0;
    }

    private int uid;
    @JSONField(name = "uid")
    public int getUserId() {
        return uid;
    }

    private int limit;
    @JSONField(name = "limit")
    public int getLimit() {
        return limit;
    }

    private int offset;
    @JSONField(name = "offset")
    public int getOffset() {
        return offset;
    }
}
