package org.apmem.widget.notes.datastore.impl;

import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.model.ListWidgetElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
public class ListsWidgetRepositoryFake implements ListsWidgetRepository {
    private static List<ListWidgetElement> listElements = new ArrayList<ListWidgetElement>();


    @Override
    public List<ListWidgetElement> list() {
        return listElements;
    }

    @Override
    public List<ListWidgetElement> list(long listId) {
        List<ListWidgetElement> result = new ArrayList<ListWidgetElement>();
        for (ListWidgetElement element : listElements) {
            if (element.getListId() == listId) {
                result.add(element);
            }
        }
        return result;
    }

    @Override
    public long add(int widgetId, long listId) {
        ListWidgetElement listElement = new ListWidgetElement();
        listElement.setListId(listId);
        listElement.setWidgetId(widgetId);
        listElement.setId(listElements.size());
        listElements.add(listElement);
        return listElement.getId();
    }

    @Override
    public void remove(int widgetId) {
        ListWidgetElement element = this.get(widgetId);
        if (element != null) {
            listElements.remove(element);
        }
    }

    @Override
    public void update(int widgetId, long listId) {
        ListWidgetElement element = this.get(widgetId);
        if (element != null) {
            element.setListId(listId);
        }
    }

    @Override
    public ListWidgetElement get(int widgetId) {
        for (ListWidgetElement element : listElements) {
            if (element.getWidgetId() == widgetId) {
                return element;
            }
        }
        return null;
    }
}