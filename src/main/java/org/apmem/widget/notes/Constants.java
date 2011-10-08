package org.apmem.widget.notes;


import android.content.Context;
import org.apmem.widget.notes.datastore.RepositoryFactory;
import org.apmem.widget.notes.datastore.repositoryFactory.RepositoryFactoryFake;

public class Constants {
    public static final String INTENT_EXTRA_WIDGET_ITEM_ID = "INTENT_EXTRA_WIDGET_ITEM_ID";

	public static final String ACTION_WIDGET_UPDATE_FROM_ACTIVITY = "ACTION_WIDGET_UPDATE_FROM_ACTIVITY";
	public static final String ACTION_WIDGET_UPDATE_FROM_WIDGET_READY_ITEM = "ACTION_WIDGET_UPDATE_FROM_WIDGET_READY_ITEM";

    private static RepositoryFactory currentRepositoryFactory = null;
    public static RepositoryFactory getCurrentRepositoryFactory(Context context){
        if (currentRepositoryFactory == null)
            currentRepositoryFactory = new RepositoryFactoryFake();

        return currentRepositoryFactory;
    }
}
