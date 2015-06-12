package com.uphyca.example.galette;

import android.app.Application;
import android.os.Build;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.uphyca.galette.HitInterceptor;
import com.uphyca.galette.TrackerProvider;

public class MyApplication extends Application implements TrackerProvider, HitInterceptor.Provider {

    private Tracker mTracker;

    private HitInterceptor hitInterceptor = new HitInterceptor() {
        @Override
        public void onEvent(EventFacade eventFacade) {
            eventFacade.setCustomDimension(1, Build.MODEL);
        }

        @Override
        public void onScreenView(ScreenViewFacade screenViewFacade) {
            screenViewFacade.setCustomDimension(1, Build.MODEL);
        }
    };

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

    @Override
    public HitInterceptor getHitInterceptor(String trackerName) {
        return hitInterceptor;
    }
}
