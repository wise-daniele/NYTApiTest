package com.epresidential.nytapitest.rest;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by daniele on 22/08/16.
 */
public class NYTRestClient extends Client {

    private static final String DEFAULT_CONTENT = "";

    private String mBaseUrl;

    protected NYTRestClient(Context context) {
        super(context);
        mBaseUrl = "http://api.nytimes.com/svc";
    }

    protected String buildParams(String... params){
        String paramsStr = "?";
        for(int i = 0; i<params.length; i++){
            if(i < params.length - 1){
                paramsStr = paramsStr + params[i] + "&";
            }
            else{
                paramsStr = paramsStr + params[i];
            }
        }
        return paramsStr;
    }

    protected String buildUrl(String path) {
        return mBaseUrl + path;
    }

    protected void get(String url, HttpResponseListener listener, boolean async, HttpHeader... headers) {

        if (async) {
            Log.d(LOG_TAG, "Async call");
            getAsync(url, listener, headers);
        } else {
            HttpResponse response = getSync(url, headers);
            handleResponse(response, listener);
        }
    }

    private void handleResponse(HttpResponse response, HttpResponseListener listener) {
        if (listener != null) {
            if (response.isSuccess()) {
                listener.onSuccess(response.getJson());
            } else {
                listener.onFailure(response.getException());
            }
        }
    }
}
