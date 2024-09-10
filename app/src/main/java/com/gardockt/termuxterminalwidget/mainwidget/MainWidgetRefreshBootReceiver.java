package com.gardockt.termuxterminalwidget.mainwidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

/**
 * Triggers widget refresh on boot. Without it, the widgets are empty until refresh time is hit or
 * the user refreshes manually.
 */
public class MainWidgetRefreshBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, MainWidget.class));
            for (int widgetId : widgetIds) {
                MainWidget.updateWidget(context, appWidgetManager, widgetId, true);
            }
        }
    }
}
