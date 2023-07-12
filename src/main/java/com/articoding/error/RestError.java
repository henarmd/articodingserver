package com.articoding.error;

import com.articoding.model.Role;

public class RestError extends RuntimeException  {

    private final String restMesssage;

    public RestError(String restMesssage) {
        this.restMesssage = restMesssage;
    }

    public String getRestMesssage() {
        return restMesssage;
    }
}
