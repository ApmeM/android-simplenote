package org.apmem.widget.notes.datastore;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 07.10.11
 * Time: 20:39
 * To change this template use File | Settings | File Templates.
 */
public interface RepositoryFactory {
    ListsRepository getListRepository();
    ListsItemRepository getListsItemRepository();
    ListsWidgetRepository getListsWidgetRepository();
}
