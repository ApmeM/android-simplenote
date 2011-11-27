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
import org.apmem.widget.notes.providers.WidgetProviderHelper;
import org.apmem.widget.notes.refresh.Refresher;

import java.util.List;

public abstract class SimpleNoteWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "SimpleNoteWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "onUpdate called for " + appWidgetIds[0]);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            this.setEventActivity(context, SimpleNoteWidgetListsActivity.class, remoteViews, appWidgetId, -1, R.id.widget_layout_logo);
            this.setEventActivity(context, SimpleNoteWidgetListsActivity.class, remoteViews, appWidgetId, -1, R.id.widget_layout_select_list_help);
            this.setEventActivity(context, SimpleNoteWidgetListsActivity.class, remoteViews, appWidgetId, -1, R.id.widget_layout_title);
            this.setEventActivity(context, SimpleNoteWidgetItemActivity.class, remoteViews, appWidgetId, -1, R.id.widget_layout_button_add);

            this.setEventBroadcast(context, remoteViews, Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_NEXT, appWidgetId, 0, R.id.widget_layout_button_next);
            this.setEventBroadcast(context, remoteViews, Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_PREV, appWidgetId, 0, R.id.widget_layout_button_prev);

            this.redrawWidget(context, remoteViews, appWidgetId);

            this.updateWidget(context, remoteViews, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "onReceive called with " + action);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Bundle extras = intent.getExtras();

        if (action.equals(Constants.ACTION_WIDGET_UPDATE_FROM_ACTIVITY)) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            this.redrawWidget(context, remoteViews, appWidgetId);
            this.updateWidget(context, remoteViews, appWidgetId);
        } else if (action.equals(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_NEXT)) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            ListsWidgetRepository widgetRepository = DependencyResolver.getListsWidgetRepository(context);
            ListWidgetElement widget = widgetRepository.get(appWidgetId);
            widgetRepository.update(widget.getWidgetId(), widget.getListId(), widget.getPage() + 1);
            this.redrawWidget(context, remoteViews, appWidgetId);
            this.updateWidget(context, remoteViews, appWidgetId);
        } else if (action.equals(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_PREV)) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            ListsWidgetRepository widgetRepository = DependencyResolver.getListsWidgetRepository(context);
            ListWidgetElement widget = widgetRepository.get(appWidgetId);
            widgetRepository.update(widget.getWidgetId(), widget.getListId(), widget.getPage() - 1);
            this.redrawWidget(context, remoteViews, appWidgetId);
            this.updateWidget(context, remoteViews, appWidgetId);
        } else if (action.equals(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_READY_ITEM)) {
            ListsItemRepository listsItemRepository = DependencyResolver.getListsItemRepository(context);
            Refresher refresher = DependencyResolver.getCurrentRefresher(context);

            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            int itemId = extras.getInt(Constants.INTENT_EXTRA_WIDGET_ITEM_ID);
            ListItemElement item = listsItemRepository.get(itemId);
            if (item != null) {
                listsItemRepository.update(item.getId(), item.getName(), !item.isDone());
                refresher.updateList(context, item.getListId());
            }
            this.updateWidget(context, remoteViews, appWidgetId);
        } else if (action.equals(AppWidgetManager.ACTION_APPWIDGET_DELETED)) {
            ListsWidgetRepository listsWidgetRepository = DependencyResolver.getListsWidgetRepository(context);

            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            listsWidgetRepository.remove(appWidgetId);
        }
        super.onReceive(context, intent);
    }

    public abstract int getPageSize();

    private void redrawWidget(Context context, RemoteViews remoteViews, int appWidgetId) {
        ListsRepository listsRepository = DependencyResolver.getListRepository(context);
        ListsItemRepository listsItemRepository = DependencyResolver.getListsItemRepository(context);
        ListsWidgetRepository listsWidgetRepository = DependencyResolver.getListsWidgetRepository(context);

        Resources resources = context.getResources();
        ListWidgetElement widgetElement = listsWidgetRepository.get(appWidgetId);
        remoteViews.removeAllViews(R.id.widget_layout_list);

        if (widgetElement != null) {
            ListElement element = listsRepository.get(widgetElement.getListId());
            List<ListItemElement> listItems = listsItemRepository.list(element.getId(), widgetElement.getPage() * this.getPageSize(), this.getPageSize());
            for (ListItemElement item : listItems) {
                this.addItem(context, appWidgetId, remoteViews, item);
            }

            remoteViews.setTextViewText(R.id.widget_layout_title, element.getName());
            remoteViews.setViewVisibility(R.id.widget_layout_select_list_help, View.GONE);
            remoteViews.setViewVisibility(R.id.widget_layout_button_add, View.VISIBLE);
            if (listItems.size() == 0) {
                remoteViews.setViewVisibility(R.id.widget_layout_add_item_help, View.VISIBLE);
            } else {
                remoteViews.setViewVisibility(R.id.widget_layout_add_item_help, View.GONE);
            }

            if (widgetElement.getPage() > 0) {
                remoteViews.setViewVisibility(R.id.widget_layout_button_prev, View.VISIBLE);
            } else {
                remoteViews.setViewVisibility(R.id.widget_layout_button_prev, View.GONE);
            }

            if (listItems.size() == this.getPageSize()) {
                remoteViews.setViewVisibility(R.id.widget_layout_button_next, View.VISIBLE);
            } else {
                remoteViews.setViewVisibility(R.id.widget_layout_button_next, View.GONE);
            }

        } else {
            remoteViews.setTextViewText(R.id.widget_layout_title, resources.getString(R.string.widget_layout_title));
            remoteViews.setViewVisibility(R.id.widget_layout_select_list_help, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widget_layout_add_item_help, View.GONE);
            remoteViews.setViewVisibility(R.id.widget_layout_button_add, View.GONE);
            remoteViews.setViewVisibility(R.id.widget_layout_button_prev, View.GONE);
            remoteViews.setViewVisibility(R.id.widget_layout_button_next, View.GONE);
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
        this.setEventBroadcast(context, newView, Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET_READY_ITEM, appWidgetId, itemId, R.id.widget_layout_row_text);
    }

    private void updateWidget(Context context, RemoteViews remoteViews, int appWidgetId) {
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

    private void setEventBroadcast(Context context, RemoteViews remoteViews, String action, int appWidgetId, int itemId, int senderId) {
        List<Class> allWidgets = WidgetProviderHelper.getAllProviders();
        for (Class widget : allWidgets) {
            Intent newIntent = new Intent(context, widget);
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
}
