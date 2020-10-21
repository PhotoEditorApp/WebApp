package com.webApp.testPart.json;

public class ActivateMessage {
    private final String status;

    public ActivateMessage(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
