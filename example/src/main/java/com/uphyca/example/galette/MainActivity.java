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

package com.uphyca.example.galette;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.uphyca.galette.Fields;
import com.uphyca.galette.LongFieldBuilder;
import com.uphyca.galette.SendEvent;
import com.uphyca.galette.SendScreenView;

import java.lang.reflect.Method;

public class MainActivity extends Activity {

    private int mClickCount;

    /**
     * Annotate arbitrary method to track
     */
    @Override
    @SendScreenView(screenName = "main")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(++mClickCount);
            }
        });
    }

    /**
     * Annotate arbitrary method to track
     */
    @SendEvent(category = "button", action = "click", label = "times", valueBuilder = ClickCountValueBuilder.class)
    private void onButtonClicked(int count) {
        // Do something
    }

    /**
     * Custom field builder to build click count as value
     */
    public static class ClickCountValueBuilder extends LongFieldBuilder {
        @Override
        public Long build(Fields fields, Long fieldValue, Object declaredObject, Method method, Object[] arguments) {
            return ((Integer) arguments[0]).longValue();
        }
    }
}
