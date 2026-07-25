# AGENTS.md - Termux Terminal Widget

## Project Overview

Termux Terminal Widget is an Android application that displays the output of shell commands in a home screen widget. It integrates with the [Termux](https://github.com/termux/termux-app) app to execute commands and display their output.

**License:** GNU General Public License v3 (GPLv3)  
**Min SDK:** 24 (Android 7.0)  
**Target SDK:** 34 (Android 14)

---

## Technologies

### Core Stack
- **Language:** Java 17
- **Build System:** Gradle 8.7 with Android Gradle Plugin 8.6.0
- **Platform:** Android (minSdk 24, targetSdk 34, compileSdk 34)

### Key Dependencies
- **AndroidX:** appcompat, constraintlayout, work-runtime
- **Termux Integration:** termux-shared v0.118.0
- **Reactive Programming:** RxJava 3.1.8
- **Utilities:** Guava 29.0-android
- **UI:** ColorPicker 1.1.0

### Architecture
- **AppWidgetProvider** for home screen widget
- **Foreground Service** (CommandRunnerService) to bypass Android battery optimization
- **WorkManager** and **AlarmManager** for scheduled widget refreshes
- **RxJava** for reactive event handling and preference change observation
- **View Binding** for UI access

---

## Project Structure

```
app/src/main/java/com/gardockt/termuxterminalwidget/
├── MainActivity.java              # App entry point, permission handling
├── MainApplication.java           # Application class
├── GlobalPreferences.java         # App-wide preferences model
├── GlobalPreferencesFragment.java # Settings UI fragment
├── GlobalPreferencesUtils.java    # Preferences utility methods
├── ColorScheme.java               # Color scheme model (FG/BG colors)
├── ColorPickerDialogInvoker.java  # Color picker dialog helper
├── PermissionInfo.java            # Permission definitions
├── components/
│   └── ColorButton.java           # Custom color button component
├── mainwidget/
│   ├── MainWidget.java            # Core widget provider (AppWidgetProvider)
│   ├── MainWidgetConfigureActivity.java # Widget configuration activity
│   ├── MainWidgetPreferences.java # Per-widget preferences model
│   ├── MainWidgetPreferencesManager.java # Widget preferences I/O
│   ├── MainWidgetRefresher.java   # Widget refresh orchestrator
│   ├── MainWidgetUpdater.java     # Widget update executor
│   ├── MainWidgetUpdateQueue.java # Update queue for API 26+
│   ├── MainWidgetUpdateWorker.java # WorkManager worker for updates
│   └── refreshers/
│       └── WorkMainWidgetRefresher.java # WorkManager-based refresher
├── shell/
│   ├── CommandRunner.java         # Executes commands via Termux intent
│   ├── CommandRunnerService.java  # Foreground service for command execution
│   ├── CommandRunnerServiceBootReceiver.java # Boot completed receiver
│   └── PluginResultsService.java  # Receives command results from Termux
├── util/
│   ├── RequestCodeManager.java    # Unique request code generator
│   └── TriConsumer.java           # 3-argument functional interface
└── widgetrefresher/
    ├── WidgetRefresher.java       # Refresher interface
    └── AlarmManagerWidgetRefresher.java # AlarmManager-based refresher
```

---

## Useful Commands

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build artifacts
./gradlew clean

# Build and install on connected device
./gradlew installDebug

# Check for dependency updates
./gradlew dependencyUpdates
```

### Development Commands

```bash
# Run lint checks
./gradlew lint

# Run lint with auto-fix
./gradlew lintFix

# Generate build report
./gradlew build --scan

# View dependency tree
./gradlew app:dependencies

# Check Gradle wrapper version
./gradlew --version
```

### Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Run specific test class
./gradlew testDebugUnitTest --tests "com.gardockt.termuxterminalwidget.ExampleTest"
```

---

## Best Practices

### Code Style
1. **Follow Java conventions:** Use camelCase for methods/variables, PascalCase for classes
2. **Use annotations:** Apply `@NonNull`, `@Nullable`, `@RequiresApi` annotations consistently
3. **Logging:** Use class-level TAG constant (`private static final String TAG = ClassName.class.getSimpleName()`)
4. **Null safety:** Check for null before operations; use `@Nullable` annotation for nullable parameters/returns

### Android-Specific
1. **API level checks:** Always guard API-specific calls with `Build.VERSION.SDK_INT` checks
2. **Permissions:** Request permissions at runtime for dangerous permissions; use the `PermissionInfo` class for definitions
3. **Services:** Use foreground services for long-running operations to comply with Android battery optimization
4. **PendingIntents:** Set appropriate flags (`FLAG_MUTABLE` for API 31+, `FLAG_IMMUTABLE` where possible)
5. **Widget updates:** Use `RemoteViews` for widget UI; avoid complex layouts

### Reactive Programming (RxJava)
1. **Dispose subscriptions:** Always dispose RxJava subscriptions to prevent memory leaks
2. **Use BehaviorSubject:** For event streams that need to emit the last value to new subscribers
3. **distinctUntilChanged():** Use to prevent unnecessary UI updates
4. **skip(1):** Skip initial emission when setting up change listeners

### Error Handling
1. **Empty commands:** Handle empty command strings gracefully (they can cause infinite execution)
2. **Service availability:** Check if `CommandRunnerService` is running before attempting operations
3. **Termux validation:** Verify Termux is installed and meets minimum version requirements

### Performance
1. **Widget refresh intervals:** Default to 15 minutes (900 seconds) to balance freshness and battery
2. **Queue updates:** Use `MainWidgetUpdateQueue` for API 26+ to serialize widget updates
3. **Lazy initialization:** Initialize components only when needed (e.g., bind service on first use)

---

## Guidelines

### When Modifying Widget Logic
- The widget uses `AppWidgetProvider` lifecycle methods (`onUpdate`, `onDeleted`, `onReceive`)
- Widget preferences are stored per-widget via `MainWidgetPreferencesManager`
- Click handling is done through `PendingIntent` broadcast to `MainWidget.onReceive`
- Output parsing strips ANSI escape sequences via regex: `\e\[[\d;?]*[a-zA-Z]`

### When Working with Termux Integration
- Commands are executed via Termux's `RUN_COMMAND` intent
- Results are received through `PluginResultsService`
- The app requires `com.termux.permission.RUN_COMMAND` permission
- Termux app must be installed (v0.109+) and queried via `<queries>` manifest entry

### When Modifying Preferences
- `GlobalPreferences`: App-wide settings (color scheme, text size, AlarmManager backend)
- `MainWidgetPreferences`: Per-widget settings (command, color scheme override, text size override, refresh interval)
- Null values in widget preferences indicate "use global setting"
- Use `GlobalPreferencesUtils.getObservable()` for reactive preference changes

### When Adding New Features
1. Check if the feature requires new permissions (update `PermissionInfo.java`)
2. Consider battery impact (use foreground service or WorkManager for background work)
3. Ensure backward compatibility (minSdk 24)
4. Update `AndroidManifest.xml` for new components (activities, services, receivers)
5. Test on multiple API levels, especially API 26+ (Oreo) for foreground service changes

---

## Known Limitations

1. **No session display:** Cannot display live terminal sessions due to Android security restrictions on Termux data directory access
2. **No ANSI escape sequences:** Currently stripped from output; color support may be added in future
3. **Foreground service required:** Battery optimization prevents background command execution

---

## Distribution

- **IzzyOnDroid:** Available via F-Droid repository
- **Codeberg:** Source releases available
- **APK naming:** `com.gardockt.termuxterminalwidget` (debug: `.debug` suffix)
