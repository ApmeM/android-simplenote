package org.apmem.widget.notes.datastore.impl;

import org.apmem.widget.notes.datastore.ListsRepository;
import org.apmem.widget.notes.datastore.model.ListElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 20:35
 * To change this template use File | Settings | File Templates.
 */
public class ListsRepositoryFake implements ListsRepository {
    private static List<ListElement> listElements = new ArrayList<ListElement>();

    @Override
    public List<ListElement> list() {
        return listElements;
    }

    @Override
    public long add(String name) {
        ListElement listElement = new ListElement();
        listElement.setName(name);
        listElement.setEdited(true);
        listElement.setId(listElements.size());
        listElements.add(listElement);
        return listElement.getId();
    }

    @Override
    public void remove(long id) {
        ListElement element = this.get(id);
        if (element != null) {
            listElements.remove(element);
        }
    }

    @Override
    public void update(long id, String name) {
        ListElement element = this.get(id);
        if (element != null) {
            element.setName(name);
        }
    }

    @Override
    public ListElement get(long listId) {
        for (ListElement element : listElements) {
            if (element.getId() == listId) {
                return element;
            }
        }
        return null;
    }
}
