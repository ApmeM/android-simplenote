package org.apmem.widget.notes.datastore.impl.memory;

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
    private List<ListWidgetElement> listElements = new ArrayList<ListWidgetElement>();

    @Override
    public List<ListWidgetElement> list(int listId) {
        List<ListWidgetElement> result = new ArrayList<ListWidgetElement>();
        for (ListWidgetElement element : this.listElements) {
            if (element.getListId() == listId) {
                result.add(element);
            }
        }
        return result;
    }

    @Override
    public int add(int widgetId, int listId) {
        ListWidgetElement listElement = new ListWidgetElement();
        listElement.setListId(listId);
        listElement.setWidgetId(widgetId);
        listElement.setId(this.listElements.size());
        listElement.setPage(0);
        this.listElements.add(listElement);
        return listElement.getId();
    }

    @Override
    public void remove(int widgetId) {
        ListWidgetElement element = this.get(widgetId);
        if (element != null) {
            this.listElements.remove(element);
        }
    }

    @Override
    public void removeList(int listId) {
        for (int i = this.listElements.size() - 1; i >= 0; i--) {
            ListWidgetElement element = this.listElements.get(i);
            if (element.getListId() == listId) {
                this.listElements.remove(i);
            }
        }
    }

    @Override
    public void update(int widgetId, int listId, int page) {
        ListWidgetElement element = this.get(widgetId);
        if (element != null) {
            element.setListId(listId);
            element.setPage(page);
        }
    }

    @Override
    public ListWidgetElement get(int widgetId) {
        for (ListWidgetElement element : this.listElements) {
            if (element.getWidgetId() == widgetId) {
                return element;
            }
        }
        return null;
    }
}
