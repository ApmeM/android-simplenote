package org.apmem.widget.notes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Date;

public class SampleWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "SampleWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "onUpdate called for " + appWidgetIds[0]);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_2_2);

            this.setOnWidgetClick(context, appWidgetId, remoteViews);

            this.setOnActivityClick(context, appWidgetId, remoteViews);

            this.updateWidget(context, appWidgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "onReceive called with " + action);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_2_2);
        Bundle extras = intent.getExtras();

        if (action.equals(Constants.ACTION_WIDGET_UPDATE_FROM_ACTIVITY)) {
            String widgetText = extras.getString(Constants.INTENT_EXTRA_WIDGET_TEXT);
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            remoteViews.setTextViewText(R.id.word_text, widgetText);
            this.updateWidget(context, appWidgetId, remoteViews);
        } else if (action.equals(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET)) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            String widgetText = extras.getString(Constants.INTENT_EXTRA_WIDGET_TEXT);
            widgetText = widgetText + " at " + Constants.dateFormat.format(new Date());
            remoteViews.setTextViewText(R.id.word_text, widgetText);
            this.updateWidget(context, appWidgetId, remoteViews);
        }else{
            super.onReceive(context, intent);
        }
    }

    private void updateWidget(Context context, int appWidgetId, RemoteViews remoteViews) {
//        ComponentName thisWidget = new ComponentName(context, SampleWidgetProvider.class);
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
    }

    private void setOnActivityClick(Context context, int appWidgetId, RemoteViews remoteViews) {
        Intent newIntent = new Intent(context, SampleWidgetActivity.class);
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT, "Button clicked on Activity");
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
    }

    private Intent setOnWidgetClick(Context context, int appWidgetId, RemoteViews remoteViews) {
        Intent newIntent = new Intent(context, SampleWidgetProvider.class);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT, "Icon clicked on Widget");
        newIntent.setAction(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.icon, pendingIntent);
        return newIntent;
    }
}
