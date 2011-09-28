package org.apmem.widget.notes;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class SampleWidgetActivity extends Activity {
    private static final String TAG = "SampleWidgetActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView text = (TextView) this.findViewById(R.id.text);
        text.setText("appWidgetId:" + this.getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0));
    }

    public void sendTextToWidget(View button) {
        int appWidgetId = this.getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        String widgetText = this.getIntent().getStringExtra(Constants.INTENT_EXTRA_WIDGET_TEXT) + Constants.dateFormat.format(new Date());

        TextView text = (TextView) this.findViewById(R.id.text);
        text.setText(String.format("Send '%s' to %d widget", widgetText, appWidgetId));

        Intent intent = new Intent(Constants.ACTION_WIDGET_UPDATE_FROM_ACTIVITY);
        intent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT, widgetText);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        this.sendBroadcast(intent);
    }
}