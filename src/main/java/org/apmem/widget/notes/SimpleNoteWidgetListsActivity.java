package org.apmem.widget.notes;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import org.apmem.widget.notes.adapters.ListsAdapter;
import org.apmem.widget.notes.datastore.ListsItemRepository;
import org.apmem.widget.notes.datastore.ListsRepository;
import org.apmem.widget.notes.datastore.ListsWidgetRepository;
import org.apmem.widget.notes.datastore.model.ListElement;
import org.apmem.widget.notes.datastore.model.ListWidgetElement;
import org.apmem.widget.notes.refresh.Refresher;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 20:33
 * To change this template use File | Settings | File Templates.
 */
public class SimpleNoteWidgetListsActivity extends Activity {
    private static final String TAG = "SimpleNoteWidgetListsActivity";

    private ListsRepository listsRepository = DependencyResolver.getListRepository(this);
    private ListsWidgetRepository listsWidgetRepository = DependencyResolver.getListsWidgetRepository(this);
    private ListsItemRepository listsItemRepository = DependencyResolver.getListsItemRepository(this);
    private Refresher refresher = DependencyResolver.getCurrentRefresher(this);
    private ListsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ListView listView = (ListView) this.findViewById(R.id.activity_lists_list);

        int appWidgetId = this.getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        ListWidgetElement element = this.listsWidgetRepository.get(appWidgetId);

        this.adapter = new ListsAdapter(this.listsRepository, layoutInflater);
        if (element != null) {
            this.adapter.setSelectedListId(element.getListId());
        }
        this.adapter.setOnRemoveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(view);
            }
        });
        this.adapter.setOnEditClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit(view);
            }
        });
        this.adapter.setOnEditClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit(view);
            }
        });
        this.adapter.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel(view);
            }
        });
        this.adapter.setOnCommitClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit(view);
            }
        });
        this.adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(view);
            }
        });

        listView.setAdapter(this.adapter);
    }

    private void select(View text) {
        Log.i(TAG, "select");
        ListElement element = this.findElement(text);
        int appWidgetId = this.getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);

        ListWidgetElement widgetElement = listsWidgetRepository.get(appWidgetId);
        if (widgetElement != null) {
            listsWidgetRepository.update(appWidgetId, element.getId(), 0);
        } else {
            listsWidgetRepository.add(appWidgetId, element.getId());
        }

        this.refresher.updateWidget(this, appWidgetId);

        this.finish();
    }


    private void commit(View button) {
        Log.i(TAG, "commit");
        ListElement element = this.findElement(button);
        RelativeLayout parent = (RelativeLayout) button.getParent();
        EditText text = (EditText) parent.findViewById(R.id.activity_lists_row_edit_text);
        if (text.getText().toString().equals("")) {
            remove(button);
        } else {
            this.listsRepository.update(element.getId(), text.getText().toString(), false);
            this.adapter.notifyDataSetChanged();

            this.refresher.updateList(this, element.getId());
        }
    }

    private void cancel(View button) {
        Log.i(TAG, "cancel");
        ListElement element = this.findElement(button);
        if (element.getName().equals("")) {
            Log.i(TAG, "cancel old item name empty");
            remove(button);
        } else {
            Log.i(TAG, "cancel old item name " + element.getName());
            this.listsRepository.update(element.getId(), element.getName(), false);
            this.adapter.notifyDataSetChanged();
        }
    }

    private void edit(View button) {
        Log.i(TAG, "edit");
        ListElement element = this.findElement(button);
        this.listsRepository.update(element.getId(), element.getName(), true);
        this.adapter.notifyDataSetChanged();
    }

    private void remove(View button) {
        Log.i(TAG, "remove");
        ListElement element = this.findElement(button);
        List<ListWidgetElement> listWidgets = this.listsWidgetRepository.list(element.getId());

        this.listsRepository.remove(element.getId());
        this.listsItemRepository.removeList(element.getId());
        this.listsWidgetRepository.removeList(element.getId());

        for (ListWidgetElement widget : listWidgets) {
            this.refresher.updateWidget(this, widget.getWidgetId());
        }

        this.adapter.notifyDataSetChanged();
    }

    public void addList(View button) {
        Log.i(TAG, "addList");
        int listId = this.listsRepository.add("", true);
        this.adapter.notifyDataSetChanged();
    }

    private ListElement findElement(View control) {
        Log.i(TAG, "findElement");
        RelativeLayout parent = (RelativeLayout) control.getParent();
        return (ListElement) parent.getTag();
    }
}
