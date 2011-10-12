package org.apmem.widget.notes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import org.apmem.widget.notes.datastore.ListsItemRepository;
import org.apmem.widget.notes.datastore.ListsRepository;
import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.model.ListElement;
import org.apmem.widget.notes.datastore.model.ListItemElement;
import org.apmem.widget.notes.datastore.model.ListWidgetElement;
import org.apmem.widget.notes.refresh.Refresher;

import java.util.List;

public class SimpleNoteWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "SimpleNoteWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "onUpdate called for " + appWidgetIds[0]);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            this.setEventActivity(context, SimpleNoteWidgetListsActivity.class, remoteViews, appWidgetId, -1, R.id.widget_layout_logo);
            this.setEventActivity(context, SimpleNoteWidgetListsActivity.class, remoteViews, appWidgetId, -1, R.id.widget_layout_body);
            this.setEventActivity(context, SimpleNoteWidgetItemActivity.class, remoteViews, appWidgetId, -1, R.id.widget_layout_button_add);

            this.updateWidget(context, appWidgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "onReceive called with " + action);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Bundle extras = intent.getExtras();

        ListsRepository listsRepository = DependencyResolver.getListRepository(context);
        ListsItemRepository listsItemRepository = DependencyResolver.getListsItemRepository(context);
        ListsWidgetRepository listsWidgetRepository = DependencyResolver.getListsWidgetRepository(context);
        Refresher refresher = DependencyResolver.getCurrentRefresher(context);

        if (action.equals(Constants.ACTION_WIDGET_UPDATE_FROM_ACTIVITY)) {
            Resources resources = context.getResources();
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            ListWidgetElement widgetElement = listsWidgetRepository.get(appWidgetId);
            remoteViews.removeAllViews(R.id.widget_layout_list);

            if (widgetElement != null) {
                ListElement element = listsRepository.get(widgetElement.getListId());
                List<ListItemElement> listItems = listsItemRepository.list(element.getId());
                for (ListItemElement item : listItems) {
                    this.addItem(context, appWidgetId, remoteViews, item);
                }

                remoteViews.setTextViewText(R.id.widget_layout_title, element.getName());
                remoteViews.setTextViewText(R.id.widget_layout_body, resources.getString(R.string.widget_layout_body_no_items));
                if (listItems.size() == 0) {
                    remoteViews.setViewVisibility(R.id.widget_layout_body, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.widget_layout_button_add, View.VISIBLE);
                } else {
                    remoteViews.setViewVisibility(R.id.widget_layout_body, View.GONE);
                    remoteViews.setViewVisibility(R.id.widget_layout_button_add, View.VISIBLE);
                }
            } else {
                remoteViews.setTextViewText(R.id.widget_layout_title, resources.getString(R.string.widget_layout_title));
                remoteViews.setTextViewText(R.id.widget_layout_body, resources.getString(R.string.widget_layout_body));
                remoteViews.setViewVisibility(R.id.widget_layout_body, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.widget_layout_button_add, View.GONE);
            }
            this.updateWidget(context, appWidgetId, remoteViews);
        } else if (action.equals(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_READY_ITEM)) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            int itemId = extras.getInt(Constants.INTENT_EXTRA_WIDGET_ITEM_ID);
            ListItemElement item = listsItemRepository.get(itemId);
            if (item != null) {
                listsItemRepository.update(item.getId(), item.getName(), !item.isDone());
                refresher.updateList(context, item.getListId());
            }
            this.updateWidget(context, appWidgetId, remoteViews);
        } else if (action.equals(AppWidgetManager.ACTION_APPWIDGET_DELETED)) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            listsWidgetRepository.remove(appWidgetId);
            super.onReceive(context, intent);
        } else {
            super.onReceive(context, intent);
        }
    }

    private void addItem(Context context, int appWidgetId, RemoteViews remoteViews, ListItemElement item) {
        CharSequence text = item.getName();
        if (item.isDone()) {
            SpannableString stringUnderline = new SpannableString(text);
            stringUnderline.setSpan(new StrikethroughSpan(), 0, stringUnderline.length(), 0);
            text = stringUnderline;
        }

        int itemId = item.getId();
        RemoteViews newView = new RemoteViews(remoteViews.getPackage(), R.layout.widget_layout_row);
        newView.setTextViewText(R.id.widget_layout_row_text, text);
        remoteViews.addView(R.id.widget_layout_list, newView);
        this.setEventActivity(context, SimpleNoteWidgetItemActivity.class, newView, appWidgetId, itemId, R.id.widget_layout_row_button_edit);
        this.setEventBroadcast(context, SimpleNoteWidgetProvider.class, newView, Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_READY_ITEM, appWidgetId, itemId, R.id.widget_layout_row_text);
    }

    private void updateWidget(Context context, int appWidgetId, RemoteViews remoteViews) {
//        ComponentName thisWidget = new ComponentName(context, SimpleNoteWidgetProvider.class);
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
    }

    private void setEventActivity(Context context, Class receiverClass, RemoteViews remoteViews, int appWidgetId, int itemId, int senderId) {
        Intent newIntent = new Intent(context, receiverClass);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, itemId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntentLogo = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(senderId, pendingIntentLogo);
    }

    private void setEventBroadcast(Context context, Class receiverClass, RemoteViews remoteViews, String action, int appWidgetId, int itemId, int senderId) {
        Intent newIntent = new Intent(context, receiverClass);
        newIntent.setAction(action);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, itemId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntentLogo = PendingIntent.getBroadcast(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(senderId, pendingIntentLogo);
    }
}
