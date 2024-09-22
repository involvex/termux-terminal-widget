package com.gardockt.termuxterminalwidget.mainwidget;

import android.content.Context;

import androidx.annotation.NonNull;

import com.gardockt.termuxterminalwidget.mainwidget.refreshers.WorkMainWidgetRefresher;
import com.gardockt.termuxterminalwidget.widgetrefresher.WidgetRefresher;

public class MainWidgetRefresher {

    private static WidgetRefresher refresher = null;

    @NonNull
    public static WidgetRefresher getInstance(@NonNull Context context) {
        if (refresher == null) {
            refresher = new WorkMainWidgetRefresher(context);
        }
        return refresher;
    }
}
