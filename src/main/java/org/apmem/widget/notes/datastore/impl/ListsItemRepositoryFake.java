package org.apmem.widget.notes.datastore.impl;

import org.apmem.widget.notes.datastore.ListsItemRepository;
import org.apmem.widget.notes.datastore.model.ListItemElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 23:39
 * To change this template use File | Settings | File Templates.
 */
public class ListsItemRepositoryFake implements ListsItemRepository {
   private static List<ListItemElement> listElements = new ArrayList<ListItemElement>();


    @Override
    public List<ListItemElement> list() {
        return listElements;
    }

    @Override
    public List<ListItemElement> list(long listId) {
        List<ListItemElement> result = new ArrayList<ListItemElement>();
        for (ListItemElement element : listElements) {
            if (element.getListId() == listId) {
                result.add(element);
            }
        }
        return result;
    }

    @Override
    public long add(String name, long listId) {
        ListItemElement listElement = new ListItemElement();
        listElement.setListId(listId);
        listElement.setName(name);
        listElement.setId(listElements.size());
        listElements.add(listElement);
        return listElement.getId();
    }

    @Override
    public void remove(long itemId) {
        ListItemElement element = this.get(itemId);
        if (element != null) {
            listElements.remove(element);
        }
    }

    @Override
    public void update(long itemId, String name) {
        ListItemElement element = this.get(itemId);
        if (element != null) {
            element.setName(name);
        }
    }

    @Override
    public ListItemElement get(long itemId) {
        for (ListItemElement element : listElements) {
            if (element.getId() == itemId) {
                return element;
            }
        }
        return null;
    }}
