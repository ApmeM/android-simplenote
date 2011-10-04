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
import org.apmem.widget.notes.datastore.impl.ListsItemRepositoryFake;
import org.apmem.widget.notes.datastore.impl.ListsRepositoryFake;
import org.apmem.widget.notes.datastore.impl.ListsWidgetRepositoryFake;
import org.apmem.widget.notes.datastore.model.ListElement;
import org.apmem.widget.notes.datastore.model.ListItemElement;
import org.apmem.widget.notes.datastore.model.ListWidgetElement;
import org.apmem.widget.notes.refresh.Refresher;
import org.apmem.widget.notes.refresh.impl.RefresherFromActivity;

import java.util.List;

public class SimpleNoteWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "SimpleNoteWidgetProvider";
    private ListsRepository listRepository = new ListsRepositoryFake();
    private ListsItemRepository listsItemRepository = new ListsItemRepositoryFake();
    private ListsWidgetRepository listsWidgetRepository = new ListsWidgetRepositoryFake();
    private Refresher refresher = new RefresherFromActivity(listsWidgetRepository);

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "onUpdate called for " + appWidgetIds[0]);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_2_2);

            this.setOnSelectListIconClick(context, appWidgetId, remoteViews);

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
            Resources resources = context.getResources();
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            ListWidgetElement widgetElement = this.listsWidgetRepository.get(appWidgetId);
            remoteViews.removeAllViews(R.id.widget_layout_list);

            if (widgetElement != null) {
                ListElement element = this.listRepository.get(widgetElement.getListId());
                List<ListItemElement> listItems = this.listsItemRepository.list(element.getId());
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
        } else if (action.equals(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_DELETE_ITEM)) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            long itemId = extras.getLong(Constants.INTENT_EXTRA_WIDGET_ITEM_ID);
            ListItemElement item = this.listsItemRepository.get(itemId);
            if (item != null) {
                this.listsItemRepository.remove(itemId);
                this.refresher.updateList(context, item.getListId());
            }
            this.updateWidget(context, appWidgetId, remoteViews);
        } else if (action.equals(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_READY_ITEM)) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            long itemId = extras.getLong(Constants.INTENT_EXTRA_WIDGET_ITEM_ID);
            ListItemElement item = this.listsItemRepository.get(itemId);
            if (item != null) {
                this.listsItemRepository.update(item.getId(), item.getName(), !item.isDone());
                this.refresher.updateList(context, item.getListId());
            }
            this.updateWidget(context, appWidgetId, remoteViews);
        } else if (action.equals(AppWidgetManager.ACTION_APPWIDGET_DELETED)) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            this.listsWidgetRepository.remove(appWidgetId);
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

        RemoteViews newView = new RemoteViews(remoteViews.getPackage(), R.layout.widget_layout_row);
        newView.setTextViewText(R.id.widget_layout_row_text, text);
        remoteViews.addView(R.id.widget_layout_list, newView);
        this.setOnEditItemClick(context, item.getId(), appWidgetId, newView);
        this.setOnDeleteItemClick(context, item.getId(), appWidgetId, newView);
        this.setOnReadyItemClick(context, item.getId(), appWidgetId, newView);
    }

    private void updateWidget(Context context, int appWidgetId, RemoteViews remoteViews) {
//        ComponentName thisWidget = new ComponentName(context, SimpleNoteWidgetProvider.class);
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
    }

    private void setOnEditItemClick(Context context, long itemId, int appWidgetId, RemoteViews remoteViews) {
        Intent newIntent = new Intent(context, SimpleNoteWidgetItemAddActivity.class);
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, itemId);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout_row_button_edit, pendingIntent);
    }

    private void setOnDeleteItemClick(Context context, long itemId, int appWidgetId, RemoteViews remoteViews) {
        // Check settings:
        boolean showDialog = true; // ToDo: some settings.

        Intent newIntent;
        if (showDialog) {
            newIntent = new Intent(context, SimpleNoteWidgetItemDeleteActivity.class);
        } else {
            newIntent = new Intent(context, SimpleNoteWidgetProvider.class);
            newIntent.setAction(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_DELETE_ITEM);
        }
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, itemId);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent;
        if (showDialog) {
            pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        remoteViews.setOnClickPendingIntent(R.id.widget_layout_row_button_remove, pendingIntent);
    }

    private void setOnReadyItemClick(Context context, long itemId, int appWidgetId, RemoteViews remoteViews) {
        // Check settings:
        Intent newIntent;
        newIntent = new Intent(context, SimpleNoteWidgetProvider.class);
        newIntent.setAction(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_READY_ITEM);
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, itemId);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.widget_layout_row_text, pendingIntent);
    }

    private void setOnAddItemClick(Context context, int appWidgetId, RemoteViews remoteViews) {
        Intent newIntent = new Intent(context, SimpleNoteWidgetItemAddActivity.class);
        newIntent.putExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, -1l);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout_button_add, pendingIntent);
    }

    private void setOnSelectListIconClick(Context context, int appWidgetId, RemoteViews remoteViews) {
        Intent newIntent = new Intent(context, SimpleNoteWidgetListsActivity.class);
        newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        newIntent.setData(Uri.parse(newIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout_logo, pendingIntent);
    }
}
