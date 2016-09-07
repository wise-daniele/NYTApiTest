package com.epresidential.nytapitest.model;

/**
 * Created by daniele on 25/08/16.
 */
public class Response extends JsonObject {

    private NYTDocs response;

    public NYTDocs getResponse() {
        return response;
    }

    public void setResponse(NYTDocs response) {
        this.response = response;
    }
}
