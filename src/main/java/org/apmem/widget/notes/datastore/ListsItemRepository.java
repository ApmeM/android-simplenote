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

    List<ListItemElement> list(long listId);

    long add(String name, long listId);

    void remove(long itemId);

    void update(long itemId, String name, boolean done);

    ListItemElement get (long itemId);
}
