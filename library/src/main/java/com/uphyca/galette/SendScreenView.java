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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <a href="https://developers.google.com/analytics/devguides/collection/android/v4/screens">Screens - Android SDK v4  _  Analytics for Android  _  Google Developers</a>
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface SendScreenView {

    String trackerName() default "";

    /**
     * The name of an application screen.
     */
    String screenName() default "";

    Class<? extends FieldBuilder<String>> screenNameBuilder() default StringFieldBuilder.class;
}
