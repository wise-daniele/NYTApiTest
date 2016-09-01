package com.epresidential.nytapitest.ui;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.epresidential.nytapitest.R;
import com.epresidential.nytapitest.data.NYTContract;
import com.epresidential.nytapitest.model.Article;
import com.epresidential.nytapitest.model.Response;
import com.epresidential.nytapitest.rest.HttpResponseException;
import com.epresidential.nytapitest.rest.JsonHttpResponseListener;
import com.epresidential.nytapitest.rest.NYTArticlesClient;
import com.epresidential.nytapitest.ui.adapter.ArticlesAdapter;
import com.epresidential.nytapitest.utils.StoreUtils;

import java.util.Vector;

/**
 * Created by daniele on 23/08/16.
 */
public class ArticlesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = ArticlesFragment.class.getSimpleName();
    public static final String FRAGMENT_TAG = "fragment_articles";

    private ArticleListener mListener;
    private ArticlesAdapter mArticlesAdapter;
    private ListView mArticlesListView;
    private int mCurrentListPosition;

    private static final int ARTICLES_LOADER = 0;

    private static final String[] ARTICLES_COLUMNS = {
            NYTContract.ArticleEntry.TABLE_NAME + "." + NYTContract.ArticleEntry._ID,
            NYTContract.ArticleEntry.COLUMN_PUB_DATE,
            NYTContract.ArticleEntry.COLUMN_WEB_URL,
            NYTContract.ArticleEntry.COLUMN_IMAGE_URL,
            NYTContract.ArticleEntry.COLUMN_MAIN_HEADLINE
    };

    // These indices are tied to ARTICLES_COLUMNS projection.  If ARTICLES_COLUMNS changes, these must change.
    public static final int COL_ARTICLE_ID = 0;
    public static final int COL_PUB_DATE = 1;
    public static final int COL_WEB_URL = 2;
    public static final int COL_IMAGE_URL = 3;
    public static final int COL_MAIN_HEADLINE = 4;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirstLoading = StoreUtils.getFirstLoading(getContext());
        if(isFirstLoading){
            updateArticles(0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_articles, container, false);
        mArticlesListView = (ListView) rootView.findViewById(R.id.articles_listview);
        mArticlesAdapter = new ArticlesAdapter(getActivity(), null, 0);
        mArticlesListView.setAdapter(mArticlesAdapter);

        mArticlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem()
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                //Calls the listener method to show the action bar notification
                mListener.onItemSelected(cursor, position);
            }
        });

        //Provide scroll listener in order to understand whether the bottom of the list has been reached
        //If so, then a new request for the next page will be send to NYT API
        mArticlesListView.setOnScrollListener(new OnBottomListReachedListener());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //initLoader calls onCreateLoader which performs the query in background
        getLoaderManager().initLoader(ARTICLES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void updateArticles(final int page) {

        if(StoreUtils.getLastLoadedPage(getContext()) >= page){
            return;
        }

        NYTArticlesClient.with(getActivity().getApplicationContext()).getArticles(page, new JsonHttpResponseListener<Response>(Response.class) {
            @Override
            public void onFailure(HttpResponseException ex) {
                Log.e(LOG_TAG, ex.getMessage(), ex);
            }

            @Override
            public void onSuccess(Response result) {
                if(StoreUtils.getFirstLoading(getContext())){
                    StoreUtils.setFirstLoading(getContext(), false);
                }
                StoreUtils.setLastLoadedPage(getContext(), page);
                Article[] articles = result.getResponse().getDocs();

                Vector<ContentValues> cVVector = new Vector<ContentValues>(10);
                for(int i = 0; i<articles.length; i++){
                    //for each received article create the ContentValues object containing the relevant info
                    Article article = articles[i];
                    ContentValues articleValues = new ContentValues();
                    if(article.getMultimedia().length > 0){
                        articleValues.put(NYTContract.ArticleEntry.COLUMN_IMAGE_URL, article.getMultimedia()[0].getUrl());
                    }
                    articleValues.put(NYTContract.ArticleEntry.COLUMN_WEB_URL, article.getWeb_url());
                    articleValues.put(NYTContract.ArticleEntry.COLUMN_MAIN_HEADLINE, article.getHeadline().getMain());
                    articleValues.put(NYTContract.ArticleEntry.COLUMN_PUB_DATE, article.getPub_date());
                    cVVector.add(articleValues);
                }
                int inserted = 0;
                // add articles to database with just one operation
                if ( cVVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = getContext().getContentResolver().bulkInsert(NYTContract.ArticleEntry.CONTENT_URI, cvArray);
                }
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = NYTContract.ArticleEntry.COLUMN_PUB_DATE + " DESC";
        Uri articleUri = NYTContract.ArticleEntry.CONTENT_URI;

        //get all articles from db sorted starting from the newest
        return new CursorLoader(getActivity(),
                articleUri,
                ARTICLES_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mArticlesAdapter.swapCursor(data);
        //Scroll to the last position when returning to this Activity after having opened the notification
        if (mCurrentListPosition != ListView.INVALID_POSITION) {
            mArticlesListView.smoothScrollToPosition(mCurrentListPosition);
            mCurrentListPosition = ListView.INVALID_POSITION;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //we release any resources that we might be using
        mArticlesAdapter.swapCursor(null);
    }

    /**
     * Listener for defining actions to be performed when scrolling list
     */
    private class OnBottomListReachedListener implements AbsListView.OnScrollListener {

        private int mCurrentVisibleItemCount;
        private int mCurrentFirstVisibleItem;
        private int mCurrentTotalItemCount;
        private int mCurrentScrollState;

        @Override
        public synchronized void onScroll(AbsListView view, int firstVisibleItem,
                                          int visibleItemCount, int totalItemCount) {
            // managing update of list
            this.mCurrentVisibleItemCount = visibleItemCount;
            this.mCurrentFirstVisibleItem = firstVisibleItem;
            this.mCurrentTotalItemCount = totalItemCount;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // managing loading of additional items
            this.mCurrentScrollState = scrollState;
            if (isScrollCompleted()) {
                int lastPage = StoreUtils.getLastLoadedPage(getContext());
                lastPage ++;
                updateArticles(lastPage);
            }
        }

        private boolean isScrollCompleted() {
            return this.mCurrentScrollState == SCROLL_STATE_IDLE &&
                    this.mCurrentVisibleItemCount > 0 &&
                    this.mCurrentFirstVisibleItem + this.mCurrentVisibleItemCount >= this.mCurrentTotalItemCount;
        }

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (ArticleListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ArticleListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setCurrentListPosition(int position){
        mCurrentListPosition = position;
    }

    public interface ArticleListener {
        /**
         * FragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Cursor article, int position);
    }

}
