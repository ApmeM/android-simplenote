package org.apmem.widget.notes;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
public class SimpleNoteWidgetItemDeleteActivity extends Activity {
    private static final String TAG = "SimpleNoteWidgetItemActivity";

    private ListsRepository listsRepository = new ListsRepositoryFake();
    private ListsWidgetRepository listsWidgetRepository = new ListsWidgetRepositoryFake();
    private ListsItemRepository listsItemRepository = new ListsItemRepositoryFake();
    private ListsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_delete);

        TextView textView = (TextView) this.findViewById(R.id.activity_item_delete_item_name);

        long itemId = this.getIntent().getLongExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, -1);
        ListItemElement item = this.listsItemRepository.get(itemId);
        if (item != null) {
            textView.setText(item.getName());
        }
    }

    public void onCommit(View button) {
        Log.i(TAG, "onCommit");

        long itemId = this.getIntent().getLongExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, -1);
        int appWidgetId = this.getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        ListWidgetElement widget = this.listsWidgetRepository.get(appWidgetId);

        this.listsItemRepository.remove(itemId);

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
