package com.webapp.json;

public class TokenMessage {
    private final long id;
    private final String token;

    public TokenMessage(long id, String token) {
        this.id = id;
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
