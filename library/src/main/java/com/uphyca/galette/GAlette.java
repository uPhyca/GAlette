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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Aspect to weave Google Analytics.
 */
@Aspect
@SuppressWarnings("unused")
public class GAlette {

    @Before("execution(* *.*(..)) && @annotation(analyticsAnnotation) ")
    public void sendAppView(JoinPoint joinPoint, SendAppView analyticsAnnotation) {
        final Tracker tracker = trackerFrom(joinPoint.getThis(), analyticsAnnotation.trackerName());
        if (tracker == null) {
            return;
        }
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();

        final HitBuilders.AppViewBuilder builder = new HitBuilders.AppViewBuilder();
        try {
            final FieldBuilder<String> screenNameBuilder = createStringFieldBuilder(analyticsAnnotation.screenNameBuilder());
            final String screenName = screenNameBuilder.build(Fields.SCREEN_NAME, analyticsAnnotation.screenName(), joinPoint.getThis(), method, joinPoint.getArgs());
            tracker.setScreenName(screenName);
        } finally {
            tracker.send(builder.build());
        }
    }

    @Before("execution(* *.*(..)) && @annotation(analyticsAnnotation) ")
    public void sendEvent(JoinPoint joinPoint, SendEvent analyticsAnnotation) {
        final Tracker tracker = trackerFrom(joinPoint.getThis(), analyticsAnnotation.trackerName());
        if (tracker == null) {
            return;
        }
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();

        final HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder();
        try {
            final FieldBuilder<String> categoryBuilder = createStringFieldBuilder(analyticsAnnotation.categoryBuilder());
            final String category = categoryBuilder.build(Fields.CATEGORY, analyticsAnnotation.category(), joinPoint.getThis(), method, joinPoint.getArgs());
            builder.setCategory(category);

            final FieldBuilder<String> actionBuilder = createStringFieldBuilder(analyticsAnnotation.actionBuilder());
            final String action = actionBuilder.build(Fields.ACTION, analyticsAnnotation.action(), joinPoint.getThis(), method, joinPoint.getArgs());
            builder.setAction(action);

            final FieldBuilder<String> labelBuilder = createStringFieldBuilder(analyticsAnnotation.labelBuilder());
            final String label = labelBuilder.build(Fields.LABEL, analyticsAnnotation.label(), joinPoint.getThis(), method, joinPoint.getArgs());
            if (!TextUtils.isEmpty(label)) {
                builder.setLabel(label);
            }

            final FieldBuilder<Long> valueBuilder = createLongFieldBuilder(analyticsAnnotation.valueBuilder());
            final Long value = valueBuilder.build(Fields.VALUE, analyticsAnnotation.value(), joinPoint.getThis(), method, joinPoint.getArgs());
            if (value != null) {
                builder.setValue(value);
            }
        } finally {
            tracker.send(builder.build());
        }
    }

    static final FieldBuilder<String> DEFAULT_STRING_FIELD_BUILDER = new StringFieldBuilder() {
        @Override
        public String build(Fields fields, String fieldValue, Object declaredObject, Method method, Object[] arguments) {
            if (arguments == null || arguments.length == 0) {
                return fieldValue;
            }
            return String.format(fieldValue, arguments);
        }
    };

    static final FieldBuilder<Long> DEFAULT_LONG_FIELD_BUILDER = new LongFieldBuilder() {
        @Override
        public Long build(Fields fields, Long fieldValue, Object declaredObject, Method method, Object[] arguments) {
            return fieldValue.longValue() > Long.MIN_VALUE ? fieldValue : null;
        }
    };

    final Map<Class<? extends FieldBuilder<?>>, FieldBuilder> mFieldBuilderMap = new HashMap<Class<? extends FieldBuilder<?>>, FieldBuilder>();

    FieldBuilder<String> createStringFieldBuilder(Class<? extends FieldBuilder<String>> fieldBuilderClass) {
        return createFieldBuilder(fieldBuilderClass, StringFieldBuilder.class, DEFAULT_STRING_FIELD_BUILDER);
    }

    synchronized FieldBuilder<Long> createLongFieldBuilder(Class<? extends FieldBuilder<Long>> fieldBuilderClass) {
        return createFieldBuilder(fieldBuilderClass, LongFieldBuilder.class, DEFAULT_LONG_FIELD_BUILDER);
    }

    <T> FieldBuilder<T> createFieldBuilder(Class<? extends FieldBuilder<T>> fieldBuilderClass, Class<? extends FieldBuilder<T>> defaultFieldBuilderClass, FieldBuilder<T> defaultFieldBuilder) {
        if (fieldBuilderClass == defaultFieldBuilderClass) {
            return defaultFieldBuilder;
        }
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

    static Tracker trackerFrom(Object obj, String trackerName) {
        final Context context = appContextFrom(obj);
        final Tracker tracker;
        try {
            return ((TrackerProvider) context).getByName(!TextUtils.isEmpty(trackerName) ? trackerName : null);
        } catch (ClassCastException e) {
            Log.w("GAlette", "Application must be a type of " + "com.uphyca.galette.TrackerProvider");
        }
        return null;
    }

    static Context appContextFrom(Object obj) {
        for (ContextAware each : CONTEXT_AWARE_LIST) {
            Context context = each.contextFrom(obj);
            if (context != null) {
                return (context instanceof Application) ? context : context.getApplicationContext();
            }
        }
        throw new IllegalArgumentException("Failed to get context from " + obj.getClass().getName());
    }

    static final List<ContextAware> CONTEXT_AWARE_LIST;

    static {
        ArrayList<ContextAware> list = new ArrayList<ContextAware>();
        list.add(new ApplicationAware());
        try {
            Class.forName("android.app.Fragment");
            list.add(new NativeFragmentContextAware());
        } catch (ClassNotFoundException ignore) {
        }
        try {
            Class.forName("android.support.v4.app.Fragment");
            list.add(new SupportFragmentContextAware());
        } catch (ClassNotFoundException ignore) {
        }
        list.add(new ContextWrapperAware());
        list.add(new ContextProviderImpl());
        CONTEXT_AWARE_LIST = Collections.unmodifiableList(list);
    }

    static interface ContextAware {
        Context contextFrom(Object obj);
    }

    static class ApplicationAware implements ContextAware {
        @Override
        public Context contextFrom(Object obj) {
            if (obj instanceof Application) {
                return (Context) obj;
            }
            if (obj instanceof Activity) {
                return ((Activity) obj).getApplication();
            }
            if (obj instanceof Service) {
                return ((Service) obj).getApplication();
            }
            return null;
        }
    }

    static class ContextWrapperAware implements ContextAware {
        @Override
        public Context contextFrom(Object obj) {
            return obj instanceof Context ? (Context) obj : null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    static class NativeFragmentContextAware implements ContextAware {
        @Override
        public Context contextFrom(Object obj) {
            return obj instanceof Fragment ? ((Fragment) obj).getActivity().getApplication() : null;
        }
    }

    static class SupportFragmentContextAware implements ContextAware {
        @Override
        public Context contextFrom(Object obj) {
            return obj instanceof android.support.v4.app.Fragment ? ((android.support.v4.app.Fragment) obj).getActivity().getApplication() : null;
        }
    }

    static class ContextProviderImpl implements ContextAware {
        @Override
        public Context contextFrom(Object obj) {
            return obj instanceof ContextProvider ? ((ContextProvider) obj).get() : null;
        }
    }
}
