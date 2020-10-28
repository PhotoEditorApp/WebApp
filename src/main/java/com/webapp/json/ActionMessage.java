package com.webapp.json;

public class ActionMessage {
    private final String status;

    public ActionMessage(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
