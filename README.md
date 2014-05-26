[![wercker status](https://app.wercker.com/status/da37f33da36130c5cda62f25542f0640/m "wercker status")](https://app.wercker.com/project/bykey/da37f33da36130c5cda62f25542f0640)


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
    dependencies {
        ...
        classpath 'com.uphyca.galette:galette-plugin:0.9.+'
    }
}

...
apply plugin: 'galette'
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

Annotate @SendAppView to your methods to track appView

```java
public class MainActivity extends Activity {

    @Override
    @SendAppView(screenName = "main")
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
            public void onClick(View v) {
                onButtonClicked();
            }
        });
    }

    @SendEvent(category = "button", action = "click")
    private void onButtonClicked() {
        ...
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
    @SendAppView(screenName = "foo", screenNameBuilder = BundleValueFieldBuilder.class)
    protected void onCreate(Bundle savedInstanceState) {
        ...
    }

    public static class BundleValueFieldBuilder extends FieldBuilder<String> {
        @Override
        public String build(Fields fields, String fieldValue, Object declaredObject, Method method, Object[] arguments) {
            // annotation's screenName value paths to fieldValue, in this case 'foo'
            return ((Bundle) arguments[0]).getString(fieldValue);
        }
    }
}
```

## Proguard

```
-keepclassmembernames class * {
    @com.uphyca.galette.SendAppView *;
    @com.uphyca.galette.SendEvent *;
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
