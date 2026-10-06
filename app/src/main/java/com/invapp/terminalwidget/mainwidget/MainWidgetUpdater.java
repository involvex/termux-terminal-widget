package com.invapp.terminalwidget.mainwidget;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.invapp.terminalwidget.shell.CommandRunnerService;
import com.invapp.terminalwidget.util.TriConsumer;

// Rationale: see MainWidgetUpdateQueue

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainWidgetUpdater {

    private static final String TAG = MainWidgetUpdater.class.getSimpleName();

    private final CommandRunnerService commandRunnerService;
    private final TriConsumer<Context, Integer, String> onCommandFinished;

    public MainWidgetUpdater(@NonNull CommandRunnerService commandRunnerService, @NonNull TriConsumer<Context, Integer, String> onCommandFinished) {
        this.commandRunnerService = commandRunnerService;
        this.onCommandFinished = onCommandFinished;
    }

    public void update(int widgetId) {
        MainWidgetPreferences preferences = MainWidgetPreferencesManager.load(commandRunnerService, widgetId);
        commandRunnerService.runCommand(preferences.getCommand(), (exitCode, stdout, stderr) -> onCommandFinished.accept(commandRunnerService, widgetId, stdout));
    }

}
