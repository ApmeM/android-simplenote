package org.apmem.widget.notes.datastore.impl.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.apmem.widget.notes.datastore.ListsRepository;
import org.apmem.widget.notes.datastore.impl.database.datasource.DataSourceOpenHelper;
import org.apmem.widget.notes.datastore.model.ListElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 20:35
 * To change this template use File | Settings | File Templates.
 */
public class ListsRepositoryDatabase implements ListsRepository {
    private final static String TAG = "ListsRepositoryDatabase";
    private DataSourceOpenHelper helper;

    public ListsRepositoryDatabase(DataSourceOpenHelper helper) {
        Log.i(TAG, "constructor");
        this.helper = helper;
    }

    @Override
    public List<ListElement> list() {
        Log.i(TAG, "list");
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_TABLE_NAME, new String[]{"elementId", "name", "edited"}, null, null, null, null, "elementId asc");

        return this.getDataFromCursor(cursor);
    }

    @Override
    public int add(String name, boolean edited) {
        Log.i(TAG, "add " + name + " edited " + edited);
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("edited", edited);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        int elementId = (int) writableDatabase.insert(DataSourceOpenHelper.LIST_TABLE_NAME, null, values);
        Log.i(TAG, "add with elementId " + elementId);
        return elementId;
    }

    @Override
    public void remove(int listId) {
        Log.i(TAG, "delete with elementId " + listId);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.delete(DataSourceOpenHelper.LIST_TABLE_NAME, "elementId=?", new String[]{Long.toString(listId)});
    }

    @Override
    public void update(int listId, String name, boolean edited) {
        Log.i(TAG, "update with elementId " + listId + " to " + name + " edited " + edited);
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("edited", edited);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.update(DataSourceOpenHelper.LIST_TABLE_NAME, values, "elementId=?", new String[]{Long.toString(listId)});
    }

    @Override
    public ListElement get(int listId) {
        Log.i(TAG, "get with elementId " + listId);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_TABLE_NAME, new String[]{"elementId", "name", "edited"}, "elementId=?", new String[]{Long.toString(listId)}, null, null, null);
        List<ListElement> result = this.getDataFromCursor(cursor);
        if (result.size() == 1) {
            ListElement listElement = result.get(0);
            Log.i(TAG, "get item found " + listElement.getName() + " edited " + listElement.isEdited());
            return listElement;
        } else {
            Log.i(TAG, "get item not found");
            return null;
        }
    }

    private List<ListElement> getDataFromCursor(Cursor cursor) {
        Log.i(TAG, "getDataFromCursor");
        List<ListElement> result = new ArrayList<ListElement>();
        if (cursor.moveToFirst()) {
            do {
                ListElement element = new ListElement();
                element.setId(cursor.getInt(0));
                element.setName(cursor.getString(1));
                element.setEdited(cursor.getInt(2) != 0);
                result.add(element);
            } while (cursor.moveToNext());
            Log.i(TAG, "getDataFromCursor " + result.size() + " items found");
        } else {
            Log.i(TAG, "getDataFromCursor no items found");
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return result;
    }
}
