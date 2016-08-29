package com.epresidential.nytapitest.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.epresidential.nytapitest.R;
import com.epresidential.nytapitest.model.Article;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ArticlesFragment.ArticleListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String ITEM_POSITION_EXTRA = "item_position";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get last position of listview in order to automatically scroll the listview
        int position = getIntent().getIntExtra(ITEM_POSITION_EXTRA, ListView.INVALID_POSITION);
        ArticlesFragment articlesFragment = new ArticlesFragment();
        articlesFragment.setCurrentListPosition(position);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment_container, articlesFragment, ArticlesFragment.FRAGMENT_TAG)
                .disallowAddToBackStack()
                .commit();
    }

    @Override
    public void onItemSelected(Cursor cursor, int position) {
        String imageUrl = cursor.getString(ArticlesFragment.COL_IMAGE_URL);
        String pubDate = cursor.getString(ArticlesFragment.COL_PUB_DATE);
        String webUrl = cursor.getString(ArticlesFragment.COL_WEB_URL);
        String headline = cursor.getString(ArticlesFragment.COL_MAIN_HEADLINE);

        Intent intent = new Intent(this, NotificationResultActivity.class);
        intent.putExtra(NotificationResultActivity.WEB_URL_EXTRA, webUrl);
        //Provide back navigation to parent activity
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this).
                addParentStack(NotificationResultActivity.class).
                addNextIntent(intent);
        //allows to move the list the the current position (i.e item selected)
        stackBuilder.editIntentAt(0).putExtra(ITEM_POSITION_EXTRA, position);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        //Create Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(pubDate);
        builder.setContentText(headline);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Message"));
        builder.setContentIntent(resultPendingIntent);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // First parameter is the ID that allows you to update the notification later on.
        mNotificationManager.notify(1, builder.build());
    }
}
