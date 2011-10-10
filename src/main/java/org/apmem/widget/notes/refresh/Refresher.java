package org.apmem.widget.notes.refresh;

import android.content.Context;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 02.10.11
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public interface Refresher {
    public void updateWidget(Context context, int appWidgetId);
    public void updateList(Context context, int listId);
}
