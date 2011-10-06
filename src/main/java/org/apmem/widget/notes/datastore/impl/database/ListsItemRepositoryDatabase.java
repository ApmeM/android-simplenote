package org.apmem.widget.notes.datastore.impl.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private DataSourceOpenHelper helper;

    public ListsItemRepositoryDatabase(Context context) {
        this.helper = new DataSourceOpenHelper(context);
    }

    @Override
    public List<ListItemElement> list(long listId) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_ITEMS_TABLE_NAME, new String[]{"id", "listId", "name", "done"}, "listId = ?", new String[]{Long.toString(listId)}, null, null, "done asc, id asc");

        return this.getDataFromCursor(cursor);
    }

    @Override
    public long add(String name, long listId) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("listId", listId);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        return writableDatabase.insert(DataSourceOpenHelper.LIST_ITEMS_TABLE_NAME, null, values);
    }

    @Override
    public void remove(long itemId) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.delete(DataSourceOpenHelper.LIST_ITEMS_TABLE_NAME, "Id=?", new String[]{Long.toString(itemId)});
    }

    @Override
    public void update(long itemId, String name, boolean done) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("done", done);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.update(DataSourceOpenHelper.LIST_ITEMS_TABLE_NAME, values, "Id=?", new String[]{Long.toString(itemId)});
    }

    @Override
    public ListItemElement get(long itemId) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_ITEMS_TABLE_NAME, new String[]{"id", "listId", "name", "done"}, "Id=?", new String[]{Long.toString(itemId)}, null, null, null);
        List<ListItemElement> result = this.getDataFromCursor(cursor);
        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    private List<ListItemElement> getDataFromCursor(Cursor cursor) {
        List<ListItemElement> result = new ArrayList<ListItemElement>();
        if (cursor.moveToFirst()) {
            do {
                ListItemElement element = new ListItemElement();
                element.setId(cursor.getLong(0));
                element.setListId(cursor.getLong(1));
                element.setName(cursor.getString(2));
                element.setDone(cursor.getInt(3) != 0);
                result.add(element);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return result;
    }
}
