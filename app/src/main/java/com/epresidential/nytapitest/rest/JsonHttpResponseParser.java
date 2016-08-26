package com.epresidential.nytapitest.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by daniele on 22/08/16.
 */
public class JsonHttpResponseParser<T> {

    private Class<T> mClass;

    public JsonHttpResponseParser(Class<T> clazz) {
        mClass = clazz;
    }

    public T parse(String json) throws JsonSyntaxException {
        return getGson().fromJson(json, mClass);
    }

    private Gson getGson() {
        return new Gson();
    }

}