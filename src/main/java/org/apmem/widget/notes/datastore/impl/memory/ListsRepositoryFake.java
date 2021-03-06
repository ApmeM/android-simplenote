package org.apmem.widget.notes.datastore.impl.memory;

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
    private List<ListElement> listElements = new ArrayList<ListElement>();

    @Override
    public List<ListElement> list() {
        return this.listElements;
    }

    @Override
    public int add(String name, boolean edited) {
        ListElement listElement = new ListElement();
        listElement.setName(name);
        listElement.setEdited(edited);
        listElement.setId(this.listElements.size());
        this.listElements.add(listElement);
        return listElement.getId();
    }

    @Override
    public void remove(int id) {
        ListElement element = this.get(id);
        if (element != null) {
            this.listElements.remove(element);
        }
    }

    @Override
    public void update(int id, String name, boolean edited) {
        ListElement element = this.get(id);
        if (element != null) {
            element.setName(name);
            element.setEdited(edited);
        }
    }

    @Override
    public ListElement get(int listId) {
        for (ListElement element : this.listElements) {
            if (element.getId() == listId) {
                return element;
            }
        }
        return null;
    }
}
