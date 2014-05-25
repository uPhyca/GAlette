package android.content;

/**
 * Stub
 */
public class ContextWrapper extends Context {

    private Context mBase;

    public ContextWrapper(Context base) {
        mBase = base;
    }

    protected void attachBaseContext(Context base) {
        if (mBase != null) {
            throw new IllegalStateException("Base context already set");
        }
        mBase = base;
    }

    @Override
    public Context getApplicationContext() {
        return mBase.getApplicationContext();
    }
}
