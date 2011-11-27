package org.apmem.widget.notes.datastore;

import org.apmem.widget.notes.datastore.model.ListItemElement;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 23:30
 * To change this template use File | Settings | File Templates.
 */
public interface ListsItemRepository {

    List<ListItemElement> list(int listId, int offset, int count);

    int add(String name, int listId);

    void remove(int itemId);

    void removeList(int listId);

    void update(int itemId, String name, boolean done);

    ListItemElement get (int itemId);
}
