package org.apmem.widget.notes.datastore.repositoryFactory;

import android.content.Context;
import org.apmem.widget.notes.datastore.ListsItemRepository;
import org.apmem.widget.notes.datastore.ListsRepository;
import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.RepositoryFactory;
import org.apmem.widget.notes.datastore.impl.database.ListsItemRepositoryDatabase;
import org.apmem.widget.notes.datastore.impl.database.ListsRepositoryDatabase;
import org.apmem.widget.notes.datastore.impl.database.ListsWidgetRepositoryDatabase;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 08.10.11
 * Time: 23:35
 * To change this template use File | Settings | File Templates.
 */
public class RepositoryFactoryDatabase implements RepositoryFactory {

    private Context context;

    public RepositoryFactoryDatabase(Context context) {
        this.context = context;
    }

    private ListsRepository listsRepository = new ListsRepositoryDatabase(this.context);
    private ListsItemRepository listsItemRepository = new ListsItemRepositoryDatabase(this.context);
    private ListsWidgetRepository listsWidgetRepository = new ListsWidgetRepositoryDatabase(this.context);

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
