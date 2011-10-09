package org.apmem.widget.notes;

import android.content.Context;
import org.apmem.widget.notes.datastore.ListsItemRepository;
import org.apmem.widget.notes.datastore.ListsRepository;
import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.impl.database.ListsItemRepositoryDatabase;
import org.apmem.widget.notes.datastore.impl.database.ListsRepositoryDatabase;
import org.apmem.widget.notes.datastore.impl.database.ListsWidgetRepositoryDatabase;
import org.apmem.widget.notes.datastore.impl.database.datasource.DataSourceOpenHelper;
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
    private static ListsRepository currentListsRepository;
    private static ListsItemRepository currentListsItemRepository;
    private static ListsWidgetRepository currentListsWidgetRepository;
    private static Refresher currentRefresher;
    private static DataSourceOpenHelper currentDataSourceOpenHelper;

    public static ListsRepository getListRepository(Context context) {
        if (currentListsRepository == null) {
            DataSourceOpenHelper helper = getCurrentDataSourceOpenHelper(context);
            currentListsRepository = new ListsRepositoryDatabase(helper);
        }
        return currentListsRepository;
    }

    public static ListsItemRepository getListsItemRepository(Context context) {
        if (currentListsItemRepository == null)                         {
            DataSourceOpenHelper helper = getCurrentDataSourceOpenHelper(context);
            currentListsItemRepository = new ListsItemRepositoryDatabase(helper);
        }
        return currentListsItemRepository;
    }

    public static ListsWidgetRepository getListsWidgetRepository(Context context) {
        if (currentListsWidgetRepository == null)  {
            DataSourceOpenHelper helper = getCurrentDataSourceOpenHelper(context);
            currentListsWidgetRepository = new ListsWidgetRepositoryDatabase(helper);
        }
        return currentListsWidgetRepository;
    }

    public static Refresher getCurrentRefresher(Context context) {
        if (currentRefresher == null) {
            ListsWidgetRepository listsWidgetRepository = getListsWidgetRepository(context);
            currentRefresher = new RefresherFromActivity(listsWidgetRepository);
        }
        return currentRefresher;
    }

    public static DataSourceOpenHelper getCurrentDataSourceOpenHelper(Context context) {
        if (currentDataSourceOpenHelper == null) {
            currentDataSourceOpenHelper = new DataSourceOpenHelper(context);
        }
        return currentDataSourceOpenHelper;
    }
}
