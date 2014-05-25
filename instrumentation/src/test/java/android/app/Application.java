package android.app;

import android.content.Context;
import android.content.ContextWrapper;

/**
 * Stub
 */
public class Application extends ContextWrapper {

    public Application() {
        super(null);
    }

    public void attach(Context context) {
        attachBaseContext(context);
    }
}
