package org.apmem.widget.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.apmem.widget.notes.datastore.ListsRepository;
import org.apmem.widget.notes.datastore.model.ListElement;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 01.10.11
 * Time: 20:41
 * To change this template use File | Settings | File Templates.
 */
public class ListsAdapter extends BaseAdapter {
    private ListsRepository listRepository;
    private LayoutInflater layoutInflater;
    private long selectedListId;

    private View.OnClickListener onRemoveClickListener;
    private View.OnClickListener onEditClickListener;
    private View.OnClickListener onCommitClickListener;
    private View.OnClickListener onCancelClickListener;
    private View.OnClickListener onItemClickListener;

    public ListsAdapter(ListsRepository listRepository, LayoutInflater layoutInflater) {
        this.listRepository = listRepository;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return listRepository.list().size();
    }

    @Override
    public Object getItem(int i) {
        return listRepository.list().get(i);
    }

    @Override
    public long getItemId(int i) {
        return listRepository.list().get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListElement element = (ListElement) this.getItem(i);
        View v;
        if (element.isEdited()) {
            v = layoutInflater.inflate(R.layout.activity_lists_row_edit, null);
            EditText editText = (EditText) v.findViewById(R.id.activity_lists_row_edit_text);
            Button commit = (Button) v.findViewById(R.id.activity_lists_row_edit_button_commit);
            Button cancel = (Button) v.findViewById(R.id.activity_lists_row_edit_button_cancel);

            if (editText != null) {
                editText.setText(element.getName());
            }
            if (commit != null) {
                commit.setOnClickListener(onCommitClickListener);
            }
            if (cancel != null) {
                cancel.setOnClickListener(onCancelClickListener);
            }
        } else {
            v = layoutInflater.inflate(R.layout.activity_lists_row, null);
            TextView textView = (TextView) v.findViewById(R.id.activity_lists_row_text);
            Button remove = (Button) v.findViewById(R.id.activity_lists_row_button_remove);
            Button edit = (Button) v.findViewById(R.id.activity_lists_row_button_edit);

            if (textView != null) {
                textView.setText(element.getName());
                textView.setClickable(true);
                textView.setOnClickListener(onItemClickListener);
            }
            if (remove != null) {
                remove.setOnClickListener(onRemoveClickListener);
            }
            if (edit != null) {
                edit.setOnClickListener(onEditClickListener);
            }
        }

        if (selectedListId == element.getId()) {
            v.setBackgroundResource(R.color.activity_lists_row_selected);
        } else {
            v.setBackgroundResource(R.color.activity_lists_row_normal);
        }

        v.setTag(element);
        return v;
    }

    public void setOnEditClickListener(View.OnClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    public void setOnRemoveClickListener(View.OnClickListener onRemoveClickListener) {
        this.onRemoveClickListener = onRemoveClickListener;
    }

    public void setOnCancelClickListener(View.OnClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    public void setOnCommitClickListener(View.OnClickListener onCommitClickListener) {
        this.onCommitClickListener = onCommitClickListener;
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setSelectedListId(long selectedListId) {
        this.selectedListId = selectedListId;
    }
}
