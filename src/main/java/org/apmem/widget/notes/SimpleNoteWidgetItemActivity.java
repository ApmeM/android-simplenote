package org.apmem.widget.notes;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import org.apmem.widget.notes.datastore.ListsItemRepository;
import org.apmem.widget.notes.datastore.ListsRepository;
import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.impl.ListsItemRepositoryFake;
import org.apmem.widget.notes.datastore.impl.ListsRepositoryFake;
import org.apmem.widget.notes.datastore.impl.ListsWidgetRepositoryFake;
import org.apmem.widget.notes.datastore.model.ListItemElement;
import org.apmem.widget.notes.datastore.model.ListWidgetElement;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 02.10.11
 * Time: 0:24
 * To change this template use File | Settings | File Templates.
 */
public class SimpleNoteWidgetItemActivity extends Activity {
    private static final String TAG = "SimpleNoteWidgetItemActivity";

    private ListsRepository listsRepository = new ListsRepositoryFake();
    private ListsWidgetRepository listsWidgetRepository = new ListsWidgetRepositoryFake();
    private ListsItemRepository listsItemRepository = new ListsItemRepositoryFake();
    private ListsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        EditText editText = (EditText) this.findViewById(R.id.activity_item_text);

        long itemId = this.getIntent().getLongExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, -1);
        ListItemElement item = this.listsItemRepository.get(itemId);
        if (item != null) {
            editText.setText(item.getName());
        } else {
            editText.setText("new item");
        }
    }

    public void onCommit(View button) {
        Log.i(TAG, "onCommit");

        EditText editText = (EditText) this.findViewById(R.id.activity_item_text);
        long itemId = this.getIntent().getLongExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, -1);
        String name = editText.getText().toString();
        int appWidgetId = this.getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        ListWidgetElement widget = this.listsWidgetRepository.get(appWidgetId);

        if(itemId == -1){
            this.listsItemRepository.add(name, widget.getListId());
        }else{
            this.listsItemRepository.update(itemId, name);
        }

        List<ListWidgetElement> listWidgetElement = this.listsWidgetRepository.list(widget.getListId());
        for (ListWidgetElement widgetElement : listWidgetElement) {
            this.updateWidget(widgetElement.getWidgetId());
        }
        this.finish();
    }

    public void onCancel(View button) {
        Log.i(TAG, "onCancel");
        this.finish();
    }

    private void updateWidget(int appWidgetId) {
        Intent intent = new Intent(Constants.ACTION_WIDGET_UPDATE_FROM_ACTIVITY);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        this.sendBroadcast(intent);
    }
}
