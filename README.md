[![Build Status](https://travis-ci.org/uPhyca/GAlette.png?branch=master)](http://travis-ci.org/uPhyca/GAlette)

GAlette
-------
Annotation-triggered tracking along with Google Analytics for Android.


```java
@SendEvent(category = "HelloWorld", action = "sayHello", label="%1$s")
String sayHello (String name) {
  return format("Hello, %s.", name);
}
```


Usage
-----

Add the android-aspectj plugin to your `buildscript`'s `dependencies` section:
```groovy
classpath 'com.uphyca.gradle:gradle-android-aspectj-plugin:0.9.+'
```

Apply the `android-aspectj` plugin:
```groovy
apply plugin: 'android-aspectj'
```

Add the GAlette library to dependencies
```
dependencies {
  compile "com.uphyca.galette:galette:0.9.+"
}
```

Implements TrackerProvider to your Application class
```java
public class MyApplication extends Application implements TrackerProvider {

    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
        ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        mTracker = ga.newTracker("SET-YOUR-TRACKING-ID");
    }

    @Override
    public Tracker getByName(String trackerName) {
        return mTracker;
    }
}
```

Declare your application in AndroidManifest.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.uphyca.example.galette">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:name=".MyApplication" ...>

        <activity ... />
    </application>

</manifest>
```

Annotate @SendAppView to your methods to track appView
```java
public class MainActivity extends Activity {

    @Override
    @SendAppView(screenName = "main")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```

Annotate @SendEvent to your methods to track event
```java
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked();
            }
        });
    }

    @SendEvent(category = "button", action = "click")
    private void onButtonClicked() {
        // Do something
    }
}
```

Use string template to apply method parameters
```java
public class MainActivity extends Activity {

    private int mClickCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(++mClickCount);
            }
        });
    }

    @SendEvent(category = "button", action = "click", label = "times", value = "%1$d")
    private void onButtonClicked(int count) {
        // Do something
    }
}
```


Use FieldBuilder to build each field value
```java
public class MainActivity extends Activity {

    @Override
    @SendAppView(screenName = "", screenNameBuilder = BundleValueFieldBuilder.class)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(++mClickCount);
            }
        });
    }

    public static class BundleValueFieldBuilder extends FieldBuilder<String> {
        @Override
        public String build(Fields fields, String fieldValue, Object declaredObject, Method method, Object[] arguments) {
            return ((Bundle) arguments[0]).getString("foo");
        }
    }
}
```

License
-------

    Copyright 2014 uPhyca, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
