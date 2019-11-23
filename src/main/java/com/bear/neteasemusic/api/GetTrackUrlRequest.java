package com.bear.neteasemusic.api;

import com.alibaba.fastjson.annotation.JSONField;

public class GetTrackUrlRequest extends ApiRequest {
    enum Rate {
        mp3_320,
        mp3_192,
        mp3_128
    }

    @Override
    public String getPath() {
        return "song/enhance/player/url";
    }

    private int[] ids;

    @JSONField(name = "ids")
    public int[] getIds() {
        return ids;
    }

    private Rate rate;

    @JSONField(name = "br")
    public int getRate() {
        switch (rate) {
            case mp3_128:
                return 128000;
            case mp3_192:
                return 192000;
            case mp3_320:
                return 320000;
        }
        return 0;
    }

    public GetTrackUrlRequest(int id, Rate rate) {
        this.ids = new int[]{id};
        this.rate = rate;
    }
}