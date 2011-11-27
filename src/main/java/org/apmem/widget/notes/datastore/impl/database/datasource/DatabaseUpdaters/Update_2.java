package org.apmem.widget.notes.datastore.impl.database.datasource.DatabaseUpdaters;

import android.database.sqlite.SQLiteDatabase;
import org.apmem.widget.notes.datastore.impl.database.datasource.DataSourceOpenHelper;
import org.apmem.widget.notes.datastore.impl.database.datasource.DatabaseUpdater;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 27.11.11
 * Time: 18:19
 * To change this template use File | Settings | File Templates.
 */
public class Update_2 implements DatabaseUpdater{
    @Override
    public void update(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("ALTER TABLE " + DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME + " ADD COLUMN page INTEGER");
    }

    @Override
    public int getVersion() {
        return 2;
    }
}
