package com.uphyca.galette;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GAletteInstrumentationTest {

    GAlette.IGAlette galette;

    @Before
    public void setUp() throws Exception {
        galette = mock(GAlette.IGAlette.class);
        GAlette.setIGAlette(galette);
    }

    @Test
    public void weaveSendAppView() throws Exception {
        // Process classes.
        GAletteInstrumentation underTest = new GAletteInstrumentation();
        underTest.processFile(new File(getClass().getResource("GAletteInstrumentationTest$TestClassForSendAppView.class").getFile()));

        // Load instrumented classes.
        Class<?> testClass = Class.forName("com.uphyca.galette.GAletteInstrumentationTest$TestClassForSendAppView");
        Object o = testClass.newInstance();

        // Invoke method.
        Method method = testClass.getDeclaredMethod("show", new Class[]{});
        method.invoke(o, new Object[]{});

        // Verify interactions.
        verify(galette).sendAppView(o, method, new Object[]{});
    }

    static class TestClassForSendAppView {
        @SendAppView
        void show() {
        }
    }

    @Test
    public void weaveSendEvent() throws Exception {
        // Process classes.
        GAletteInstrumentation underTest = new GAletteInstrumentation();
        underTest.processFile(new File(getClass().getResource("GAletteInstrumentationTest$TestClassForSendEvent.class").getFile()));

        // Load instrumented classes.
        Class<?> testClass = Class.forName("com.uphyca.galette.GAletteInstrumentationTest$TestClassForSendEvent");
        Object o = testClass.newInstance();

        // Invoke method.
        Method method = testClass.getDeclaredMethod("click", new Class[]{});
        method.invoke(o, new Object[]{});

        // Verify interactions.
        verify(galette).sendEvent(o, method, new Object[]{});
    }

    static class TestClassForSendEvent {
        @SendEvent
        void click() {
        }
    }
}
