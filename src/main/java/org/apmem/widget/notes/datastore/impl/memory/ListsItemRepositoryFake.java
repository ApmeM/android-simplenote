package org.apmem.widget.notes.datastore.impl.memory;

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
    private List<ListItemElement> listElements = new ArrayList<ListItemElement>();

    @Override
    public List<ListItemElement> list(long listId) {
        List<ListItemElement> result = new ArrayList<ListItemElement>();
        for (ListItemElement element : this.listElements) {
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
        listElement.setId(this.listElements.size());
        this.listElements.add(listElement);
        return listElement.getId();
    }

    @Override
    public void remove(long itemId) {
        ListItemElement element = this.get(itemId);
        if (element != null) {
            this.listElements.remove(element);
        }
    }

    @Override
    public void update(long itemId, String name, boolean isDone) {
        ListItemElement element = this.get(itemId);
        if (element != null) {
            if (element.isDone() != isDone) {
                this.listElements.remove(element);
                if (isDone) {
                    this.listElements.add(element);
                } else {
                    this.listElements.add(0, element);
                }
            }
            element.setName(name);
            element.setDone(isDone);
        }
    }

    @Override
    public ListItemElement get(long itemId) {
        for (ListItemElement element : this.listElements) {
            if (element.getId() == itemId) {
                return element;
            }
        }
        return null;
    }
}
