package com.uphyca.galette.tests;

import android.app.Application;
import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.uphyca.galette.HitInterceptor;
import com.uphyca.galette.TrackerProvider;

import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HitInterceptorTest extends ActivityUnitTestCase<TestActivity> {

    public class MockApplication extends Application implements TrackerProvider, HitInterceptor.Provider {
        @Override
        public Tracker getByName(String trackerName) {
            return mTracker;
        }

        @Override
        public HitInterceptor getHitInterceptor(String s) {
            return mHitInterceptor;
        }
    }

    Tracker mTracker;

    HitInterceptor mHitInterceptor;

    public HitInterceptorTest() {
        super(TestActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ensureDexmakerCacheDir();
        mTracker = mock(Tracker.class);
        mHitInterceptor = mock(HitInterceptor.class);
        setApplication(new MockApplication());
    }

    private void ensureDexmakerCacheDir() {
        File cacheDir = getInstrumentation().getTargetContext()
                .getCacheDir();
        System.setProperty("dexmaker.dexcache", cacheDir.toString());
    }

    public void testSendScreenView() throws Exception {
        startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        verify(mHitInterceptor).onScreenView(any(HitInterceptor.ScreenViewFacade.class));
    }

    public void testSendEvent() throws Exception {
        TestActivity testActivity = startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        testActivity.onButtonClick();
        verify(mHitInterceptor).onEvent(any(HitInterceptor.EventFacade.class));
    }
}
