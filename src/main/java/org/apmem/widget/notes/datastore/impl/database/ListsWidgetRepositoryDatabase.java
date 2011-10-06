package org.apmem.widget.notes.datastore.impl.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private DataSourceOpenHelper helper;

    public ListsWidgetRepositoryDatabase(Context context) {
        this.helper = new DataSourceOpenHelper(context);
    }

    @Override
    public List<ListWidgetElement> list(long listId) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, new String[]{"id", "listId", "widgetId"}, "listId = ?", new String[]{Long.toString(listId)}, null, null, "id asc");

        return this.getDataFromCursor(cursor);
    }

    @Override
    public long add(int widgetId, long listId) {
        ContentValues values = new ContentValues();
        values.put("widgetId", widgetId);
        values.put("listId", listId);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        return writableDatabase.insert(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, null, values);
    }

    @Override
    public void remove(int widgetId) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.delete(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, "widgetId=?", new String[]{Long.toString(widgetId)});
    }

    @Override
    public void update(int widgetId, long listId) {
        ContentValues values = new ContentValues();
        values.put("widgetId", widgetId);
        values.put("listId", listId);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.update(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, values, "widgetId=?", new String[]{Long.toString(widgetId)});

    }

    @Override
    public ListWidgetElement get(int widgetId) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_WIDGET_TABLE_NAME, new String[]{"id", "listId", "widgetId"}, "Id=?", new String[]{Long.toString(widgetId)}, null, null, null);
        List<ListWidgetElement> result = this.getDataFromCursor(cursor);
        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    private List<ListWidgetElement> getDataFromCursor(Cursor cursor) {
        List<ListWidgetElement> result = new ArrayList<ListWidgetElement>();
        if (cursor.moveToFirst()) {
            do {
                ListWidgetElement element = new ListWidgetElement();
                element.setId(cursor.getLong(0));
                element.setListId(cursor.getLong(1));
                element.setWidgetId(cursor.getInt(2));
                result.add(element);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return result;
    }
}
