package com.bear.neteasemusic.api;

public abstract class ApiResponse {
    protected boolean ok = false;
    public boolean isOk() {
        return ok;
    }

    protected String reason = null;
    public String errorReason() {
        return reason;
    }
}
