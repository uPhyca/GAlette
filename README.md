[![wercker status](https://app.wercker.com/status/da37f33da36130c5cda62f25542f0640/s "wercker status")](https://app.wercker.com/project/bykey/da37f33da36130c5cda62f25542f0640)


# GAlette

Annotation-triggered tracking along with Google Analytics for Android.


```java
@SendEvent(category = "HelloWorld", action = "sayHello", label="%1$s")
String sayHello (String name) {
  return format("Hello, %s.", name);
}
```

# Before you begin

Before using GAlette, make sure you have done following instructions described in https://developers.google.com/analytics/devguides/collection/android/v4/
- Before you begin
- Updating AndroidManifest.xml
- Initialize Trackers
- Create a configuration XML file


# Getting Started


Add the GAlette plugin to your `buildscript`'s `dependencies` section and apply the plugin:
```groovy
buildscript {
    repositories {
        ...
        mavenCentral()
    }
    dependencies {
        ...
        classpath 'com.uphyca.galette:galette-plugin:0.9.14'
    }
}

...
apply plugin: 'com.uphyca.galette'
```


Implements TrackerProvider to your Application class
```java
public class MyApplication extends Application implements TrackerProvider {

    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
        mTracker = ga.newTracker(R.xml.your_tracker_resource);
    }

    @Override
    public Tracker getByName(String trackerName) {
        return mTracker;
    }
}
```

Declare your application in AndroidManifest.xml.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.uphyca.example.galette">
  ...
    
  <application
    android:name=".MyApplication" ...>

    ...

  </application>
</manifest>
```

## Send Screen View

Annotate @SendScreenView to your methods to track appView

```java
public class MainActivity extends Activity {

    @Override
    @SendScreenView(screenName = "main")
    protected void onCreate(Bundle savedInstanceState) {
        ...
    }
}
```

## Measuring Events

Annotate @SendEvent to your methods to track event

```java
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            @SendEvent(category = "button", action = "click")
            public void onClick(View v) {
                onButtonClicked();
            }
        });
    }

}
```

## Advanced Usage

### String Templates

Use string template to apply method parameters

```java
public class MainActivity extends Activity {

    private int mClickCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(++mClickCount);
            }
        });
    }

    @SendEvent(category = "button", action = "click", label = "times", value = "%1$d")
    private void onButtonClicked(int count) {
        ...
    }
}
```

### Customizing field values

Use FieldBuilder to build each field value

```java
public class MainActivity extends Activity {

    @Override
    @SendScreenView(screenName = "foo", screenNameBuilder = BundleValueFieldBuilder.class)
    protected void onCreate(Bundle savedInstanceState) {
        ...
    }

    public static class BundleValueFieldBuilder implements FieldBuilder<String> {
        @Override
        public String build(Fields fields, String fieldValue, Object declaredObject, Method method, Object[] arguments) {
            // annotation's screenName value paths to fieldValue, in this case 'foo'
            return ((Bundle) arguments[0]).getString(fieldValue);
        }
    }
}
```


### Interceptor

Use HitInterceptor to send arbitrary values

```java
public class MyApplication extends Application implements TrackerProvider, HitInterceptor.Provider {

    private Tracker mTracker;

    private HitInterceptor hitInterceptor = new HitInterceptor() {
        @Override
        public void onEvent(EventFacade eventFacade) {
            eventFacade.setCustomDimension(1, Build.MODEL);
        }

        @Override
        public void onScreenView(ScreenViewFacade screenViewFacade) {
            screenViewFacade.setCustomDimension(1, Build.MODEL);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
        ga.setLocalDispatchPeriod(1);
        mTracker = ga.newTracker("SET-YOUR-TRACKING-ID");
    }

    @Override
    public Tracker getByName(String trackerName) {
        return mTracker;
    }

    @Override
    public HitInterceptor getHitInterceptor(String trackerName) {
        return hitInterceptor;
    }
}
```


### Supported types

Following types or these subclasses are supported.

- Application
- Activity
- Service
- Fragment (android.app or android.support.v4.app)
- View
- Context

or implements ContextProvider to orbitary classes.

```Java
public class MyClass implements ContextProvider {

    private Context mContext;
    
    public MyClass(Context context) {
        mContext = context;
    }
    
    @Override
    public Context get() {
        return mContext;
    }
    
    @SendScreenView(screenName = "my-class")
    void foo() {
    }
}
```


## Specify Google Play Services version

```
configurations.all {
    resolutionStrategy {
        force "com.google.android.gms:play-services-analytics:${playservicesVersion}"
    }
}
```

See more details [ResolutionStrategy - Gradle DSL Version 2.8](https://docs.gradle.org/current/dsl/org.gradle.api.artifacts.ResolutionStrategy.html)


## Proguard

```
-keepclassmembernames class * {
    @com.uphyca.galette.SendScreenView *;
    @com.uphyca.galette.SendEvent *;
}

-keepclassmembers class * implements com.uphyca.galette.FieldBuilder {
   <init>();
}
```


# License

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
