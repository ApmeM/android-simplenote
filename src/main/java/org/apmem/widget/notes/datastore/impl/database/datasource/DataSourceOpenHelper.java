package org.apmem.widget.notes.datastore.impl.database.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 04.10.11
 * Time: 20:43
 * To change this template use File | Settings | File Templates.
 */
public class DataSourceOpenHelper extends SQLiteOpenHelper {
    private final static String TAG = "DataSourceOpenHelper";
    private final static String DATABASE_NAME = "SIMPLE_NOTES_DATABASE";
    private final static int DATABASE_VERSION = 8;

    public final static String LIST_ITEMS_TABLE_NAME = "ListItems";
    public final static String LIST_WIDGET_TABLE_NAME = "ListWidgets";
    public final static String LIST_TABLE_NAME = "Lists";

    private static SQLiteDatabase writableDatabase;

    public DataSourceOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "Constructor " + DATABASE_NAME + " " + DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "onCreate");
        sqLiteDatabase.execSQL("CREATE TABLE " + LIST_ITEMS_TABLE_NAME + "(elementId INTEGER PRIMARY KEY, listId INTEGER, name TEXT, done BIT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + LIST_WIDGET_TABLE_NAME + "(elementId INTEGER PRIMARY KEY, listId INTEGER, name TEXT, widgetId INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE " + LIST_TABLE_NAME + "(elementId INTEGER PRIMARY KEY,name TEXT, edited BIT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade");
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        Log.i(TAG, "getWritableDatabase");
        if(writableDatabase == null){
            writableDatabase = super.getWritableDatabase();
        }
        return writableDatabase;
    }
}