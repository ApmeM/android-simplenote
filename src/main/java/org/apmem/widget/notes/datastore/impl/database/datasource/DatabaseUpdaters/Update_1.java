package org.apmem.widget.notes.datastore.impl.database.datasource.DatabaseUpdaters;

import android.database.sqlite.SQLiteDatabase;
import org.apmem.widget.notes.datastore.impl.database.datasource.DataSourceOpenHelper;
import org.apmem.widget.notes.datastore.impl.database.datasource.DatabaseUpdater;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 27.11.11
 * Time: 18:18
 * To change this template use File | Settings | File Templates.
 */
public class Update_1 implements DatabaseUpdater{
    @Override
    public void update(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + DataSourceOpenHelper.LIST_ITEMS_TABLE_NAME + "(elementId INTEGER PRIMARY KEY, listId INTEGER, name TEXT, done BIT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME + "(elementId INTEGER PRIMARY KEY, listId INTEGER, name TEXT, widgetId INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE " + DataSourceOpenHelper.LIST_TABLE_NAME + "(elementId INTEGER PRIMARY KEY,name TEXT, edited BIT)");
    }

    @Override
    public int getVersion() {
        return 1;
    }
}
