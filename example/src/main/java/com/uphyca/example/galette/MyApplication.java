package com.uphyca.example.galette;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.uphyca.galette.TrackerProvider;

public class MyApplication extends Application implements TrackerProvider {

    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
        ga.setLocalDispatchPeriod(1);

        // To enable debug logging on a device run:
        // adb shell setprop log.tag.GAv4 DEBUG
        // adb logcat -s GAv4

        // Logger is deprecated. To enable debug logging, please run:
        // adb shell setprop log.tag.GAv4 DEBUG
        // ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);

        mTracker = ga.newTracker("SET-YOUR-TRACKING-ID");
    }

    @Override
    public Tracker getByName(String trackerName) {
        return mTracker;
    }
}
