package org.apmem.widget.notes.datastore.impl.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private DataSourceOpenHelper helper;

    public ListsRepositoryDatabase(Context context) {
        this.helper = new DataSourceOpenHelper(context);
    }

    @Override
    public List<ListElement> list() {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_TABLE_NAME, new String[]{"id", "name", "edited"}, null, null, null, null, "id asc");

        return this.getDataFromCursor(cursor);
    }

    @Override
    public long add(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("edited", false);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        return writableDatabase.insert(DataSourceOpenHelper.LIST_TABLE_NAME, null, values);
    }

    @Override
    public void remove(long listId) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.delete(DataSourceOpenHelper.LIST_TABLE_NAME, "Id=?", new String[]{Long.toString(listId)});
    }

    @Override
    public void update(long listId, String name, boolean edited) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("edited", edited);

        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.update(DataSourceOpenHelper.LIST_TABLE_NAME, values, "Id=?", new String[]{Long.toString(listId)});
    }

    @Override
    public ListElement get(long listId) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(DataSourceOpenHelper.LIST_TABLE_NAME, new String[]{"id", "name", "edited"}, "Id=?", new String[]{Long.toString(listId)}, null, null, null);
        List<ListElement> result = this.getDataFromCursor(cursor);
        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    private List<ListElement> getDataFromCursor(Cursor cursor) {
        List<ListElement> result = new ArrayList<ListElement>();
        if (cursor.moveToFirst()) {
            do {
                ListElement element = new ListElement();
                element.setId(cursor.getLong(0));
                element.setName(cursor.getString(1));
                element.setEdited(cursor.getInt(2) != 0);
                result.add(element);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return result;
    }
}
