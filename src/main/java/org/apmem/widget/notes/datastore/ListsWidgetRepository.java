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

    List<ListWidgetElement> list(int listId);

    int add(int widgetId, int listId);

    void remove(int widgetId);

    void removeList(int listId);

    void update(int widgetId, int listId, int page);

    ListWidgetElement get (int widgetId);
}
