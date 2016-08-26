package com.epresidential.nytapitest.model;

import com.epresidential.nytapitest.rest.JsonObject;

/**
 * Created by daniele on 25/08/16.
 */
public class Multimedia extends JsonObject {

    String url;
    String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
