package com.bear.neteasemusic.api;

public abstract class ApiResponse {
    public abstract boolean isOk();
    public abstract String errorReason();
}
