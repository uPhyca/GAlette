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
        ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        mTracker = ga.newTracker("SET-YOUR-TRACKING-ID");
    }

    @Override
    public Tracker getByName(String trackerName) {
        return mTracker;
    }
}
