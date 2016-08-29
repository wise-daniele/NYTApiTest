package com.epresidential.nytapitest.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epresidential.nytapitest.R;
import com.epresidential.nytapitest.ui.ArticlesFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by daniele on 22/08/16.
 */
public class ArticlesAdapter extends CursorAdapter {

    public static final String LOG_TAG = ArticlesAdapter.class.getSimpleName();
    public static final String NYT_IMAGES_BASE_URL = "http://www.nytimes.com/";

    public static class ViewHolder {
        public final ImageView imageArticle;
        public final TextView textArticlePubdate;
        public final TextView textArticleHeadline;

        public ViewHolder(View view) {
            imageArticle = (ImageView) view.findViewById(R.id.image_article);
            textArticlePubdate = (TextView) view.findViewById(R.id.text_article_pubdate);
            textArticleHeadline = (TextView) view.findViewById(R.id.text_article_headline);
        }
    }

    public ArticlesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_article_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String imageUrl = cursor.getString(ArticlesFragment.COL_IMAGE_URL);
        String pubDate = cursor.getString(ArticlesFragment.COL_PUB_DATE);
        String headline = cursor.getString(ArticlesFragment.COL_MAIN_HEADLINE);
        viewHolder.textArticleHeadline.setText(headline);
        viewHolder.textArticlePubdate.setText(pubDate);
        if(imageUrl != null){
            Picasso.with(context).load(NYT_IMAGES_BASE_URL + imageUrl).into(viewHolder.imageArticle);
        }
        else{
            viewHolder.imageArticle.setImageResource(R.drawable.android_bkg);
        }
    }
}
