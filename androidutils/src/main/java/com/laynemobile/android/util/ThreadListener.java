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

package com.laynemobile.android.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Interface for listeners that are designated to a specific thread */
public interface ThreadListener {
    @IntDef({
            THREAD_MAIN,
            THREAD_BACKGROUND,
            THREAD_IMMEDIATE
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Thread { }

    int THREAD_MAIN = 0;
    int THREAD_BACKGROUND = 1;
    int THREAD_IMMEDIATE = 2;

    /**
     * Returns the {@link Thread} in which this ThreadListener intends to listen for callbacks.
     *
     * @return the thread this instance intends to listen
     */
    @Thread int getListeningThread();
}
