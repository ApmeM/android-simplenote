package org.apmem.widget.notes.datastore.impl.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.impl.database.datasource.DataSourceOpenHelper;
import org.apmem.widget.notes.datastore.model.ListWidgetElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
public class ListsWidgetRepositoryDatabase implements ListsWidgetRepository {
    private final static String TAG = "ListsWidgetRepositoryDatabase";
    private DataSourceOpenHelper helper;

    public ListsWidgetRepositoryDatabase(DataSourceOpenHelper helper) {
        Log.i(TAG, "constructor");
        this.helper = helper;
    }

    @Override
    public List<ListWidgetElement> list(int listId) {
        Log.i(TAG, "list " + listId);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, new String[]{"elementId", "listId", "widgetId", "page"}, "listId = ?", new String[]{Integer.toString(listId)}, null, null, "elementId asc");

        return this.getDataFromCursor(cursor);
    }

    @Override
    public int add(int widgetId, int listId) {
        Log.i(TAG, "add '" + widgetId + "' to list " + listId);
        ContentValues values = new ContentValues();
        values.put("widgetId", widgetId);
        values.put("listId", listId);
        values.put("page", 0);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        return (int) writableDatabase.insert(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, null, values);
    }

    @Override
    public void remove(int widgetId) {
        Log.i(TAG, "delete with elementId " + widgetId);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.delete(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, "widgetId=?", new String[]{Integer.toString(widgetId)});
    }

    @Override
    public void removeList(int listId) {
        Log.i(TAG, "delete widgets with listId " + listId);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.delete(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, "listId=?", new String[]{Integer.toString(listId)});
    }

    @Override
    public void update(int widgetId, int listId, int page) {
        Log.i(TAG, "update with elementId " + widgetId + " to " + listId);
        ContentValues values = new ContentValues();
        values.put("widgetId", widgetId);
        values.put("listId", listId);
        values.put("page", page);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.update(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, values, "widgetId=?", new String[]{Integer.toString(widgetId)});
    }

    @Override
    public ListWidgetElement get(int widgetId) {
        Log.i(TAG, "get with elementId " + widgetId);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, new String[]{"elementId", "listId", "widgetId", "page"}, "widgetId=?", new String[]{Integer.toString(widgetId)}, null, null, null);
        List<ListWidgetElement> result = this.getDataFromCursor(cursor);
        if (result.size() == 1) {
            ListWidgetElement listWidgetElement = result.get(0);
            Log.i(TAG, "get item found " + listWidgetElement.getWidgetId() + "  for list " + listWidgetElement.getListId());
            return listWidgetElement;
        } else if (result.size() > 1) {
            Log.i(TAG, "get item too many found, remove all");
            writableDatabase.delete(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, "widgetId=?", new String[]{Integer.toString(widgetId)});
            return null;
        } else {
            Log.i(TAG, "get item not found");
            return null;
        }
    }

    private List<ListWidgetElement> getDataFromCursor(Cursor cursor) {
        Log.i(TAG, "getDataFromCursor");
        List<ListWidgetElement> result = new ArrayList<ListWidgetElement>();
        if (cursor.moveToFirst()) {
            do {
                ListWidgetElement element = new ListWidgetElement();
                element.setId(cursor.getInt(0));
                element.setListId(cursor.getInt(1));
                element.setWidgetId(cursor.getInt(2));
                element.setPage(cursor.getInt(3));
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
