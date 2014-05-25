package com.uphyca.galette;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.objectweb.asm.MethodVisitor;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GAletteInstrumentationTest {

    GAlette.IGAlette galette;

    @Before
    public void setUp() throws Exception {
        galette = mock(GAlette.IGAlette.class);
        GAlette.setIGAlette(galette);
    }

    private URL getJarFile(URL classPath) throws Exception {
        String classFile = classPath.getFile();
        return new URL(classFile.substring(0, classFile.indexOf('!')));
    }

    private URL getClassesDir(Class<?> clazz) throws Exception {
        String classFile = clazz.getResource(clazz.getSimpleName() + ".class").getFile();
        return new URL("file://" + classFile.substring(0, classFile.indexOf(clazz.getName().replace('.', '/'))));
    }

    private ClassLoader getIsolatedClassLoader() throws Exception {
        final URL asmJar = getJarFile(MethodVisitor.class.getResource("ClassReader.class"));
        return new URLClassLoader(new URL[]{asmJar, getClassesDir(GAletteInstrumentation.class), getClassesDir(getClass())}, Class.class.getClassLoader());
    }

    private File getClassFile(String className) {
        return new File(getClass().getResource("/" + className.replace('.', '/') + ".class").getFile());
    }

    private Class<?> processClass(String className) throws Exception {
        final ClassLoader classLoader = getIsolatedClassLoader();
        Class<?> inst = classLoader.loadClass(GAletteInstrumentation.class.getName());
        Method processFile = inst.getMethod("processFile", new Class[]{File.class});
        processFile.invoke(inst.newInstance(), new Object[]{getClassFile(className)});
        return classLoader.loadClass(className);
    }

    @Test
    public void weaveSendAppView() throws Exception {
        // Process classes.
        processClass("com.uphyca.galette.GAletteInstrumentationTest$TestClassForSendAppView");

        // Load instrumented classes.
        TestClassForSendAppView instrumentedObject = new TestClassForSendAppView();
        Application app = new Application();
        instrumentedObject.attach(app);

        // Invoke method.
        instrumentedObject.show();

        // Verify interactions.
        Method method = instrumentedObject.getClass().getDeclaredMethod("show", new Class[]{});
        verify(galette).sendAppView(eq(instrumentedObject), eq(app), eq(method), eq(new Object[]{}));
    }

    static class TestClassForSendAppView extends Activity {
        @SendAppView
        void show() {
        }
    }


    @Test
    public void weaveSendEvent() throws Exception {
        // Process classes.
        processClass("com.uphyca.galette.GAletteInstrumentationTest$TestClassForSendEvent");

        // Load instrumented classes.
        TestClassForSendEvent instrumentedObject = new TestClassForSendEvent();
        Application app = new Application();
        instrumentedObject.attach(app);

        // Invoke method.
        instrumentedObject.click();

        // Verify interactions.
        Method method = instrumentedObject.getClass().getDeclaredMethod("click", new Class[]{});
        verify(galette).sendEvent(eq(instrumentedObject), eq(app), eq(method), eq(new Object[]{}));
    }

    static class TestClassForSendEvent extends Activity {
        @SendEvent
        void click() {
        }
    }

    @Test
    public void weaveService() throws Exception {
        // Process classes.
        processClass("com.uphyca.galette.GAletteInstrumentationTest$TestServiceClass");

        // Load instrumented classes.
        TestServiceClass instrumentedObject = new TestServiceClass();
        Application app = new Application();
        instrumentedObject.setApplication(app);

        // Invoke method.
        instrumentedObject.click();

        // Verify interactions.
        Method method = instrumentedObject.getClass().getDeclaredMethod("click", new Class[]{});
        verify(galette).sendEvent(eq(instrumentedObject), eq(app), eq(method), eq(new Object[]{}));
    }

    static class TestServiceClass extends Service {
        @SendEvent
        void click() {
        }
    }

    @Test
    public void weaveApplication() throws Exception {
        // Process classes.
        processClass("com.uphyca.galette.GAletteInstrumentationTest$TestApplicationClass");

        // Load instrumented classes.
        TestApplicationClass instrumentedObject = new TestApplicationClass();

        // Invoke method.
        instrumentedObject.click();

        // Verify interactions.
        Method method = instrumentedObject.getClass().getDeclaredMethod("click", new Class[]{});
        verify(galette).sendEvent(eq(instrumentedObject), eq(instrumentedObject), eq(method), eq(new Object[]{}));
    }

    static class TestApplicationClass extends Application {
        @SendEvent
        void click() {
        }
    }

    @Test
    public void weaveView() throws Exception {
        // Process classes.
        processClass("com.uphyca.galette.GAletteInstrumentationTest$TestViewClass");

        // Load instrumented classes.
        Application app = new Application();
        TestViewClass instrumentedObject = new TestViewClass(app);

        // Invoke method.
        instrumentedObject.click();

        // Verify interactions.
        Method method = instrumentedObject.getClass().getDeclaredMethod("click", new Class[]{});
        verify(galette).sendEvent(eq(instrumentedObject), eq(app), eq(method), eq(new Object[]{}));
    }

    static class TestViewClass extends View {
        TestViewClass(Context context) {
            super(context);
        }

        @SendEvent
        void click() {
        }
    }

    @Test
    public void weaveContextProvider() throws Exception {
        // Process classes.
        processClass("com.uphyca.galette.GAletteInstrumentationTest$TestContextProviderClass");

        // Load instrumented classes.
        final Application app = new Application();
        app.attach(new ContextWrapper(app) {
            @Override
            public Context getApplicationContext() {
                return app;
            }
        });
        TestContextProviderClass instrumentedObject = new TestContextProviderClass(app);

        // Invoke method.
        instrumentedObject.click();

        // Verify interactions.
        Method method = instrumentedObject.getClass().getDeclaredMethod("click", new Class[]{});
        verify(galette).sendEvent(eq(instrumentedObject), eq(app), eq(method), eq(new Object[]{}));
    }

    static class TestContextProviderClass implements ContextProvider {


        private Context mContext;

        TestContextProviderClass(Context context) {
            mContext = context;
        }

        @SendEvent
        void click() {
        }

        @Override
        public Context get() {
            return mContext;
        }
    }

    @Test
    public void weaveContext() throws Exception {
        // Process classes.
        processClass("com.uphyca.galette.GAletteInstrumentationTest$TestContextClass");

        // Load instrumented classes.
        final Application app = new Application();
        app.attach(new ContextWrapper(app) {
            @Override
            public Context getApplicationContext() {
                return app;
            }
        });
        TestContextClass instrumentedObject = new TestContextClass(app);

        // Invoke method.
        instrumentedObject.click();

        // Verify interactions.
        Method method = instrumentedObject.getClass().getDeclaredMethod("click", new Class[]{});
        verify(galette).sendEvent(eq(instrumentedObject), eq(app), eq(method), eq(new Object[]{}));
    }

    static class TestContextClass extends ContextWrapper {

        TestContextClass(Context context) {
            super(context);
        }

        @SendEvent
        void click() {
        }
    }

    @Test
    public void weaveFragment() throws Exception {
        // Process classes.
        processClass("com.uphyca.galette.GAletteInstrumentationTest$TestFragmentClass");

        // Load instrumented classes.
        TestFragmentClass instrumentedObject = new TestFragmentClass();
        Application app = new Application();
        Activity activity = new Activity();
        activity.attach(app);
        instrumentedObject.setActivity(activity);

        // Invoke method.
        instrumentedObject.click();

        // Verify interactions.
        Method method = instrumentedObject.getClass().getDeclaredMethod("click", new Class[]{});
        verify(galette).sendEvent(eq(instrumentedObject), eq(app), eq(method), eq(new Object[]{}));
    }

    static class TestFragmentClass extends Fragment {

        @SendEvent
        void click() {
        }
    }

    @Test
    public void weaveSupportFragment() throws Exception {
        // Process classes.
        processClass("com.uphyca.galette.GAletteInstrumentationTest$TestSupportFragmentClass");

        // Load instrumented classes.
        TestSupportFragmentClass instrumentedObject = new TestSupportFragmentClass();
        Application app = new Application();
        Activity activity = new Activity();
        activity.attach(app);
        instrumentedObject.setActivity(activity);

        // Invoke method.
        instrumentedObject.click();

        // Verify interactions.
        Method method = instrumentedObject.getClass().getDeclaredMethod("click", new Class[]{});
        verify(galette).sendEvent(eq(instrumentedObject), eq(app), eq(method), eq(new Object[]{}));
    }

    static class TestSupportFragmentClass extends android.support.v4.app.Fragment {

        @SendEvent
        void click() {
        }
    }

    public void weaveNestedClass() throws Exception {
        // Process classes.
        GAletteInstrumentation underTest = new GAletteInstrumentation();
        underTest.processFile(new File(getClass().getResource("GAletteInstrumentationTest$NestedTestClass$TestClassForSendEvent.class").getFile()));

        // Load instrumented classes.
        Class<?> testClass = Class.forName("com.uphyca.galette.GAletteInstrumentationTest$NestedTestClass$TestClassForSendEvent");
        Class<?> enclosingClass = Class.forName("com.uphyca.galette.GAletteInstrumentationTest$NestedTestClass");
        Object enclosingInstance = enclosingClass.newInstance();
        Object o = testClass.getDeclaredConstructors()[0].newInstance(enclosingInstance);

        // Invoke method.
        Application app = new Application();
        ((Activity) o).attach(app);
        Method method = testClass.getDeclaredMethod("click", new Class[]{});
        method.invoke(o, new Object[]{});

        // Verify interactions.
        ArgumentCaptor<Object[]> captor = ArgumentCaptor.forClass(Object[].class);
        verify(galette).sendEvent(eq(o), eq(app), eq(method), captor.capture());
        captor.getValue();
    }

    static class NestedTestClass extends Activity {

        class TestClassForSendEvent {
            @SendEvent
            void click() {
            }
        }
    }

}
