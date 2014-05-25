package android.app;

import android.view.ContextThemeWrapper;

/**
 * Stub
 */
public class Activity extends ContextThemeWrapper {

    private Application mApplication;

    public Application getApplication() {
        return mApplication;
    }

    public void attach(Application application) {
        mApplication = application;
    }
}
