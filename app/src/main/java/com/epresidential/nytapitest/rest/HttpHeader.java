package com.epresidential.nytapitest.rest;

import com.epresidential.nytapitest.model.JsonObject;

/**
 * Created by daniele on 22/08/16.
 */
public class HttpHeader extends JsonObject {

    private String key;
    private String value;

    public HttpHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}