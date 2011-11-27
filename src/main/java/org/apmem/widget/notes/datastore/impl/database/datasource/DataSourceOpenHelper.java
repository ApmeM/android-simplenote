package org.apmem.widget.notes.datastore.impl.database.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.apmem.widget.notes.datastore.impl.database.datasource.DatabaseUpdaters.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 04.10.11
 * Time: 20:43
 * To change this template use File | Settings | File Templates.
 */
public class DataSourceOpenHelper {
    private final static String TAG = "DataSourceOpenHelper";
    private final static String DATABASE_NAME = "SIMPLE_NOTES_DATABASE";

    public final static String LIST_ITEMS_TABLE_NAME = "ListItems";
    public final static String LIST_WIDGET_TABLE_NAME = "ListWidgets";
    public final static String LIST_TABLE_NAME = "Lists";

    private SQLiteOpenHelper helper;
    private SQLiteDatabase writableDatabase;

    public DataSourceOpenHelper(Context context) {
        final List<DatabaseUpdater> updaters = new ArrayList<DatabaseUpdater>();
        updaters.add(new Update_1());
        updaters.add(new Update_2());
        final int databaseVersion = this.getVersion(updaters);

        Log.i(TAG, "Constructor " + DATABASE_NAME + " " + databaseVersion);

        this.helper = new SQLiteOpenHelper(context, DATABASE_NAME, null, databaseVersion) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                Log.i(TAG, "onCreate");
                this.onUpgrade(sqLiteDatabase, 0, databaseVersion);
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int prev, int now) {
                Log.i(TAG, "onUpgrade");
                for (DatabaseUpdater updater : updaters) {
                    if (updater.getVersion() > prev && updater.getVersion() <= now) {
                        Log.i(TAG, "apply " + updater.getClass() + " update.");
                        updater.update(sqLiteDatabase);
                    } else {
                        Log.i(TAG, "skip " + updater.getClass() + " update.");
                    }
                }
            }
        };
    }

    private int getVersion(List<DatabaseUpdater> updaters) {
        int databaseVersion = 0;
        for (DatabaseUpdater updater : updaters) {
            if (databaseVersion < updater.getVersion()){
                databaseVersion = updater.getVersion();
            }
        }
        return databaseVersion;
    }

    public SQLiteDatabase getWritableDatabase() {
        Log.i(TAG, "getWritableDatabase");
        if (writableDatabase == null) {
            writableDatabase = helper.getWritableDatabase();
        }
        return writableDatabase;
    }
}