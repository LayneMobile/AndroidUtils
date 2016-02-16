/*
 * Copyright 2016 Layne Mobile, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.laynemobile.android.widget;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;

import com.laynemobile.android.util.ReflectionUtils;

public class AndroidDrawerLayout extends DrawerLayout {
    private static final String TAG = AndroidDrawerLayout.class.getSimpleName();

    public AndroidDrawerLayout(Context context) {
        super(context);
        init();
    }

    public AndroidDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AndroidDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // Set min margin to 0 due to fix library code
        try {
            ReflectionUtils.setIntFieldValue(this, "mMinDrawerMargin", 0);
        } catch (ReflectionUtils.ReflectionException e) {
            Log.e(TAG, "unable to set min drawer margin", e);
        }
    }
}
