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
 * @see com.google.android.gms.analytics.HitBuilders.EventBuilder
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface SendEvent {

    String trackerName() default "";

    String category();

    String action();

    String label() default "";

    long value() default Long.MIN_VALUE;

    Class<? extends FieldBuilder<String>> categoryBuilder() default StringFieldBuilder.class;

    Class<? extends FieldBuilder<String>> actionBuilder() default StringFieldBuilder.class;

    Class<? extends FieldBuilder<String>> labelBuilder() default StringFieldBuilder.class;

    Class<? extends FieldBuilder<Long>> valueBuilder() default LongFieldBuilder.class;
}
