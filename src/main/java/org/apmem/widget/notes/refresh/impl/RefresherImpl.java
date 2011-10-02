package org.apmem.widget.notes.refresh.impl;

import android.appwidget.AppWidgetManager;
import android.content.ContextWrapper;
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
public class RefresherImpl implements Refresher {
    private ContextWrapper contextWrapper;
    private ListsWidgetRepository listsWidgetRepository;

    public RefresherImpl(ContextWrapper contextWrapper, ListsWidgetRepository listsWidgetRepository) {
        this.contextWrapper = contextWrapper;
        this.listsWidgetRepository = listsWidgetRepository;
    }

    @Override
    public void updateWidget(int appWidgetId) {
        Intent intent = new Intent(Constants.ACTION_WIDGET_UPDATE_FROM_ACTIVITY);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        contextWrapper.sendBroadcast(intent);
    }

    @Override
    public void updateList(long listId) {
        List<ListWidgetElement> listWidgetElement = this.listsWidgetRepository.list(listId);
        for (ListWidgetElement widgetElement : listWidgetElement) {
            this.updateWidget(widgetElement.getWidgetId());
        }
    }
}
