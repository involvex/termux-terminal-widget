package com.gardockt.termuxterminalwidget;

import android.app.Application;

import com.gardockt.termuxterminalwidget.mainwidget.MainWidget;

/**
 * Triggers widget refresh when the app launches. Without it, after boot widgets are empty until
 * refresh time is hit or the user refreshes manually.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        MainWidget.updateAll(this);
        super.onCreate();
    }
}
