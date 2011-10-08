package org.apmem.widget.notes.datastore.repositoryFactory;

import org.apmem.widget.notes.datastore.ListsItemRepository;
import org.apmem.widget.notes.datastore.ListsRepository;
import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.RepositoryFactory;
import org.apmem.widget.notes.datastore.impl.ListsItemRepositoryFake;
import org.apmem.widget.notes.datastore.impl.ListsRepositoryFake;
import org.apmem.widget.notes.datastore.impl.ListsWidgetRepositoryFake;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 08.10.11
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class RepositoryFactoryFake implements RepositoryFactory{
    private ListsRepository listsRepository = new ListsRepositoryFake();
    private ListsItemRepository listsItemRepository = new ListsItemRepositoryFake();
    private ListsWidgetRepository listsWidgetRepository = new ListsWidgetRepositoryFake();

    @Override
    public ListsRepository getListRepository() {
        return this.listsRepository;
    }

    @Override
    public ListsItemRepository getListsItemRepository() {
        return this.listsItemRepository;
    }

    @Override
    public ListsWidgetRepository getListsWidgetRepository() {
        return this.listsWidgetRepository;
    }
}
