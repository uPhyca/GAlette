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

import java.lang.reflect.Method;

/**
 * Helper class for building field value.
 * The Constructor should be no-arguments and public.
 *
 * @param <T> type of the field.
 * @see SendAppView#screenNameBuilder()
 * @see SendEvent#categoryBuilder()
 * @see SendEvent#actionBuilder()
 * @see SendEvent#labelBuilder()
 * @see SendEvent#valueBuilder()
 */
public interface FieldBuilder<T> {

    /**
     * @param fields         the name of the field
     * @param fieldValue     the value that is specified in annotations
     * @param declaredObject the instance on which the method was invoked
     * @param method         the method invoked on the instance
     * @param arguments      an array of objects containing the parameters passed to the method, or null if no arguments are expected. Primitive types are boxed.
     * @return field value
     */
    T build(Fields fields, T fieldValue, Object declaredObject, Method method, Object[] arguments);
}
