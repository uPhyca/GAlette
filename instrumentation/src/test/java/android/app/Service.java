package android.app;

import android.content.ContextWrapper;

/**
 * Stub
 */
public class Service extends ContextWrapper {

    private Application mApplication;

    public Service() {
        super(null);
    }

    public Application getApplication() {
        return mApplication;
    }

    public void setApplication(Application application) {
        mApplication = application;
    }
}
