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
import org.apmem.widget.notes.datastore.ListsItemRepository;
import org.apmem.widget.notes.datastore.ListsRepository;
import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.impl.ListsItemRepositoryFake;
import org.apmem.widget.notes.datastore.impl.ListsRepositoryFake;
import org.apmem.widget.notes.datastore.impl.ListsWidgetRepositoryFake;
import org.apmem.widget.notes.datastore.model.ListElement;
import org.apmem.widget.notes.datastore.model.ListItemElement;
import org.apmem.widget.notes.datastore.model.ListWidgetElement;

import java.util.List;

public class SimpleNoteWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "SimpleNoteWidgetProvider";
    private ListsRepository listRepository = new ListsRepositoryFake();
    private ListsItemRepository listsItemRepository = new ListsItemRepositoryFake();
    private ListsWidgetRepository listsWidgetRepository = new ListsWidgetRepositoryFake();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "onUpdate called for " + appWidgetIds[0]);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_2_2);

            this.setOnWidgetClick(context, appWidgetId, remoteViews);

            this.setOnSelectListClick(context, appWidgetId, remoteViews);

            this.setOnAddItemClick(context, appWidgetId, remoteViews);

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
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            ListWidgetElement widgetElement = this.listsWidgetRepository.get(appWidgetId);
            if (widgetElement != null) {
                ListElement element = this.listRepository.get(widgetElement.getListId());
                remoteViews.setTextViewText(R.id.widget_layout_title, element.getName());
                List<ListItemElement> listItems = this.listsItemRepository.list(element.getId());

                remoteViews.removeAllViews(R.id.widget_layout_list);

                for (ListItemElement item : listItems) {
                    this.addItem(context, appWidgetId, remoteViews, item);
                }
            }
            this.updateWidget(context, appWidgetId, remoteViews);
//        } else if (action.equals(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET)) {
//            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
//            String widgetText = extras.getString(Constants.INTENT_EXTRA_WIDGET_TEXT);
//            widgetText = widgetText + " at " + Constants.dateFormat.format(new Date());
//            remoteViews.setTextViewText(R.id.widget_layout_body, widgetText);
//            this.updateWidget(context, appWidgetId, remoteViews);
        } else {
            super.onReceive(context, intent);
        }
    }

    private void addItem(Context context, int appWidgetId, RemoteViews remoteViews, ListItemElement item) {
        RemoteViews newView = new RemoteViews(remoteViews.getPackage(), R.layout.widget_layout_row);
        newView.setTextViewText(R.id.widget_layout_row_text, item.getName());
        remoteViews.addView(R.id.widget_layout_list, newView);
        this.setOnEditItemClick(context, item.getId(), appWidgetId, newView);
        this.setOnDeleteItemClick(context, item.getId(), appWidgetId, newView);
    }

    private void updateWidget(Context context, int appWidgetId, RemoteViews remoteViews) {
//        ComponentName thisWidget = new ComponentName(context, SimpleNoteWidgetProvider.class);
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
    }

    private void setOnEditItemClick(Context context, long itemId, int appWidgetId, RemoteViews remoteViews) {
        Intent newIntent = new Intent(context, SimpleNoteWidgetItemActivity.class);
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, itemId);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout_row_button_edit, pendingIntent);
    }

    private void setOnDeleteItemClick(Context context, long itemId, int appWidgetId, RemoteViews remoteViews) {
        Intent newIntent = new Intent(context, SimpleNoteWidgetItemDeleteActivity.class);
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, itemId);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout_row_button_delete, pendingIntent);
    }

    private void setOnAddItemClick(Context context, int appWidgetId, RemoteViews remoteViews) {
        Intent newIntent = new Intent(context, SimpleNoteWidgetItemActivity.class);
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, -1l);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout_button_add, pendingIntent);
    }

    private void setOnSelectListClick(Context context, int appWidgetId, RemoteViews remoteViews) {
        Intent newIntent = new Intent(context, SimpleNoteWidgetListsActivity.class);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
    }

    private Intent setOnWidgetClick(Context context, int appWidgetId, RemoteViews remoteViews) {
        Intent newIntent = new Intent(context, SimpleNoteWidgetProvider.class);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT, "Icon clicked on Widget");
        newIntent.setAction(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout_logo, pendingIntent);

        return newIntent;
    }
}
