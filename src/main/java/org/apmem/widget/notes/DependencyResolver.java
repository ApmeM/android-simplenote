package org.apmem.widget.notes;

import org.apmem.widget.notes.datastore.ListsItemRepository;
import org.apmem.widget.notes.datastore.ListsRepository;
import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.impl.ListsItemRepositoryFake;
import org.apmem.widget.notes.datastore.impl.ListsRepositoryFake;
import org.apmem.widget.notes.datastore.impl.ListsWidgetRepositoryFake;
import org.apmem.widget.notes.refresh.Refresher;
import org.apmem.widget.notes.refresh.impl.RefresherFromActivity;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 08.10.11
 * Time: 23:37
 * To change this template use File | Settings | File Templates.
 */
public class DependencyResolver {
    private static ListsRepository currentListsRepository = new ListsRepositoryFake();
    private static ListsItemRepository currentListsItemRepository = new ListsItemRepositoryFake();
    private static ListsWidgetRepository currentListsWidgetRepository = new ListsWidgetRepositoryFake();
    private static Refresher currentRefresher = new RefresherFromActivity(currentListsWidgetRepository);

    public static ListsRepository getListRepository() {
        if (currentListsRepository == null)
            currentListsRepository = new ListsRepositoryFake();
        return currentListsRepository;
    }

    public static ListsItemRepository getListsItemRepository() {
        if (currentListsItemRepository == null)
            currentListsItemRepository = new ListsItemRepositoryFake();
        return currentListsItemRepository;
    }

    public static ListsWidgetRepository getListsWidgetRepository() {
        if (currentListsWidgetRepository == null)
            currentListsWidgetRepository = new ListsWidgetRepositoryFake();
        return currentListsWidgetRepository;
    }

    public static Refresher getCurrentRefresher() {
        if (currentRefresher == null) {
            ListsWidgetRepository listsWidgetRepository = getListsWidgetRepository();
            currentRefresher = new RefresherFromActivity(listsWidgetRepository);
        }
        return currentRefresher;
    }
}
