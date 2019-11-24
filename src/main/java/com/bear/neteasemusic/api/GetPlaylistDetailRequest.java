package com.bear.neteasemusic.api;

import com.alibaba.fastjson.annotation.JSONField;

public class GetPlaylistDetailRequest extends ApiRequest {
    @Override
    public String getPath() {
        return "v3/playlist/detail";
    }

    public GetPlaylistDetailRequest(long id) {
        this.id = id;
        this.limit = 1000;
        this.offset = 0;
    }

    private long id;
    @JSONField(name = "id")
    public long getId() {
        return id;
    }

    @JSONField(name = "total")
    public boolean getTotal() {
        return true;
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

    @JSONField(name = "n")
    public int getN() {
        return 1000;
    }
}
