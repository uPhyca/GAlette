package android.view;

import android.content.Context;
import android.content.ContextWrapper;

/**
 * Stub
 */
public class ContextThemeWrapper extends ContextWrapper {

    private int mThemeResource;
    private Context mBase;

    public ContextThemeWrapper() {
        super(null);
    }

    public ContextThemeWrapper(Context base, int themeres) {
        super(base);
        mBase = base;
        mThemeResource = themeres;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        mBase = newBase;
    }
}
