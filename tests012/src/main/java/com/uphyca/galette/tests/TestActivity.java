package com.uphyca.galette.tests;

import android.app.Activity;
import android.os.Bundle;
import com.uphyca.galette.*;

import java.lang.reflect.Method;

public class TestActivity extends Activity {

    @SendAppView(screenName = "screenName")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SendAppView(screenName = "screenName", screenNameBuilder = ScreenNameBuilder.class)
    void screenNameWithBuilders() {
    }

    @SendEvent(category = "category", action = "action")
    @SuppressWarnings("unused")
    void onButtonClick() {
    }

    @SendEvent(category = "category", action = "action", label = "label", value = 1L)
    @SuppressWarnings("unused")
    void onButtonClickWithOptionalFields() {
    }

    @SendEvent(category = "category", action = "action", categoryBuilder = CategoryBuilder.class, actionBuilder = ActionBuilder.class, labelBuilder = LabelBuilder.class, valueBuilder = ValueBuilder.class)
    @SuppressWarnings("unused")
    void onButtonClickWithBuilders() {
    }

    public static class ScreenNameBuilder extends StringFieldBuilder {
        @Override
        public String build(Fields fields, String fieldValue, Object declaredObject, Method method, Object[] arguments) {
            return "screenNameBuilder";
        }
    }

    public static class CategoryBuilder extends StringFieldBuilder {
        @Override
        public String build(Fields fields, String fieldValue, Object declaredObject, Method method, Object[] arguments) {
            return "categoryBuilder";
        }
    }

    public static class ActionBuilder extends StringFieldBuilder {
        @Override
        public String build(Fields fields, String fieldValue, Object declaredObject, Method method, Object[] arguments) {
            return "actionBuilder";
        }
    }

    public static class LabelBuilder extends StringFieldBuilder {
        @Override
        public String build(Fields fields, String fieldValue, Object declaredObject, Method method, Object[] arguments) {
            return "labelBuilder";
        }
    }

    public static class ValueBuilder extends LongFieldBuilder {
        @Override
        public Long build(Fields fields, Long fieldValue, Object declaredObject, Method method, Object[] arguments) {
            return Long.MAX_VALUE;
        }
    }
}
