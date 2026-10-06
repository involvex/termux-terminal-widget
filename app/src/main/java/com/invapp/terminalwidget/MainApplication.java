package com.invapp.terminalwidget;

import android.app.Application;

import com.invapp.terminalwidget.mainwidget.MainWidget;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        MainWidget.setup(this);
        super.onCreate();
    }
}
