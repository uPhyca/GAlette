/*
 * Copyright (C) 2014 uPhyca Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uphyca.galette;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static java.lang.annotation.RetentionPolicy.CLASS;

public class GAlette {

    private static GAlette INSTANCE = new GAlette();

    static GAlette getInsntance() {
        return INSTANCE;
    }

    public static void sendAppView(Object target, Context appContext, Method method, Object[] arguments) {
        getInsntance().sendAppView0(target, appContext, method, arguments);
    }

    public static void sendEvent(Object target, Context appContext, Method method, Object[] arguments) {
        getInsntance().sendEvent0(target, appContext, method, arguments);
    }

    private void sendAppView0(Object target, Context appContext, Method method, Object[] arguments) {
        final SendAppView analyticsAnnotation = method.getAnnotation(SendAppView.class);
        final Tracker tracker = trackerFrom(appContext, analyticsAnnotation.trackerName());
        if (tracker == null) {
            return;
        }

        final HitBuilders.AppViewBuilder builder = new HitBuilders.AppViewBuilder();
        try {
            final FieldBuilder<String> screenNameBuilder = createStringFieldBuilder(analyticsAnnotation.screenNameBuilder());
            final String screenName = screenNameBuilder.build(Fields.SCREEN_NAME, analyticsAnnotation.screenName(), target, method, arguments);
            tracker.setScreenName(screenName);
        } finally {
            tracker.send(builder.build());
        }
    }

    private void sendEvent0(Object target, Context appContext, Method method, Object[] arguments) {
        final SendEvent analyticsAnnotation = method.getAnnotation(SendEvent.class);
        final Tracker tracker = trackerFrom(appContext, analyticsAnnotation.trackerName());
        if (tracker == null) {
            return;
        }

        final HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder();
        try {
            final FieldBuilder<String> categoryBuilder = createStringFieldBuilder(analyticsAnnotation.categoryBuilder());
            final String category = categoryBuilder.build(Fields.CATEGORY, analyticsAnnotation.category(), target, method, arguments);
            builder.setCategory(category);

            final FieldBuilder<String> actionBuilder = createStringFieldBuilder(analyticsAnnotation.actionBuilder());
            final String action = actionBuilder.build(Fields.ACTION, analyticsAnnotation.action(), target, method, arguments);
            builder.setAction(action);

            final FieldBuilder<String> labelBuilder = createStringFieldBuilder(analyticsAnnotation.labelBuilder());
            final String label = labelBuilder.build(Fields.LABEL, analyticsAnnotation.label(), target, method, arguments);
            if (!TextUtils.isEmpty(label)) {
                builder.setLabel(label);
            }

            final FieldBuilder<Long> valueBuilder = createLongFieldBuilder(analyticsAnnotation.valueBuilder());
            final Long value = valueBuilder.build(Fields.VALUE, analyticsAnnotation.value(), target, method, arguments);
            if (value != null) {
                builder.setValue(value);
            }
        } finally {
            tracker.send(builder.build());
        }
    }

    private static final FieldBuilder<String> DEFAULT_STRING_FIELD_BUILDER = new StringFieldBuilder() {
        @Override
        public String build(Fields fields, String fieldValue, Object declaredObject, Method method, Object[] arguments) {
            if (arguments == null || arguments.length == 0) {
                return fieldValue;
            }
            return String.format(fieldValue, arguments);
        }
    };

    private static final FieldBuilder<Long> DEFAULT_LONG_FIELD_BUILDER = new LongFieldBuilder() {
        @Override
        public Long build(Fields fields, Long fieldValue, Object declaredObject, Method method, Object[] arguments) {
            return fieldValue.longValue() > Long.MIN_VALUE ? fieldValue : null;
        }
    };

    private final Map<Class<? extends FieldBuilder<?>>, FieldBuilder> mFieldBuilderMap = new HashMap<Class<? extends FieldBuilder<?>>, FieldBuilder>();

    private FieldBuilder<String> createStringFieldBuilder(Class<? extends FieldBuilder<String>> fieldBuilderClass) {
        if (fieldBuilderClass == StringFieldBuilder.class) {
            return DEFAULT_STRING_FIELD_BUILDER;
        }
        return createFieldBuilder(fieldBuilderClass);
    }

    private FieldBuilder<Long> createLongFieldBuilder(Class<? extends FieldBuilder<Long>> fieldBuilderClass) {
        if (fieldBuilderClass == LongFieldBuilder.class) {
            return DEFAULT_LONG_FIELD_BUILDER;
        }
        return createFieldBuilder(fieldBuilderClass);
    }

    private synchronized <T> FieldBuilder<T> createFieldBuilder(Class<? extends FieldBuilder<T>> fieldBuilderClass) {
        if (!mFieldBuilderMap.containsKey(fieldBuilderClass)) {
            try {
                FieldBuilder fieldBuilder = fieldBuilderClass.newInstance();
                mFieldBuilderMap.put(fieldBuilderClass, fieldBuilder);
            } catch (InstantiationException e) {
                throw new IllegalArgumentException("Not found no-argument public constructor in " + fieldBuilderClass.getName(), e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Not found no-argument public constructor in " + fieldBuilderClass.getName(), e);
            }
        }
        return mFieldBuilderMap.get(fieldBuilderClass);
    }

    private static Tracker trackerFrom(Context appContext, String trackerName) {
        try {
            return ((TrackerProvider) appContext).getByName(!TextUtils.isEmpty(trackerName) ? trackerName : null);
        } catch (ClassCastException e) {
            Log.w("GAlette", "Application must be a type of " + "com.uphyca.galette.TrackerProvider");
        }
        return null;
    }

    @Target(ElementType.TYPE)
    @Retention(CLASS)
    private @interface Baked {
    }
}
