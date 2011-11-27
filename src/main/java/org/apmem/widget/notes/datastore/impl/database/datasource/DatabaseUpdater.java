package org.apmem.widget.notes.datastore.impl.database.datasource;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 27.11.11
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
public interface DatabaseUpdater {
    void update(SQLiteDatabase sqLiteDatabase);

    int getVersion();
}
