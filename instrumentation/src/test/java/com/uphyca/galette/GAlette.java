package com.uphyca.galette;

import android.content.Context;

import java.lang.reflect.Method;

/**
 * Stub
 */
public class GAlette {

    interface IGAlette {
        void sendAppView(Object target, Context context, Method method, Object[] arguments);

        void sendEvent(Object target, Context context, Method method, Object[] arguments);

        void sendScreenView(Object target, Context context, Method method, Object[] arguments);
    }

    private static IGAlette sIGAlette;

    public static void setIGAlette(IGAlette IGAlette) {
        sIGAlette = IGAlette;
    }

    static void sendAppView(Object target, Context context, Method method, Object[] arguments) {
        sIGAlette.sendAppView(target, context, method, arguments);
    }

    static void sendScreenView(Object target, Context context, Method method, Object[] arguments) {
        sIGAlette.sendScreenView(target, context, method, arguments);
    }

    static void sendEvent(Object target, Context context, Method method, Object[] arguments) {
        sIGAlette.sendEvent(target, context, method, arguments);
    }
}
