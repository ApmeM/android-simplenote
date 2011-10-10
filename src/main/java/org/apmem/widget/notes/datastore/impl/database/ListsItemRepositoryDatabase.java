package org.apmem.widget.notes.datastore.impl.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.apmem.widget.notes.datastore.ListsItemRepository;
import org.apmem.widget.notes.datastore.impl.database.datasource.DataSourceOpenHelper;
import org.apmem.widget.notes.datastore.model.ListItemElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 23:39
 * To change this template use File | Settings | File Templates.
 */
public class ListsItemRepositoryDatabase implements ListsItemRepository {
    private final static String TAG = "ListsItemRepositoryDatabase";
    private DataSourceOpenHelper helper;

    public ListsItemRepositoryDatabase(DataSourceOpenHelper helper) {
        Log.i(TAG, "constructor");
        this.helper = helper;
    }

    @Override
    public List<ListItemElement> list(int listId) {
        Log.i(TAG, "list " + listId);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_ITEMS_TABLE_NAME, new String[]{"elementId", "listId", "name", "done"}, "listId = ?", new String[]{Long.toString(listId)}, null, null, "done asc, elementId asc");

        return this.getDataFromCursor(cursor);
    }

    @Override
    public int add(String name, int listId) {
        Log.i(TAG, "add '" + name + "' to list " + listId);
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("listId", listId);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        int elementId = (int) writableDatabase.insert(DataSourceOpenHelper.LIST_ITEMS_TABLE_NAME, null, values);
        Log.i(TAG, "added with elementId " + elementId);
        return elementId;
    }

    @Override
    public void remove(int itemId) {
        Log.i(TAG, "delete with elementId " + itemId);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.delete(DataSourceOpenHelper.LIST_ITEMS_TABLE_NAME, "elementId=?", new String[]{Long.toString(itemId)});
    }

    @Override
    public void update(int itemId, String name, boolean done) {
        Log.i(TAG, "update with elementId " + itemId + " to " + name + " done " + done);
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("done", done);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.update(DataSourceOpenHelper.LIST_ITEMS_TABLE_NAME, values, "elementId=?", new String[]{Long.toString(itemId)});
    }

    @Override
    public ListItemElement get(int itemId) {
        Log.i(TAG, "get with elementId " + itemId);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_ITEMS_TABLE_NAME, new String[]{"elementId", "listId", "name", "done"}, "elementId=?", new String[]{Long.toString(itemId)}, null, null, null);
        List<ListItemElement> result = this.getDataFromCursor(cursor);
        if (result.size() == 1) {
            ListItemElement listItemElement = result.get(0);
            Log.i(TAG, "get item found " + listItemElement.getName() + " done " + listItemElement.isDone() + " for list " + listItemElement.getListId());
            return listItemElement;
        } else {
            Log.i(TAG, "get item not found");
            return null;
        }
    }

    private List<ListItemElement> getDataFromCursor(Cursor cursor) {
        Log.i(TAG, "getDataFromCursor");
        List<ListItemElement> result = new ArrayList<ListItemElement>();
        if (cursor.moveToFirst()) {
            do {
                ListItemElement element = new ListItemElement();
                element.setId(cursor.getInt(0));
                element.setListId(cursor.getInt(1));
                element.setName(cursor.getString(2));
                element.setDone(cursor.getInt(3) != 0);
                result.add(element);
            } while (cursor.moveToNext());
            Log.i(TAG, "getDataFromCursor " + result.size() + " items found");
        }else{
            Log.i(TAG, "getDataFromCursor no items found");
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return result;
    }
}
