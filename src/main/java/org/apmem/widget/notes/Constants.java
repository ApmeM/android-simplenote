package org.apmem.widget.notes;


import java.text.SimpleDateFormat;

public class Constants {
    public static final String INTENT_EXTRA_WIDGET_TEXT = "INTENT_EXTRA_WIDGET_TEXT";
    public static final String INTENT_EXTRA_WIDGET_ITEM_ID = "INTENT_EXTRA_WIDGET_ITEM_ID";
    public static final String INTENT_EXTRA_WIDGET_LIST_ID = "INTENT_EXTRA_WIDGET_LIST_ID";

	public static final String ACTION_WIDGET_UPDATE_FROM_ACTIVITY = "ACTION_WIDGET_UPDATE_FROM_ACTIVITY";
	public static final String ACTION_WIDGET_UPDATE_FROM_WIDGET = "ACTION_WIDGET_UPDATE_FROM_WIDGET";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
}
