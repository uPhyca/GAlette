[![Build Status](https://travis-ci.org/uPhyca/galette.png?branch=master)](http://travis-ci.org/uPhyca/galette)

GAlette
-------
Annotation-triggered tracking along with Google Analytics for Android.



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
```
package com.uphyca.example.galette;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.uphyca.galette.TrackerProvider;

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
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.uphyca.example.galette">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:name=".MyApplication"
        android:allowBackup="true"
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme">

        <activity ... />
    </application>

</manifest>
```

Annotate @SendAppView to your methods to track appView
```
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
```
public class MainActivity extends Activity {

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

    @SendEvent(category = "button", action = "click")
    private void onButtonClicked(int count) {
        // Do something
    }
}
```

User FieldBuilder to build each field value
```
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

    @SendEvent(category = "button", action = "click", label = "times", valueBuilder = ClickCountValueBuilder.class)
    private void onButtonClicked(int count) {
        // Do something
    }

    public static class ClickCountValueBuilder extends LongFieldBuilder {
        @Override
        public Long build(Fields fields, Long fieldValue, Object declaredObject, Method method, Object[] arguments) {
            return ((Integer) arguments[0]).longValue();
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
