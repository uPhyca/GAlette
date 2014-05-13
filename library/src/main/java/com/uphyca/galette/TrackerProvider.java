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

import com.google.android.gms.analytics.Tracker;

/**
 * Provides instances of Tracker.
 * The application should implement this interface and returns the arbitrary Tracker instances.
 * <a href="https://developers.google.com/analytics/devguides/collection/android/v4/#tracking-methods">Google Analytics SDK v4 for Android - Getting Started</a>
 */
public interface TrackerProvider {

    /**
     * Provides instances of Tracker.
     *
     * @param trackerName Identifier of the tracker. null if unspecified.
     * @return a Tracker instance
     */
    Tracker getByName(String trackerName);
}
