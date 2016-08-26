package com.epresidential.nytapitest.rest;

import com.google.gson.Gson;

/**
 * Created by daniele on 22/08/16.
 */
public abstract class HttpResponseListener {

    protected Gson getGson() {
        return new Gson();
    }

    public void onPrepare() {
    }

    abstract public void onSuccess(String result);

    abstract public void onFailure(HttpResponseException ex);

}
