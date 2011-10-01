package org.apmem.widget.notes.datastore.model;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class ListWidgetElement {
    private long id;
    private long listId;
    private int WidgetId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public int getWidgetId() {
        return WidgetId;
    }

    public void setWidgetId(int widgetId) {
        WidgetId = widgetId;
    }
}
