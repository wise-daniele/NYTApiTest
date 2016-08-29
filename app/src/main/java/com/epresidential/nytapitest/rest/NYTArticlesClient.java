package com.epresidential.nytapitest.rest;

import android.content.Context;
import android.util.Log;

import com.epresidential.nytapitest.R;

/**
 * Created by daniele on 22/08/16.
 */
public class NYTArticlesClient extends NYTRestClient {

    private static final String LOG_TAG = NYTArticlesClient.class.getSimpleName();
    private static NYTArticlesClient instance;

    public static synchronized NYTArticlesClient with(Context context) {
        if (instance == null) {
            if (context == null) {
                throw new IllegalStateException("context not defined for rest client");
            }
            instance = new NYTArticlesClient(context);
        }
        return instance;
    }

    public static NYTArticlesClient getInstance() {
        return instance;
    }

    protected NYTArticlesClient(Context context) {
        super(context);
    }



    /*
    public synchronized void getMovieReviews(String query, HttpResponseListener listener){
        Log.d(LOG_TAG, "Get Reviews");
        String queryParam = "query=" + query;
        String apiKeyParam = "api-key=" + getContext().getString(R.string.nyt_movies_api_key);
        String params = buildParams(queryParam, apiKeyParam);
        final String url = buildUrl("/svc/movies/v2/reviews/search.json?" + params);
        get(url, listener, true);

    }
    */

    public synchronized void getArticles(int page, HttpResponseListener listener){
        String pageParam = "page=" + page;
        String sortParam = "sort=newest";
        String apiKeyParam = "api-key=" + getContext().getString(R.string.nyt_articles_api_key);
        String params = buildParams(pageParam, sortParam, apiKeyParam);
        final String url = buildUrl("/search/v2/articlesearch.json" + params);
        get(url, listener, true);

    }
}
