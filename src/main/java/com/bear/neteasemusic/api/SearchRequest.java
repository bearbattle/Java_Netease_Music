package com.bear.neteasemusic.api;

import com.alibaba.fastjson.annotation.JSONField;

public class SearchRequest extends ApiRequest {

    @Override
    public String getPath() {
        return "search/get";
    }

    @Override
    public NeteaseAPI.EncryptionType getEncryptionMethod() {
        return NeteaseAPI.EncryptionType.WEAPI;
    }

    public SearchRequest(String keyword) {
        this.keyword = keyword;
    }

    private String keyword;

    @JSONField(name = "s")
    public String getKeyword() {
        return keyword;
    }

    @JSONField(name = "type")
    public int getType() {
        return 1018;
    }

    @JSONField(name = "offset")
    public int getOffset() {
        return 0;
    }

    @JSONField(name = "limit")
    public int getLimit() {
        return 30;
    }
}
