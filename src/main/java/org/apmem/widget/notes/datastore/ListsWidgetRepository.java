package org.apmem.widget.notes.datastore;

import org.apmem.widget.notes.datastore.model.ListWidgetElement;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 22:25
 * To change this template use File | Settings | File Templates.
 */
public interface ListsWidgetRepository {

    List<ListWidgetElement> list();
    List<ListWidgetElement> list(long listId);

    long add(int widgetId, long listId);

    void remove(int widgetId);

    void update(int widgetId, long listId);

    ListWidgetElement get (int widgetId);
}
