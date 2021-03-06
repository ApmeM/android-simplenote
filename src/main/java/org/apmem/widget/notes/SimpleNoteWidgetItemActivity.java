package org.apmem.widget.notes;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import org.apmem.widget.notes.datastore.ListsItemRepository;
import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.model.ListItemElement;
import org.apmem.widget.notes.datastore.model.ListWidgetElement;
import org.apmem.widget.notes.refresh.Refresher;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 02.10.11
 * Time: 0:24
 * To change this template use File | Settings | File Templates.
 */
public class SimpleNoteWidgetItemActivity extends Activity {
    private static final String TAG = "SimpleNoteWidgetItemActivity";

    private ListsWidgetRepository listsWidgetRepository = DependencyResolver.getListsWidgetRepository(this);
    private ListsItemRepository listsItemRepository = DependencyResolver.getListsItemRepository(this);
    private Refresher refresher = DependencyResolver.getCurrentRefresher(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        EditText editText = (EditText) this.findViewById(R.id.activity_item_text);
        Button deleteButton = (Button) this.findViewById(R.id.activity_item_button_delete);
        Button nextButton = (Button) this.findViewById(R.id.activity_item_button_next);

        int itemId = this.getIntent().getIntExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, -1);
        ListItemElement item = this.listsItemRepository.get(itemId);
        if (item != null) {
            editText.setText(item.getName());
            deleteButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.GONE);
        } else {
            editText.setText("");
            deleteButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void onCommit(View button) {
        Log.i(TAG, "onCommit");

        EditText editText = (EditText) this.findViewById(R.id.activity_item_text);
        String name = editText.getText().toString();

        saveItem(name);
        this.finish();
    }


    public void onNext(View button) {
        Log.i(TAG, "onNext");

        EditText editText = (EditText) this.findViewById(R.id.activity_item_text);
        String name = editText.getText().toString();

        saveItem(name);
        editText.setText("");
    }

    private void saveItem(String name) {
        if (!name.trim().equals("")) {
            int itemId = this.getIntent().getIntExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, -1);
            ListItemElement item = this.listsItemRepository.get(itemId);
            int appWidgetId = this.getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
            ListWidgetElement widget = this.listsWidgetRepository.get(appWidgetId);

            if (itemId == -1l) {
                this.listsItemRepository.add(name, widget.getListId());
            } else {
                this.listsItemRepository.update(item.getId(), name, item.isDone());
            }

            this.refresher.updateList(this, widget.getListId());
        }
    }

    public void onCancel(View button) {
        Log.i(TAG, "onCancel");
        this.finish();
    }


    public void onDelete(View button) {
        Log.i(TAG, "onDelete");

        int itemId = this.getIntent().getIntExtra(Constants.INTENT_EXTRA_WIDGET_ITEM_ID, -1);
        int appWidgetId = this.getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        ListWidgetElement widget = this.listsWidgetRepository.get(appWidgetId);

        this.listsItemRepository.remove(itemId);

        this.refresher.updateList(this, widget.getListId());

        this.finish();
    }
}
