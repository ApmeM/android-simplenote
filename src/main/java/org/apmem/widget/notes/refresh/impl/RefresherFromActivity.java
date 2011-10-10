package org.apmem.widget.notes.refresh.impl;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import org.apmem.widget.notes.Constants;
import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.model.ListWidgetElement;
import org.apmem.widget.notes.refresh.Refresher;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 02.10.11
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class RefresherFromActivity implements Refresher {
    private ListsWidgetRepository listsWidgetRepository;

    public RefresherFromActivity(ListsWidgetRepository listsWidgetRepository) {
        this.listsWidgetRepository = listsWidgetRepository;
    }

    @Override
    public void updateWidget(Context context, int appWidgetId) {
        Intent intent = new Intent(Constants.ACTION_WIDGET_UPDATE_FROM_ACTIVITY);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        context.sendBroadcast(intent);
    }

    @Override
    public void updateList(Context context, int listId) {
        List<ListWidgetElement> listWidgetElement = listsWidgetRepository.list(listId);
        for (ListWidgetElement widgetElement : listWidgetElement) {
            this.updateWidget(context, widgetElement.getWidgetId());
        }
    }
}
