package org.apmem.widget.notes.datastore;

import org.apmem.widget.notes.datastore.model.ListElement;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 20:34
 * To change this template use File | Settings | File Templates.
 */
public interface ListsRepository {

    List<ListElement> list();

    long add(String name);

    void remove(long listId);

    void update(long listId, String name, boolean edited);

    ListElement get(long listId);
}
