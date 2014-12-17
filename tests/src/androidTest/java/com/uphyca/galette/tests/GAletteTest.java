package com.uphyca.galette.tests;

import android.app.Application;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.uphyca.galette.TrackerProvider;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GAletteTest extends ActivityUnitTestCase<TestActivity> {

    public class MockApplication extends Application implements TrackerProvider {
        @Override
        public Tracker getByName(String trackerName) {
            return mTracker;
        }
    }

    Tracker mTracker;

    public GAletteTest() {
        super(TestActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ensureDexmakerCacheDir();

        mTracker = mock(Tracker.class);
        setApplication(new MockApplication());
    }

    private void ensureDexmakerCacheDir() {
        File cacheDir = getInstrumentation().getTargetContext()
                .getCacheDir();
        System.setProperty("dexmaker.dexcache", cacheDir.toString());
    }

    public void testSendAppView() throws Exception {
        startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        verify(mTracker).setScreenName("screenName");
        verify(mTracker).send(new HitBuilders.AppViewBuilder().build());
    }

    public void testSendAppViewWithBuilders() throws Exception {
        TestActivity testActivity = startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        testActivity.screenNameWithBuilders();

        InOrder inOrder = Mockito.inOrder(mTracker, mTracker);

        inOrder.verify(mTracker).setScreenName("screenName");
        inOrder.verify(mTracker).send(new HitBuilders.AppViewBuilder().build());

        inOrder.verify(mTracker).setScreenName("screenNameBuilder");
        inOrder.verify(mTracker).send(new HitBuilders.AppViewBuilder().build());
    }

    public void testSendEvent() throws Exception {
        TestActivity testActivity = startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        testActivity.onButtonClick();
        verify(mTracker).send(new HitBuilders.EventBuilder().setCategory("category").setAction("action").build());
    }

    public void testSendEventWithOptionalFields() throws Exception {
        TestActivity testActivity = startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        testActivity.onButtonClickWithOptionalFields();
        verify(mTracker).send(new HitBuilders.EventBuilder().setCategory("category").setAction("action").setLabel("label").setValue(1L).build());
    }

    public void testSendEventWithBuilders() throws Exception {
        TestActivity testActivity = startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        testActivity.onButtonClickWithBuilders();
        verify(mTracker).send(new HitBuilders.EventBuilder().setCategory("categoryBuilder").setAction("actionBuilder").setLabel("labelBuilder").setValue(Long.MAX_VALUE).build());
    }

}
