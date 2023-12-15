package com.makarov.laba3_v14.responses.success;

import java.io.Serializable;

public class Response implements Serializable {
    protected String message;

    public Response (String message) {
        this.message = message;
    }

    public Response () {}

    public String getMessage () {
        return message;
    }
}
