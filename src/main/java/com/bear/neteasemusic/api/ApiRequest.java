package com.bear.neteasemusic.api;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class ApiRequest {
    @JSONField(serialize = false)
    public abstract String getPath();

    @JSONField(serialize = false)
    public abstract NeteaseAPI.EncryptionType getEncryptionMethod();
}
