package com.bear.neteasemusic.api;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchSuggestResponse extends ApiResponse {
    private SearchSuggestResponse() {}
    public static SearchSuggestResponse parse(JSONObject response) {
        var resp = new SearchSuggestResponse();
        if (response.getIntValue("code") != 200) {
            resp.ok = false;
            resp.reason = "未知错误。";
        } else {
            resp.ok = true;
            try {
                var listObject = response.getJSONObject("result").getJSONArray("allMatch");
                resp.suggests = listObject.stream().map((Object x) -> {
                    return ((JSONObject) x).getString("keyword");
                }).collect(Collectors.toList());
            } catch (Exception ex) {
                ex.printStackTrace();
                resp.suggests = new ArrayList<String>();
            }

        }
        return resp;
    }

    private List<String> suggests;

    public List<String> getSuggests() {
        return suggests;
    }
}
