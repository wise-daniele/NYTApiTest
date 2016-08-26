package com.epresidential.nytapitest.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by daniele on 23/08/16.
 */
public class NYTContract {

    public static final String CONTENT_AUTHORITY = "com.epresidential.nytapitest.app";
    // CONTENT AUTORITY for the access to the DB of this app
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Path to the table Article
    public static final String PATH_ARTICLE = "article";

    /* Inner class that defines the table contents of the location table */
    public static final class ArticleEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE;

        // Table name
        public static final String TABLE_NAME = "article";
        public static final String COLUMN_PUB_DATE = "article_pub_date";
        public static final String COLUMN_WEB_URL = "article_web_url";
        public static final String COLUMN_IMAGE_URL = "article_image_url";
        public static final String COLUMN_MAIN_HEADLINE = "article_main_headline";

        public static Uri buildArticleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
