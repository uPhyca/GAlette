package com.uphyca.galette;

import java.lang.reflect.Method;

/**
 * Stub
 */
public class GAlette {

    interface IGAlette {
        void sendAppView(Object owner, Method method, Object[] arguments);

        void sendEvent(Object owner, Method method, Object[] arguments);
    }

    private static IGAlette sIGAlette;

    public static void setIGAlette(IGAlette IGAlette) {
        sIGAlette = IGAlette;
    }

    static void sendAppView(Object owner, Method method, Object[] arguments) {
        sIGAlette.sendAppView(owner, method, arguments);
    }

    static void sendEvent(Object owner, Method method, Object[] arguments) {
        sIGAlette.sendEvent(owner, method, arguments);
    }
}
