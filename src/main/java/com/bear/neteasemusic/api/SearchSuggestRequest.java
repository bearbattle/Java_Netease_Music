package com.bear.neteasemusic.api;

import com.alibaba.fastjson.annotation.JSONField;

public class SearchSuggestRequest extends ApiRequest {
    @Override
    public String getPath() {
        return "search/suggest/keyword";
    }

    @Override
    public NeteaseAPI.EncryptionType getEncryptionMethod() {
        return NeteaseAPI.EncryptionType.WEAPI;
    }

    public SearchSuggestRequest(String keyword) {
        this.keyword = keyword;
    }

    private String keyword;

    @JSONField(name = "s")
    public String getKeyword() {
        return keyword;
    }
}
