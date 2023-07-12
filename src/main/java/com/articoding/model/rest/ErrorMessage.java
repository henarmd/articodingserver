package com.articoding.model.rest;

public class ErrorMessage {

    private final String message;
    private final int code;

    public ErrorMessage(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
